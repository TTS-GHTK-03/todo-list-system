package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjectByUser(String userId);
    Project getProjectByUser(String userId, String projectId);
}
