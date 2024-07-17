package org.ghtk.todo_list.mapper;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Board;

public interface BoardMapper {
  Board toBoard(String projectId);
}
