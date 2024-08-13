package org.ghtk.todo_list.service;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Board;

public interface BoardService {
  Board createBoard(String projectId);
}
