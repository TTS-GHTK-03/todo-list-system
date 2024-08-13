package org.ghtk.todo_list.configuration;

import org.ghtk.todo_list.base_authrization.BaseAuthorization;
import org.ghtk.todo_list.facade.ActivityLogFacadeService;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.facade.ProjectFacadeService;
import org.ghtk.todo_list.facade.TypeFacadeService;
import org.ghtk.todo_list.facade.imp.ActivityLogFacadeServiceImpl;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.facade.imp.ProjectFacadeServiceImpl;
import org.ghtk.todo_list.facade.SprintFacadeService;
import org.ghtk.todo_list.facade.imp.SprintFacadeServiceImpl;
import org.ghtk.todo_list.facade.imp.ProjectUserFacadeServiceImpl;
import org.ghtk.todo_list.facade.imp.TypeFacadeServiceImpl;
import org.ghtk.todo_list.mapper.ActivityLogMapper;
import org.ghtk.todo_list.mapper.BoardMapper;
import org.ghtk.todo_list.mapper.ProjectInformationResponseMapper;
import org.ghtk.todo_list.mapper.ProjectMapper;
import org.ghtk.todo_list.mapper.ProjectUserMapper;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.mapper.TypeMapper;
import org.ghtk.todo_list.repository.ActivityLogRepository;
import org.ghtk.todo_list.repository.BoardRepository;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.ghtk.todo_list.repository.SprintProgressRepository;
import org.ghtk.todo_list.repository.TaskAssigneesRepository;
import org.ghtk.todo_list.service.ActivityLogService;
import org.ghtk.todo_list.service.AuthTokenService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.repository.SprintRepository;
import org.ghtk.todo_list.service.BoardService;
import org.ghtk.todo_list.service.CommentService;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.ghtk.todo_list.service.LabelService;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.ghtk.todo_list.service.LabelService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.ShareTokenService;
import org.ghtk.todo_list.service.SprintProgressService;
import org.ghtk.todo_list.service.SprintService;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.service.TaskService;
import org.ghtk.todo_list.service.TypeService;
import org.ghtk.todo_list.service.UserService;
import org.ghtk.todo_list.service.impl.ActivityLogServiceImpl;
import org.ghtk.todo_list.service.impl.BoardServiceImpl;
import org.ghtk.todo_list.service.impl.ProjectServiceImpl;
import org.ghtk.todo_list.service.impl.ProjectUserServiceImpl;
import org.ghtk.todo_list.service.impl.SprintProgressServiceImpl;
import org.ghtk.todo_list.service.impl.SprintServiceImpl;
import org.ghtk.todo_list.service.impl.TaskAssigneesServiceImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"org.ghtk.todo_list.repository"})
@ComponentScan(basePackages = {"org.ghtk.todo_list.repository"})
@EnableJpaAuditing
@EntityScan(basePackages = "org.ghtk.todo_list.entity")
public class TdlCoreConfiguration {

  @Bean
  public ProjectFacadeService projectFacadeService(ProjectService projectService,
      ProjectUserService projectUserService, BoardService boardService,
      AuthUserService authUserService,
      ProjectInformationResponseMapper projectInformationResponseMapper,
      ProjectMapper projectMapper,
      TypeService typeService, TypeMapper typeMapper, LabelService labelService,
      LabelAttachedService labelAttachedService,
      TaskService taskService, ActivityLogService activityLogService,
      TaskAssigneesService taskAssigneesService,
      CommentService commentService, SprintService sprintService,
      SprintProgressService sprintProgressService,
      UserService userService) {
    return new ProjectFacadeServiceImpl(projectService, projectUserService, boardService,
        authUserService, projectInformationResponseMapper, projectMapper, typeService, typeMapper,
        labelService, labelAttachedService, taskService, activityLogService, taskAssigneesService,
        commentService, sprintService, sprintProgressService, userService);
  }

  @Bean
  public ProjectUserFacadeService projectUserFacadeService(ProjectUserService projectUserService,
      ProjectService projectService,
      AuthUserService authUserService, ShareTokenService shareTokenService,
      RedisCacheService redisCacheService, TaskAssigneesService taskAssigneesService,
      EmailHelper emailHelper, ActivityLogService activityLogService) {
    return new ProjectUserFacadeServiceImpl(projectUserService, projectService, authUserService,
        shareTokenService,
        redisCacheService, taskAssigneesService, emailHelper, activityLogService);
  }

  @Bean
  public TypeFacadeService typeFacadeService(ProjectService projectService,
      ProjectUserService projectUserService, TypeService typeService, TaskService taskService,
      LabelService labelService, LabelAttachedService labelAttachedService,
      TypeMapper typeMapper) {
    return new TypeFacadeServiceImpl(projectService, projectUserService, typeService, taskService,
        labelService, labelAttachedService,
        typeMapper);
  }

  @Bean
  public ProjectService projectService(ProjectRepository projectRepository,
      ProjectMapper projectMapper) {
    return new ProjectServiceImpl(projectRepository, projectMapper);
  }

  @Bean
  public SprintFacadeService sprintFacadeService(
      SprintService sprintService,
      ProjectService projectService,
      SprintMapper sprintMapper,
      SprintProgressService sprintProgressService,
      TaskService taskService,
      CommentService commentService,
      LabelAttachedService labelAttachedService,
      ActivityLogService activityLogService,
      TaskAssigneesService taskAssigneesService
  ) {
    return new SprintFacadeServiceImpl(sprintService,
        sprintMapper,
        projectService,
        sprintProgressService,
        taskService,
        commentService,
        labelAttachedService,
        activityLogService,
        taskAssigneesService
    );
  }

  @Bean
  public ProjectUserService projectUserService(ProjectUserRepository projectUserRepository,
      ProjectUserMapper projectUserMapper) {
    return new ProjectUserServiceImpl(projectUserRepository, projectUserMapper);
  }

  @Bean
  public BoardService boardService(BoardRepository boardRepository, BoardMapper boardMapper) {
    return new BoardServiceImpl(boardRepository, boardMapper);
  }

  @Bean
  public SprintService sprintService(SprintRepository sprintRepository) {
    return new SprintServiceImpl(sprintRepository);
  }

  @Bean
  public TaskAssigneesService taskAssigneesService(
      TaskAssigneesRepository taskAssigneesRepository) {
    return new TaskAssigneesServiceImpl(taskAssigneesRepository);
  }

  @Bean
  public ActivityLogService activityLogService(ActivityLogRepository activityLogRepository) {
    return new ActivityLogServiceImpl(activityLogRepository);
  }

  @Bean
  public ActivityLogFacadeService activityLogFacadeService(ActivityLogService activityLogService,
      AuthUserService authUserService,
      ProjectService projectService, SprintService sprintService, TaskService taskService,
      ActivityLogMapper activityLogMapper) {
    return new ActivityLogFacadeServiceImpl(activityLogService, authUserService, projectService,
        sprintService, taskService, activityLogMapper);
  }

  @Bean
  public SprintProgressService sprintProgressService(
      SprintProgressRepository sprintProgressRepository) {
    return new SprintProgressServiceImpl(sprintProgressRepository);
  }
}