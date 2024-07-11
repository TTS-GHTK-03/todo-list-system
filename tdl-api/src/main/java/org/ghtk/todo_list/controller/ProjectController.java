package org.ghtk.todo_list.controller;

import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.service.impl.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectServiceImpl projectService;

    @GetMapping()
    public BaseResponse getAllProjectByUser(){
        log.info("(getAllProjectByUser)");
        String userId = "5df2fe3e-c3d7-4236-8283-e62656a5fd24";
        return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), projectService.getAllProjectByUser(userId));
    }

    @GetMapping("/{project_id}")
    public BaseResponse getProjectByUser(@PathVariable(name = "project_id") String projectId){
        log.info("(getProjectByUser)projectId: {}", projectId);
        String userId = "5df2fe3e-c3d7-4236-8283-e62656a5fd24";
        return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), projectService.getProjectByUser(userId, projectId));
    }
}
