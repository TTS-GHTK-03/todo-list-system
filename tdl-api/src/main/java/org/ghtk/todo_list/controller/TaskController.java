package org.ghtk.todo_list.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.service.impl.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/projects")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController {
    @Autowired
    private TaskServiceImp taskServiceImp;

    @GetMapping("/{project_id}/tasks")
    public Map<String, Object> getTasksByProjectId(@PathVariable("project_id") String projectId) {
        List<Task> tasks = taskServiceImp.getAllTasks(projectId);

        Map<String, Object> response = new HashMap<>();
        response.put("code", "200");
        response.put("timestamp", Instant.now().toEpochMilli());
        response.put("data", tasks);

        return response;

        //return taskServiceImp.getAllTasks(projectId);
    }

}
