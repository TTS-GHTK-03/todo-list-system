package org.ghtk.todo_list.controller;

import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.service.impl.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectServiceImpl projectService;

    @GetMapping("")
    public BaseResponse getAllProjectByUser(@RequestHeader String token){
        // decoding token

        //
        // check user exist

        //

        List<Project> projectList = projectService.getAllProjectByUser("0");
        Date now = new Date();
        Timestamp timestamp = new Timestamp(now.getTime());
        BaseResponse baseResponse= new BaseResponse();
        baseResponse.setStatus(200);
        baseResponse.setTimestamp(timestamp.toString());
        baseResponse.setData(projectList);
        return baseResponse;
    }
}
