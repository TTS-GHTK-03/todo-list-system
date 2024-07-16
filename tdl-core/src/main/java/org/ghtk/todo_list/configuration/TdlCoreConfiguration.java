package org.ghtk.todo_list.configuration;

import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.facade.ProjectFacadeService;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.facade.imp.ProjectFacadeServiceImpl;
import org.ghtk.todo_list.facade.SprintFacadeService;
import org.ghtk.todo_list.facade.imp.SprintFacadeServiceImpl;
import org.ghtk.todo_list.facade.imp.ProjectUserFacadeServiceImpl;
import org.ghtk.todo_list.mapper.BoardMapper;
import org.ghtk.todo_list.mapper.ProjectInformationResponseMapper;
import org.ghtk.todo_list.mapper.ProjectMapper;
import org.ghtk.todo_list.mapper.ProjectUserMapper;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.repository.BoardRepository;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.ghtk.todo_list.repository.TaskAssigneesRepository;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.repository.SprintRepository;
import org.ghtk.todo_list.service.BoardService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.SprintService;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.service.impl.BoardServiceImpl;
import org.ghtk.todo_list.service.impl.ProjectServiceImpl;
import org.ghtk.todo_list.service.impl.ProjectUserServiceImpl;
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
  public ProjectFacadeService projectFacadeService(ProjectService projectService, ProjectUserService projectUserService, BoardService boardService,
      AuthUserService authUserService, ProjectInformationResponseMapper projectInformationResponseMapper){
    return new ProjectFacadeServiceImpl(projectService, projectUserService, boardService, authUserService, projectInformationResponseMapper);
  }

  @Bean
  public ProjectUserFacadeService projectUserFacadeService(ProjectUserService projectUserService, ProjectService projectService,
      AuthUserService authUserService, RedisCacheService redisCacheService, EmailHelper emailHelper) {
    return new ProjectUserFacadeServiceImpl(projectUserService, projectService, authUserService, redisCacheService, emailHelper);
  }

  @Bean
  public ProjectService projectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
    return new ProjectServiceImpl(projectRepository, projectMapper);
  }
  @Bean
  public SprintFacadeService sprintFacadeService(
      SprintService sprintService, ProjectService projectService, SprintMapper sprintMapper
  ) {
    return new SprintFacadeServiceImpl(sprintService, sprintMapper, projectService);
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
  public TaskAssigneesService taskAssigneesService(TaskAssigneesRepository taskAssigneesRepository) {
    return new TaskAssigneesServiceImpl(taskAssigneesRepository);
  }
}
