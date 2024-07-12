package org.ghtk.todo_list.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProject(String userId) {
        log.info("(getAllProjectByUser)userId: {}", userId);
        //check user exist

        //
        List<Project> projectList = projectRepository.getAllProject(userId);
        return projectList;
    }

    @Override
    public Project getProject(String userId, String projectId) {
        log.info("(getProjectByUser)user: {}", userId);
        //check user exist

        //

        if (!projectRepository.existsById(projectId)) {
            log.error("(getProjectByUser)user: {} not found ", projectId);
            throw new ProjectNotFoundException();
        }

        Project project = projectRepository.getProject(userId, projectId);
        return project;
    }

    @Override
    public boolean existById(String id) {
        log.info("(existById)id: {}", id);
        return projectRepository.existsById(id);
    }
}
