package org.ghtk.todo_list.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.service.TaskService;
import org.ghtk.todo_list.service.impl.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/projects")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private TaskServiceImp taskServiceImp;

    @GetMapping("/{project_id}/tasks")
    public List<Task> getTasksByProjectId(@PathVariable("project_id") String projectId) {
        return taskServiceImp.getAllTasks(projectId);
    }

}
