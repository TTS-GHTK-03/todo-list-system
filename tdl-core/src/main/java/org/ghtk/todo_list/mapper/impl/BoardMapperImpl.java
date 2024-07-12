package org.ghtk.todo_list.mapper.impl;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Board;
import org.ghtk.todo_list.mapper.BoardMapper;
import org.springframework.stereotype.Component;

@Component
public class BoardMapperImpl implements BoardMapper {

  @Override
  public Board toBoard(String projectId, LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
    Board board = new Board();
    board.setProjectId(projectId);
    board.setCreatedAt(createdAt);
    board.setLastUpdatedAt(lastUpdatedAt);
    return board;
  }
}
