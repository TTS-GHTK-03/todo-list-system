package org.ghtk.todo_list.facade.imp;

import static org.ghtk.todo_list.constant.ActivityLogConstant.ProjectUserAction.*;
import static org.ghtk.todo_list.constant.CacheConstant.INVITE_KEY;
import static org.ghtk.todo_list.constant.CacheConstant.MAIL_TTL_MINUTES;
import static org.ghtk.todo_list.constant.CacheConstant.SHARE_KEY;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.RoleProjectUser;
import org.ghtk.todo_list.constant.URL;
import org.ghtk.todo_list.constant.UserActionStatus;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.EmailInviteStillValidException;
import org.ghtk.todo_list.exception.EmailNotInviteException;
import org.ghtk.todo_list.exception.EmailShareStillValidException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.ProjectUserExistedException;
import org.ghtk.todo_list.exception.RoleProjectUserNotFound;
import org.ghtk.todo_list.exception.RoleSharedUserExceededException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.model.request.RedisInviteUserRequest;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.model.response.AcceptInviteResponse;
import org.ghtk.todo_list.model.response.AcceptShareResponse;
import org.ghtk.todo_list.service.ActivityLogService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.service.ShareTokenService;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class ProjectUserFacadeServiceImpl implements ProjectUserFacadeService {

  private final ProjectUserService projectUserService;
  private final ProjectService projectService;
  private final AuthUserService authUserService;
  private final ShareTokenService shareTokenService;
  private final RedisCacheService redisCacheService;
  private final TaskAssigneesService taskAssigneesService;
  private final EmailHelper emailHelper;
  private final ActivityLogService activityLogService;

  @Override
  public void inviteUser(String userId, String projectId, String invitedUserEmail, String role,
      Boolean reSend) {
    log.info("(inviteUser)user: {}, project: {}", userId, projectId);

    validateProjectId(projectId);

    if (!RoleProjectUser.isValid(role)) {
      log.error("(inviteUser)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    String invitedUserId = authUserService.getUserId(invitedUserEmail);
    if (invitedUserId != null && projectUserService.existsByUserIdAndProjectId(invitedUserId,
        projectId)) {
      log.error("(inviteUser)user: {} already in project: {}", userId, projectId);
      throw new ProjectUserExistedException();
    }

    if (reSend == null || !reSend) {
      log.info("(inviteUser)reSend: {}", reSend);
      var redisInviteUser = redisCacheService.get(INVITE_KEY + projectId, invitedUserEmail);
      if (redisInviteUser.isPresent()) {
        log.error("(inviteUser)email: {} already invited", invitedUserEmail);
        throw new EmailInviteStillValidException(invitedUserEmail);
      }
    }

    var subject = "Admin has invited you to join their project";
    var param = new HashMap<String, Object>();
    String emailEncode = encodeEmail(invitedUserEmail);

    param.put("email", invitedUserEmail);
    param.put("projectId", projectId);
    param.put("domain", URL.DOMAIN);
    param.put("emailEncode", emailEncode);
    emailHelper.send(subject, invitedUserEmail, "email-invite-to-project-template", param);

    RedisInviteUserRequest redisInviteUserRequest = new RedisInviteUserRequest();
    redisInviteUserRequest.setProjectId(projectId);
    redisInviteUserRequest.setRole(role);

    redisCacheService.save(INVITE_KEY + projectId, invitedUserEmail, redisInviteUserRequest);

    var notification = new ActivityLog();
    notification.setAction(INVITE_USER);
    notification.setUserId(userId);
    activityLogService.create(notification);
  }

  @Override
  public AcceptInviteResponse accept(String userId, String emailEncode, String projectId) {
    log.info("(accept)user: {}, email: {}, project: {}", userId, emailEncode, projectId);

    String email = decryptionEmailEncode(emailEncode);

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

    AcceptInviteResponse acceptInviteResponse = new AcceptInviteResponse();
    acceptInviteResponse.setEmail(email);
    acceptInviteResponse.setProjectId(projectId);
    if (userId != null && !userId.isEmpty() && !userId.isBlank()) {
      log.info("(accept)user: log in");
      if (email.equals(authUserService.findById(userId).getEmail())) {
        log.info("(accept)email: {} valid with token", email);
        projectUserService.createProjectUser(userId, projectId, role);
        redisCacheService.delete(INVITE_KEY + projectId, email);
        acceptInviteResponse.setStatus(UserActionStatus.LOGGED_ACCEPTED.toString());

        var notification = new ActivityLog();
        notification.setAction(ACCEPT_INVITE);
        notification.setUserId(authUserService.findByEmail(email).getId());
        activityLogService.create(notification);
      } else {
        log.warn("(accept)email: {} invalid with token", email);
        checkEmailAccept(userId, email, projectId, role, acceptInviteResponse);
      }
    } else {
      log.info("(accept)user: not log in");
      checkEmailAccept(null, email, projectId, role,
          acceptInviteResponse);
    }

    return acceptInviteResponse;
  }

  @Override
  public void shareProject(String userId, String projectId, String sharedUserEmail, String role,
      long expireTime) {
    log.info("(shareProject)user: {}, project: {}, sharedUserEmail: {}, role: {}, expireDate: {}",
        userId, projectId, sharedUserEmail, role, expireTime);

    validateProjectId(projectId);

    if (!RoleProjectUser.isValid(role)) {
      log.error("(shareProject)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    String sharerRole = projectUserService.getRoleProjectUser(userId, projectId);
    if ((sharerRole.equals(RoleProjectUser.ADMIN.toString()) && role.equals(
        RoleProjectUser.ADMIN.toString())) || (
        sharerRole.equals(RoleProjectUser.EDIT.toString()) && !role.equals(
            RoleProjectUser.VIEWER.toString()))) {
      log.error("(shareProject)role: {} exceeded sharerRole: {}", role, sharerRole);
      throw new RoleSharedUserExceededException();
    }

    String sharedUserId = authUserService.getUserId(sharedUserEmail);
    if (sharedUserId != null && projectUserService.existsByUserIdAndProjectId(sharedUserId,
        projectId)) {
      log.error("(shareProject)user: {} already in project: {}", userId, projectId);
      throw new ProjectUserExistedException();
    }

    ProjectUser projectUser = new ProjectUser();
    projectUser.setProjectId(projectId);
    projectUser.setRole(role);
    if (authUserService.existsByEmail(sharedUserEmail)) {
      AuthUser authUser = authUserService.findByEmail(sharedUserEmail);
      log.info("(shareProject)email: {} has account", sharedUserEmail);
      if(!projectUserService.existsByUserIdAndProjectId(authUser.getId(), projectId)) {
        log.info("(shareProject)email: {} not in project: {}", sharedUserEmail, projectId);
        projectUser.setUserId(authUser.getId());
        projectUser.setExpireDateShare(LocalDateTime.now().plusDays(expireTime));
        projectUserService.createProjectUserShare(projectUser);
      } else {
        log.error("(shareProject)email: {} already in project: {}", sharedUserEmail, projectId);
        throw new ProjectUserExistedException();
      }
    } else {
      log.warn("(shareProject)email: {} don't have account", sharedUserEmail);
      AuthUser authUser = authUserService.createTemporaryUser(sharedUserEmail);
      projectUser.setUserId(authUser.getId());
      projectUser.setExpireDateShare(LocalDateTime.now().plusDays(expireTime));
      projectUserService.createProjectUserShare(projectUser);
    }

    var redisShareUser = redisCacheService.get(SHARE_KEY + projectId + sharedUserEmail);
    if (redisShareUser.isPresent()) {
      log.error("(shareProject)email: {} already shared", sharedUserEmail);
      throw new EmailShareStillValidException(sharedUserEmail);
    }

    var subject = "Admin shared you to their project in Todo List";
    var param = new HashMap<String, Object>();

    String shareToken = shareTokenService.generateShareToken(sharedUserEmail, role, projectId,
        expireTime * 24 * 60 * 60 * 1000);

    param.put("projectId", projectId);
    param.put("email", sharedUserEmail);
    param.put("domain", URL.DOMAIN);
    param.put("shareToken", shareToken);
    emailHelper.send(subject, sharedUserEmail, "email-share-project-template", param);

    redisCacheService.save(SHARE_KEY + projectId + sharedUserEmail, sharedUserEmail,
        MAIL_TTL_MINUTES, TimeUnit.MINUTES);

    var notification = new ActivityLog();
    notification.setAction(SHARE_KEY);
    notification.setUserId(userId);
    activityLogService.create(notification);
  }

  @Override
  public AcceptShareResponse viewShareProject(String userId, String shareToken) {
    log.info("(viewShareProject)shareToken: {}", shareToken);

    Claims claims;

    try {
      claims = shareTokenService.getClaimsFromShareToken(shareToken);
    } catch (ExpiredJwtException ex) {
      log.error("(viewShareProject)shareToken: {} expired", shareToken);
      return AcceptShareResponse.builder()
          .email(null)
          .shareToken(shareToken)
          .status("EXPIRED")
          .projectId(null)
          .build();
    } catch (Exception e) {
      log.error("(viewShareProject)shareToken: {} invalid", shareToken);
      return AcceptShareResponse.builder()
          .email(null)
          .shareToken(shareToken)
          .status("INVALID")
          .projectId(null)
          .build();
    }

    String email = claims.get("email", String.class);
    String role = claims.get("role", String.class);
    String projectId = claims.get("projectId", String.class);
    long expireTime = claims.get("expireTime", Long.class);
    log.info("(viewShareProject)expireTime: {}", expireTime);
    validateProjectId(projectId);

    if (!RoleProjectUser.isValid(role)) {
      log.error("(viewShareProject)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    AcceptShareResponse acceptShareResponse = new AcceptShareResponse();
    acceptShareResponse.setEmail(email);
    acceptShareResponse.setShareToken(shareToken);
    acceptShareResponse.setProjectId(projectId);
    if (userId != null && !userId.isEmpty() && !userId.isBlank()) {
      log.info("(viewShareProject)user: log in");
      if (email.equals(authUserService.findById(userId).getEmail())) {
        log.info("(viewShareProject)email: {} valid with token", email);
        acceptShareResponse.setStatus(UserActionStatus.LOGGED_ACCEPTED.toString());
      } else {
        log.warn("(viewShareProject)email: {} invalid with token", email);
        checkEmailAcceptShare(email, acceptShareResponse);
      }
    } else {
      log.info("(viewShareProject)user: not log in");
      checkEmailAcceptShare(email, acceptShareResponse);
    }
    return acceptShareResponse;
  }

  @Override
  public List<UserResponse> getAllUserByProject(String userId, String projectId) {
    log.info("(getAllUserByProject)user: {}, project: {}", userId, projectId);
    validateProjectId(projectId);

    List<UserResponse> userResponseList = authUserService.getAllUserByProject(projectId);
    for (UserResponse userResponse : userResponseList) {
      userResponse.setRole(projectUserService.getRoleProjectUser(userResponse.getId(), projectId));
    }
    return userResponseList;
  }

  @Override
  @Transactional
  public String updateRoleProjectUser(String projectId, String memberId, String role) {
    log.info("(updateRoleProjectUser)projectId: {}, memberId: {}, role: {}", projectId, memberId,
        role);
    validateProjectId(projectId);

    if (!RoleProjectUser.isValid(role)) {
      log.error("(updateRoleProjectUser)role: {} not found", role);
      throw new RoleProjectUserNotFound();
    }

    if (!authUserService.existById(memberId)) {
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
    param.put("title", fullName + " kicked you out of the project: " + project.getTitle());
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

  private void checkEmailAccept(String userId, String email, String projectId, String role,
      AcceptInviteResponse acceptInviteResponse) {
    log.info("(checkEmailAccept)email: {}", email);
    if (authUserService.existsByEmail(email)) {
      log.info("(checkEmailAccept)email: {} existed", email);
      projectUserService.createProjectUser(userId, projectId, role);
      redisCacheService.delete(INVITE_KEY + projectId, email);
      acceptInviteResponse.setStatus(UserActionStatus.ACCEPTED.toString());

      var notification = new ActivityLog();
      notification.setAction(ACCEPT_INVITE);
      notification.setUserId(authUserService.findByEmail(email).getId());
      activityLogService.create(notification);
    } else {
      log.warn("(checkEmailAccept)email: {} not existed", email);
      AuthUser authUser = authUserService.createTemporaryUser(email);
      projectUserService.createProjectUser(authUser.getId(), projectId, role);
      acceptInviteResponse.setStatus(UserActionStatus.UNREGISTERED.toString());
    }
  }

  private void checkEmailAcceptShare(String email,AcceptShareResponse acceptShareResponse) {
    log.info("(checkEmailAcceptShare)email: {}", email);
    if (authUserService.existsByEmail(email)) {
      log.info("(checkEmailAcceptShare)email: {} existed", email);
      acceptShareResponse.setStatus(UserActionStatus.ACCEPTED.toString());

    } else {
      log.warn("(checkEmailAcceptShare)email: {} not existed", email);
      acceptShareResponse.setStatus(UserActionStatus.UNREGISTERED.toString());
    }
  }
}
