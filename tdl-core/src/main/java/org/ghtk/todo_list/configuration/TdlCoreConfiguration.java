package org.ghtk.todo_list.configuration;

import org.ghtk.todo_list.mapper.BoardMapper;
import org.ghtk.todo_list.mapper.ProjectMapper;
import org.ghtk.todo_list.mapper.ProjectUserMapper;
import org.ghtk.todo_list.repository.BoardRepository;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.ghtk.todo_list.service.BoardService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.impl.BoardServiceImpl;
import org.ghtk.todo_list.service.impl.ProjectServiceImpl;
import org.ghtk.todo_list.service.impl.ProjectUserServiceImpl;
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
  public ProjectService projectService(ProjectRepository projectRepository,
      ProjectMapper projectMapper, ProjectUserService projectUserService, BoardService boardService) {
    return new ProjectServiceImpl(projectRepository, projectMapper, projectUserService, boardService);
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
}
