package org.ghtk.todo_list.service.impl;

import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TaskServiceImp implements TaskService {
    @Autowired
    private TaskRepository taskRepo;


    @Override
    public List<Task> getAllTasks(String projectId) {
        if(!taskRepo.existsByProjectId(projectId)) {
            throw new ProjectNotFoundException();
        }
        return taskRepo.getAllTask(projectId);
    }
}
