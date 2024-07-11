package org.ghtk.todo_list.service.impl;

import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjectByUser(String userId) {
        log.info("(getAllProjectByUser)userId: {}", userId);
        //check user exist

        //
        List<Project> projectList = projectRepository.getAllProjectByUser(userId);
        return projectList;
    }

    @Override
    public Project getProjectByUser(String userId, String projectId) {
        log.info("(getProjectByUser)user: {}", userId);
        //check user exist

        //
        //check project exist
        if (!projectRepository.existsById(projectId)) {
            log.error("(getProjectByUser)user: {} not found ", projectId);
            throw new ProjectNotFoundException();
        }
        //
        Project project = projectRepository.getProjectByUser(userId, projectId);
        return project;
    }
}
