package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.TaskFacadeService;
import org.ghtk.todo_list.model.request.CreateTaskRequest;
import org.ghtk.todo_list.model.request.UpdateDueDateTaskRequest;
import org.ghtk.todo_list.model.request.UpdatePointTaskRequest;
import org.ghtk.todo_list.model.request.UpdateTitleTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/projects")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController {

  private final TaskFacadeService taskFacadeService;

  @GetMapping("/{project_id}/tasks")
  public BaseResponse getTasksByProjectId(@PathVariable("project_id") String projectId, @RequestParam(value = "status", required = false) String status) {
    log.info("(getTasksByProjectId)");
    if (status == null) {
      log.info("(getTasksByProjectId)getAllTasks");
      return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
          taskFacadeService.getAllTaskByProjectId(getUserId(), projectId));
    } else {
      log.info("(getTasksByProjectId)getTasksByProjectIdAndStatus");
      return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
          taskFacadeService.getAllTaskByProjectIdAndStatus(getUserId(), projectId, status));
    }
  }

  @PostMapping ("/{project_id}/tasks")
  public BaseResponse createTask(@PathVariable("project_id") String projectId, @Valid @RequestBody
      CreateTaskRequest createTaskRequest) {
    log.info("(createTask)projectId: {}", projectId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
          taskFacadeService.createTask(getUserId(),projectId,createTaskRequest.getTitle()));
    }

  @GetMapping("/{project_id}/tasks/{task_id}")
  public BaseResponse getTaskByTaskId(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId) {
    log.info("(getTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.getTaskByTaskId(getUserId(), projectId, taskId));
  }

  @PatchMapping("/{project_id}/tasks/{task_id}")
  public BaseResponse updateStatusTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId, @Valid @RequestParam(value = "statusTask") String status) {
    log.info("(updateStatusTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateStatusTask(getUserId(), projectId, taskId, status));
  }

  @PatchMapping("/{project_id}/tasks/{task_id}/points")
  public BaseResponse updatePointTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId, @Valid @RequestBody UpdatePointTaskRequest updatePointTaskRequest) {
    log.info("(updatePointTask),projectId: {}, taskId: {}", projectId, taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updatePointTask(getUserId(), projectId, taskId, updatePointTaskRequest.getPoint()));
  }

  @PatchMapping("/{project_id}/sprints/{sprint_id}/tasks/{task_id}")
  public BaseResponse updateSprintTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId, @PathVariable("sprint_id") String sprintId) {
    log.info("(updateSprintTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateSprintTask(getUserId(), projectId, sprintId, taskId));
  }

  @PostMapping("/{project_id}/tasks/{task_id}/clone")
  public BaseResponse cloneTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId) {
    log.info("(cloneTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.cloneTask(getUserId(), projectId, taskId));
  }

  @PutMapping("/{project_id}/sprints/{sprint_id}/tasks/{task_id}/update-date")
  public BaseResponse updateStartDateDueDateTask(@PathVariable("project_id") String projectId,
      @PathVariable("sprint_id") String sprintId, @PathVariable("task_id") String taskId,
      @RequestBody @Valid UpdateDueDateTaskRequest updateDueDateTaskRequest) {
    log.info("(updateStartDateDueDateTask)project: {}, sprint: {}, task: {}", projectId, sprintId,
        taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateStartDateDueDateTask(getUserId(), projectId, sprintId, taskId,
            updateDueDateTaskRequest.getStatusTaskKey(),
            updateDueDateTaskRequest.getDueDate()));
  }

  @GetMapping("/{project_id}/sprints/{sprint_id}/tasks")
  public BaseResponse getAllBySprintId(@PathVariable("project_id") String projectId,
      @PathVariable("sprint_id") String sprintId) {
    log.info("(getAllTaskBySprintId)");
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.getAllBySprintId(projectId, sprintId));
  }

  @DeleteMapping ("/{project_id}/tasks/{task_id}")
  public BaseResponse deleteTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId) {
    log.info("(deleteTask)projectId: {}, taskId: {}", projectId, taskId);
    taskFacadeService.deleteTask(getUserId(), projectId, taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Successfull delete task!");
  }

  @PutMapping("/{project_id}/tasks/{task_id}")
  public BaseResponse updateTitleTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId, @Valid @RequestBody UpdateTitleTaskRequest request) {
    log.info("(updateTitleTask)project: {}, task: {}, title: {}", projectId, taskId, request.getTitle());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateTitleTask(getUserId(), projectId, taskId, request.getTitle()));
  }
  @GetMapping("/{project_id}/tasks/search")
  public BaseResponse searchTask(@PathVariable("project_id") String projectId,
      @RequestParam(required = false)  String search,
      @RequestParam(required = false)  String typeId,
      @RequestParam(required = false)  String labelId) {
    log.info("(searchTask)project: {}, search: {}", projectId, search);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.searchTask(search, typeId, labelId, getUserId(), projectId));
  }

  @GetMapping("/{project_id}/tasks/search-board")
  public BaseResponse searchTaskBoard(@PathVariable("project_id") String projectId,
      @RequestParam(required = false)  String search,
      @RequestParam(required = false)  String sprintId) {
    log.info("(searchTaskBoard)project: {}, search: {}, sprintId: {}", projectId, search, sprintId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.searchTaskBoard(search, sprintId, getUserId(), projectId));
  }

  @GetMapping("/tasks/search-filter")
  public BaseResponse searchTaskBoard(
      @RequestParam(required = false)  String projectId,
      @RequestParam(required = false)  String search,
      @RequestParam(required = false)  String typeId,
      @RequestParam(required = false)  String status,
      @RequestParam(required = false)  String assignee) {
    log.info("(searchTaskBoard)project: {}, search: {}, typeId: {}, status: {}, assignee: {}", projectId, search, typeId, status, assignee);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.searchTaskFilter(search, typeId, status, assignee, getUserId(), projectId));
  }
}
