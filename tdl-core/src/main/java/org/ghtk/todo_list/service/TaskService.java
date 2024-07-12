package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Task;

import java.util.List;

public interface TaskService {

  List<Task> getAllTasksByProjectId(String projectId);

}
