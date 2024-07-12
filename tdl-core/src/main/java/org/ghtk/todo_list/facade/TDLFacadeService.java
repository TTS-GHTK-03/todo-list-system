package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.repository.TaskRepository;
import org.w3c.dom.stylesheets.LinkStyle;

public interface TDLFacadeService {

  List<Task> getAllTaskByProjectId(String projectId);
}
