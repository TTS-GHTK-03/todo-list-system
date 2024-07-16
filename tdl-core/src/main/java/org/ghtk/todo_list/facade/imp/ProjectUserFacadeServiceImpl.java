package org.ghtk.todo_list.facade.imp;

import static org.ghtk.todo_list.constant.CacheConstant.INVITE_KEY;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.exception.EmailNotInviteExcpetion;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.model.request.RedisInviteUserRequest;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.RedisCacheService;

@Slf4j
@RequiredArgsConstructor
public class ProjectUserFacadeServiceImpl implements ProjectUserFacadeService {

  private final ProjectUserService projectUserService;
  private final ProjectService projectService;
  private final AuthUserService authUserService;
  private final RedisCacheService redisCacheService;
  private final EmailHelper emailHelper;

  @Override
  public void inviteUser(String userId, String projectId, String email, String role) {
    log.info("(inviteUser)user: {}, project: {}", userId, projectId);

    if(!authUserService.existById(userId)){
      log.error("(inviteUser)user: {} not found", userId);
      throw new UserNotFoundException();
    }

    if(!projectService.existById(projectId)){
      log.error("(inviteUser)project: {} not found", projectId);
      throw new ProjectNotFoundException();
    }

    var subject = "Admin invited you to join them in Todo List";
    var param = new HashMap<String, Object>();
    param.put("email", email);
    emailHelper.send(subject, email, "email-invite-to-project-template", param);

    RedisInviteUserRequest redisInviteUserRequest = new RedisInviteUserRequest();
    redisInviteUserRequest.setProjectId(projectId);
    redisInviteUserRequest.setRole(role);

    redisCacheService.save(INVITE_KEY, email, redisInviteUserRequest);
  }

  @Override
  public String accept(String email) {
    log.info("(accept)email: {}", email);

    var redisRequest = redisCacheService.get(INVITE_KEY, email);

    if(redisRequest.isEmpty()){
      log.error("(accept)redis invite user request: {} isn't invited", email);
      throw new EmailNotInviteExcpetion();
    }

    RedisInviteUserRequest redisInviteUserRequest = (RedisInviteUserRequest) redisRequest.get();
    String acceptEmailKey = generateAcceptEmailKey(email, redisInviteUserRequest.getProjectId(), redisInviteUserRequest.getRole());
    System.out.println(acceptEmailKey);
    return null;
  }

  private String generateAcceptEmailKey(String email, String projectId, String role){
    return Base64.getEncoder().encodeToString((email + projectId + role + System.currentTimeMillis()).getBytes());
  }
}
