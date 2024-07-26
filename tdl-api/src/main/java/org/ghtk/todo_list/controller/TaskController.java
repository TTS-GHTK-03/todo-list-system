package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.TaskFacadeService;
import org.ghtk.todo_list.model.request.CreateTaskRequest;
import org.ghtk.todo_list.model.request.UpdateDueDateTaskRequest;
import org.ghtk.todo_list.model.request.UpdatePointTaskRequest;
import org.ghtk.todo_list.model.request.UpdateTitleTaskRequest;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.model.response.UpdateDueDateTaskResponse;
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
@Tag(name = "Task")
public class TaskController {

  private final TaskFacadeService taskFacadeService;

  @GetMapping("/{project_id}/tasks")
  @Operation(summary = "Get all task by project id")
  public BaseResponse<List<TaskResponse>> getTasksByProjectId(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "status", description = "Status task", example = "TODO")
      @RequestParam(value = "status", required = false) String status) {
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
  @Operation(description = "Create task")
  public BaseResponse<TaskResponse> createTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Valid @RequestBody CreateTaskRequest createTaskRequest) {
    log.info("(createTask)projectId: {}", projectId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
          taskFacadeService.createTask(getUserId(),projectId,createTaskRequest.getTitle()));
    }

  @GetMapping("/{project_id}/tasks/{task_id}")
  @Operation(description = "Get task by task id")
  public BaseResponse<TaskResponse> getTaskByTaskId(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification task")
      @PathVariable("task_id") String taskId) {
    log.info("(getTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.getTaskByTaskId(getUserId(), projectId, taskId));
  }

  @PatchMapping("/{project_id}/tasks/{task_id}")
  @Operation(description = "Update status task")
  public BaseResponse<TaskResponse> updateStatusTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "statusTask", description = "Status task", example = "TODO")
      @Valid @RequestParam(value = "statusTask") String status) {
    log.info("(updateStatusTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateStatusTask(getUserId(), projectId, taskId, status));
  }

  @PatchMapping("/{project_id}/tasks/{task_id}/points")
  @Operation(description = "Update point task")
  public BaseResponse<TaskResponse> updatePointTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification task")
      @PathVariable("task_id") String taskId,
      @Valid @RequestBody UpdatePointTaskRequest updatePointTaskRequest) {
    log.info("(updatePointTask),projectId: {}, taskId: {}", projectId, taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updatePointTask(getUserId(), projectId, taskId, updatePointTaskRequest.getPoint()));
  }

  @PatchMapping("/{project_id}/sprints/{sprint_id}/tasks/{task_id}")
  @Operation(description = "Update sprint of task")
  public BaseResponse<TaskResponse> updateSprintTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification taskId")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "sprint_id", description = "Identification sprint")
      @PathVariable("sprint_id") String sprintId) {
    log.info("(updateSprintTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateSprintTask(getUserId(), projectId, sprintId, taskId));
  }

  @PostMapping("/{project_id}/tasks/{task_id}/clone")
  @Operation(description = "Clone task")
  public BaseResponse<TaskResponse> cloneTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification task")
      @PathVariable("task_id") String taskId) {
    log.info("(cloneTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.cloneTask(getUserId(), projectId, taskId));
  }

  @PutMapping("/{project_id}/sprints/{sprint_id}/tasks/{task_id}/update-date")
  @Operation(description = "Update start date and due date of task")
  public BaseResponse<UpdateDueDateTaskResponse> updateStartDateDueDateTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint_id", description = "Identification sprint")
      @PathVariable("sprint_id") String sprintId,
      @Parameter(name = "task_id", description = "Identification task")
      @PathVariable("task_id") String taskId,
      @RequestBody @Valid UpdateDueDateTaskRequest updateDueDateTaskRequest) {
    log.info("(updateStartDateDueDateTask)project: {}, sprint: {}, task: {}", projectId, sprintId,
        taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateStartDateDueDateTask(getUserId(), projectId, sprintId, taskId,
            updateDueDateTaskRequest.getStatusTaskKey(),
            updateDueDateTaskRequest.getDueDate()));
  }

  @GetMapping("/{project_id}/sprints/{sprint_id}/tasks")
  @Operation(description = "Get all task by sprint")
  public BaseResponse<List<TaskResponse>> getAllBySprintId(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint_id", description = "Identification sprint")
      @PathVariable("sprint_id") String sprintId) {
    log.info("(getAllTaskBySprintId)");
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.getAllBySprintId(projectId, sprintId));
  }

  @DeleteMapping ("/{project_id}/tasks/{task_id}")
  @Operation(description = "Delete task")
  public BaseResponse<String> deleteTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification task")
      @PathVariable("task_id") String taskId) {
    log.info("(deleteTask)projectId: {}, taskId: {}", projectId, taskId);
    taskFacadeService.deleteTask(getUserId(), projectId, taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Successfull delete task!");
  }

  @PutMapping("/{project_id}/tasks/{task_id}")
  @Operation(description = "Update title task")
  public BaseResponse<TaskResponse> updateTitleTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification task")
      @PathVariable("task_id") String taskId,
      @Valid @RequestBody UpdateTitleTaskRequest request) {
    log.info("(updateTitleTask)project: {}, task: {}, title: {}", projectId, taskId, request.getTitle());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateTitleTask(getUserId(), projectId, taskId, request.getTitle()));
  }
  @GetMapping("/{project_id}/tasks/search")
  @Operation(description = "Search task by keyProjectTask, title, typeId, labelId")
  public BaseResponse<List<TaskResponse>> searchTask(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "search", description = "KeyProjectTask or title")
      @RequestParam(required = false)  String search,
      @Parameter(name = "typeId", description = "TypeId")
      @RequestParam(required = false)  String typeId,
      @Parameter(name = "labelId", description = "LabelId")
      @RequestParam(required = false)  String labelId) {
    log.info("(searchTask)project: {}, search: {}", projectId, search);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.searchTask(search, typeId, labelId, getUserId(), projectId));
  }

  @GetMapping("/{project_id}/tasks/search-board")
  @Operation(description = "Search task by keyProjectTask, title, sprintId")
  public BaseResponse<List<TaskResponse>> searchTaskBoard(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "search", description = "KeyProjectTask or title")
      @RequestParam(required = false)  String search,
      @Parameter(name = "sprintId", description = "SprintId")
      @RequestParam(required = false)  String sprintId) {
    log.info("(searchTaskBoard)project: {}, search: {}, sprintId: {}", projectId, search, sprintId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.searchTaskBoard(search, sprintId, getUserId(), projectId));
  }

  @GetMapping("/tasks/search-filter")
  @Operation(description = "Search task by keyProjectTask, title, projectId, typeId, status, assignee")
  public BaseResponse<List<TaskResponse>> searchTaskBoard(
      @Parameter(name = "project_id", description = "Project id")
      @RequestParam(required = false)  String projectId,
      @Parameter(name = "search", description = "KeyProjectTask or title")
      @RequestParam(required = false)  String search,
      @Parameter(name = "typeId", description = "TypeId")
      @RequestParam(required = false)  String typeId,
      @Parameter(name = "status", description = "Status")
      @RequestParam(required = false)  String status,
      @Parameter(name = "assignee", description = "Assignee")
      @RequestParam(required = false)  String assignee) {
    log.info("(searchTaskBoard)project: {}, search: {}, typeId: {}, status: {}, assignee: {}", projectId, search, typeId, status, assignee);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.searchTaskFilter(search, typeId, status, assignee, getUserId(), projectId));
  }
}
