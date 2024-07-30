package org.ghtk.todo_list.facade.imp;

import static org.ghtk.todo_list.constant.ActivityLogConstant.ProjectUserAction.*;
import static org.ghtk.todo_list.constant.CacheConstant.INVITE_KEY;
import static org.ghtk.todo_list.constant.CacheConstant.MAIL_TTL_MINUTES;
import static org.ghtk.todo_list.constant.CacheConstant.SHARE_KEY;

import io.jsonwebtoken.Claims;
import java.sql.Time;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.RoleProjectUser;
import org.ghtk.todo_list.constant.URL;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.EmailInvalidWithToken;
import org.ghtk.todo_list.exception.EmailInviteStillValidException;
import org.ghtk.todo_list.exception.EmailNotInviteException;
import org.ghtk.todo_list.exception.EmailShareStillValidException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.ProjectUserExistedException;
import org.ghtk.todo_list.exception.RoleProjectUserNotFound;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.model.request.RedisInviteUserRequest;
import org.ghtk.todo_list.dto.response.UserResponse;
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

    String invitedUserId = authUserService.getUserId(email);
    if(projectUserService.existsByUserIdAndProjectId(invitedUserId, projectId)){
      log.error("(inviteUser)user: {} already in project: {}", userId, projectId);
      throw new ProjectUserExistedException();
    }

    var redisInviteUser = redisCacheService.get(INVITE_KEY + projectId, email);
    if (redisInviteUser.isPresent()) {
      log.error("(inviteUser)email: {} already invited", email);
      throw new EmailInviteStillValidException(email);
    }

    var subject = "Admin has invited you to join their project";
    var param = new HashMap<String, Object>();
    String emailEncode = encodeEmail(email);

    param.put("email", email);
    param.put("projectId", projectId);
    param.put("domain", URL.DOMAIN);
    param.put("emailEncode", emailEncode);
    emailHelper.send(subject, email, "email-invite-to-project-template", param);

    RedisInviteUserRequest redisInviteUserRequest = new RedisInviteUserRequest();
    redisInviteUserRequest.setProjectId(projectId);
    redisInviteUserRequest.setRole(role);

    redisCacheService.save(INVITE_KEY + projectId, email, redisInviteUserRequest);

    var notification = new ActivityLog();
    notification.setAction(INVITE_USER);
    notification.setUserId(userId);
    activityLogService.create(notification);
  }

  @Override
  public String accept(String userId, String emailEncode, String projectId) {
    log.info("(accept)user: {}, email: {}, project: {}", userId, emailEncode, projectId);

    String email = decryptionEmailEncode(emailEncode);

    if(!email.equals(authUserService.findById(userId).getEmail())){
      log.error("(accept)email: {} invalid with token", email);
      throw new EmailInvalidWithToken(email);
    }

    var redisRequest = redisCacheService.get(INVITE_KEY + projectId, email);

    if (redisRequest.isEmpty()) {
      log.error("(accept)email: {} isn't invited", email);
      throw new EmailNotInviteException();
    }

    validateProjectId(projectId);

    RedisInviteUserRequest redisInviteUserRequest = (RedisInviteUserRequest) redisRequest.get();
    String role = redisInviteUserRequest.getRole();

    if (!RoleProjectUser.isValid(role)) {
      log.error("(accept)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    ProjectUser projectUser = projectUserService.createProjectUser(userId, projectId, role);
    String acceptEmailKey = generateAcceptEmailKey(email, projectId, role);
    redisCacheService.delete(INVITE_KEY + projectId, email);

    var notification = new ActivityLog();
    notification.setAction(ACCEPT_INVITE);
    notification.setUserId(authUserService.findByEmail(email).getId());
    activityLogService.create(notification);

    //call project page
    return null;
  }

  @Override
  public void shareProject(String userId, String projectId, String email, String role, Time expireTime) {
    log.info("(shareProject)user: {}, project: {}, email: {}, role: {}, expireDate: {}", userId, projectId, email, role, expireTime);

    String sharedUserId = authUserService.getUserId(email);
    if(projectUserService.existsByUserIdAndProjectId(sharedUserId, projectId)){
      log.error("(shareProject)user: {} already in project: {}", userId, projectId);
      throw new ProjectUserExistedException();
    }

    validateProjectId(projectId);

    if(!RoleProjectUser.isValid(role)){
      log.error("(shareProject)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    //Bo sung check quyen nguoi moi

    var redisShareUser = redisCacheService.get(SHARE_KEY + projectId + email);
    if (redisShareUser.isPresent()) {
      log.error("(shareProject)email: {} already shared", email);
      throw new EmailShareStillValidException(email);
    }

    var subject = "Admin shared you to their project in Todo List";
    var param = new HashMap<String, Object>();

    int hours = expireTime.getHours();
    int minutes = expireTime.getMinutes();
    int second = expireTime.getSeconds();

    String shareToken = authTokenService.generateShareToken(email, role, projectId, (long)(hours * 3600 + minutes * 60 + second) * 1000);

    param.put("projectId", projectId);
    param.put("email", email);
    param.put("domain", URL.DOMAIN);
    param.put("shareToken", shareToken);
    emailHelper.send(subject, email, "email-share-project-template", param);

    redisCacheService.save(SHARE_KEY + projectId + email, email, MAIL_TTL_MINUTES, TimeUnit.MINUTES);

    var notification = new ActivityLog();
    notification.setAction(SHARE_KEY);
    notification.setUserId(userId);
    activityLogService.create(notification);
  }

  @Override
  public String viewShareProject(String userId, String shareToken) {
    log.info("(viewShareProject)shareToken: {}", shareToken);

    Claims claims = authTokenService.getClaimsFromShareToken(shareToken);

    String email = claims.get("email", String.class);
    String role = claims.get("role", String.class);
    String projectId = claims.get("projectId", String.class);

    if(!email.equals(authUserService.findById(userId).getEmail())){
      log.error("(viewShareProject)email: {} invalid with token", email);
      throw new EmailInvalidWithToken(email);
    }

    validateProjectId(projectId);

    var projectUser = projectUserService.createProjectUser(userId, projectId, role);

    //call login page and return token share
    return null;
  }

  @Override
  public List<UserResponse> getAllUserByProject(String userId, String projectId) {
    log.info("(getAllUserByProject)user: {}, project: {}", userId, projectId);
    validateProjectId(projectId);

    List<UserResponse> userResponseList = authUserService.getAllUserByProject(projectId);
    for(UserResponse userResponse : userResponseList){
      userResponse.setRole(projectUserService.getRoleProjectUser(userResponse.getId(), projectId));
    }
    return userResponseList;
  }

  @Override
  @Transactional
  public String updateRoleProjectUser(String projectId, String memberId, String role) {
    log.info("(updateRoleProjectUser)projectId: {}, memberId: {}, role: {}", projectId, memberId, role);
    validateProjectId(projectId);

    if(!RoleProjectUser.isValid(role)){
      log.error("(updateRoleProjectUser)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    if(!authUserService.existById(memberId)){
      log.error("(updateRoleProjectUser)memberId: {} not found", memberId);
      throw new UserNotFoundException();
    }

    return projectUserService.updateRoleProjectUser(projectId, memberId, role);
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
}
