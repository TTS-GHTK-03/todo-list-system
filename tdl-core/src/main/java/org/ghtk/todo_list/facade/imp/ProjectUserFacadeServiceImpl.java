package org.ghtk.todo_list.facade.imp;

import static org.ghtk.todo_list.constant.ActivityLogConstant.ProjectUserAction.*;
import static org.ghtk.todo_list.constant.CacheConstant.INVITE_KEY;
import static org.ghtk.todo_list.constant.CacheConstant.SHARE_KEY;

import io.jsonwebtoken.Claims;
import java.sql.Time;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.RoleProjectUser;
import org.ghtk.todo_list.constant.URL;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.dto.response.AuthUserResponse;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.EmailNotInviteException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.RoleProjectUserNotFound;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.model.request.RedisInviteUserRequest;
import org.ghtk.todo_list.service.ActivityLogService;
import org.ghtk.todo_list.service.AuthTokenService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class ProjectUserFacadeServiceImpl implements ProjectUserFacadeService {

  private final ProjectUserService projectUserService;
  private final ProjectService projectService;
  private final AuthUserService authUserService;
  private final AuthTokenService authTokenService;
  private final RedisCacheService redisCacheService;
  private final TaskAssigneesService taskAssigneesService;
  private final EmailHelper emailHelper;
  private final ActivityLogService activityLogService;

  @Override
  public void inviteUser(String userId, String projectId, String email, String role) {
    log.info("(inviteUser)user: {}, project: {}", userId, projectId);

    validateProjectId(projectId);

    if(!RoleProjectUser.isValid(role)){
      log.error("(inviteUser)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    var subject = "Admin has shared with you his project";
    var param = new HashMap<String, Object>();
    String emailEncode = encodeEmail(email);

    param.put("email", email);
    param.put("domain", URL.DOMAIN);
    param.put("emailEncode", emailEncode);
    emailHelper.send(subject, email, "email-invite-to-project-template", param);

    RedisInviteUserRequest redisInviteUserRequest = new RedisInviteUserRequest();
    redisInviteUserRequest.setProjectId(projectId);
    redisInviteUserRequest.setRole(role);

    redisCacheService.save(INVITE_KEY, email, redisInviteUserRequest);

    var notification = new ActivityLog();
    notification.setAction(INVITE_USER);
    notification.setUserId(userId);
    activityLogService.create(notification);
  }

  @Override
  public String accept(String emailEncode) {
    log.info("(accept)email: {}", emailEncode);

    String email = decryptionEmailEncode(emailEncode);
    var redisRequest = redisCacheService.get(INVITE_KEY, email);

    if (redisRequest.isEmpty()) {
      log.error("(accept)email: {} isn't invited", email);
      throw new EmailNotInviteException();
    }

    RedisInviteUserRequest redisInviteUserRequest = (RedisInviteUserRequest) redisRequest.get();
    String projectId = redisInviteUserRequest.getProjectId();
    String role = redisInviteUserRequest.getRole();

    validateProjectId(projectId);

    if (!RoleProjectUser.isValid(role)) {
      log.error("(accept)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    if (!authUserService.existsByEmail(email)) {
      log.info("(accept)email: {} not found", email);
      //call login page
    } else {
      log.info("(accept)email: {} is existed", email);
      String userId = authUserService.getUserId(email);
      ProjectUser projectUser = projectUserService.createProjectUser(userId, projectId, role);
      //call project page
      String acceptEmailKey = generateAcceptEmailKey(email, projectId, role);
    }

    var notification = new ActivityLog();
    notification.setAction(ACCEPT_INVITE);
    notification.setUserId(authUserService.findByEmail(email).getId());
    activityLogService.create(notification);
    return null;
  }

  @Override
  public void shareProject(String userId, String projectId, String email, String role, Time expireTime) {
    log.info("(shareProject)user: {}, project: {}, email: {}, role: {}, expireDate: {}", userId, projectId, email, role, expireTime);

    validateProjectId(projectId);

    if(!RoleProjectUser.isValid(role)){
      log.error("(shareProject)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    var subject = "Admin shared you to their project in Todo List";
    var param = new HashMap<String, Object>();

    int hours = expireTime.getHours();
    int minutes = expireTime.getMinutes();
    int second = expireTime.getSeconds();

    String shareToken = authTokenService.generateShareToken(email, role, projectId, (long)(hours * 3600 + minutes * 60 + second) * 1000);

    param.put("role", role);
    param.put("email", email);
    param.put("domain", URL.DOMAIN);
    param.put("shareToken", shareToken);
    emailHelper.send(subject, email, "email-share-project-template", param);

    var notification = new ActivityLog();
    notification.setAction(SHARE_KEY);
    notification.setUserId(userId);
    activityLogService.create(notification);
  }

  @Override
  public void viewShareProject(String shareToken) {
    log.info("(viewShareProject)shareToken: {}", shareToken);

    Claims claims = authTokenService.getClaimsFromShareToken(shareToken);

    String email = claims.get("email", String.class);
    String role = claims.get("role", String.class);
    String projectId = claims.get("projectId", String.class);

    if (authUserService.existsByEmail(email)) {
      var userId = authUserService.getUserId(email);
      if(!projectUserService.existsByUserIdAndProjectId(userId, projectId)){
        var projectUser = projectUserService.createProjectUser(userId, projectId, role);
      }
    }

    //call login page and return token share
  }

  @Override
  public List<AuthUserResponse> getAllUserByProject(String userId, String projectId) {
    log.info("(getAllUserByProject)user: {}, project: {}", userId, projectId);
    validateProjectId(projectId);

    return authUserService.getAllUserByProject(projectId);
  }

  @Override
  @Transactional
  public void deleteUser(String userId, String projectId, String memberId) {
    log.info("(deleteUser)projectId: {}, memberId: {}", projectId, memberId);
    AuthUser userMember = authUserService.findById(memberId);
    AuthUser userAdmin = authUserService.findById(memberId);
    Project project = projectService.getProjectById(projectId);

    projectUserService.deleteByUserIdAndProjectId(memberId, projectId);

    var subject = "Notice from the Todo List administrator in the project: " + project.getTitle();
    var param = new HashMap<String, Object>();

    String fullName = setFullName(userAdmin);
    if (fullName == null || fullName.isEmpty()) {
      fullName = "Admin";
    }
    param.put("email", userMember.getEmail());
    param.put("title", fullName+" kicked you out of the project: " + project.getTitle());
    param.put("subtitle", "If you want to join with our, contact me!");
    emailHelper.send(subject, userMember.getEmail(), "email-kick-user-in-project-template", param);

    var notification = new ActivityLog();
    notification.setAction(KICK_USER);
    notification.setUserId(userId);
    activityLogService.create(notification);
    taskAssigneesService.updateTaskAssigneesByUserIdAndProjectId(userId, memberId, projectId);
  }

  private void validateProjectId(String projectId) {
    log.info("(validateProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      log.error("(validateProjectId)project: {} not found", projectId);
      throw new ProjectNotFoundException();
    }
  }

  private String generateAcceptEmailKey(String email, String projectId, String role) {
    return Base64.getEncoder()
        .encodeToString((email + projectId + role + System.currentTimeMillis()).getBytes());
  }

  private String encodeEmail(String email) {
    return Base64.getEncoder().encodeToString((email).getBytes());
  }

  private String decryptionEmailEncode(String emailEncode) {
    byte[] email = Base64.getDecoder().decode(emailEncode);
    return new String(email);
  }

  private String setFullName(AuthUser user) {
    StringBuilder fullName = new StringBuilder();
    fullName.append(capitalizeName(user.getFirstName())).append(" ")
        .append(capitalizeName(user.getMiddleName())).append(" ")
        .append(capitalizeName(user.getLastName()));

    return fullName.toString().trim();
  }

  private String capitalizeName(String name) {
    if (name == null || name.isEmpty()) {
      return "";
    }
    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
  }

//  private
}
