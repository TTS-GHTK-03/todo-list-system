package org.ghtk.todo_list.service.impl;

import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjectByUser(String userId) {
        List<Project> projectList = projectRepository.getAllProjectByUser(userId);
        return projectList;
    }

    @Override
    public Project getProjectByUser(String userId, String projectId) {
        return null;
    }
}
