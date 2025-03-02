package org.ghtk.todo_list.mapper.impl;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Board;
import org.ghtk.todo_list.mapper.BoardMapper;
import org.springframework.stereotype.Component;

@Component
public class BoardMapperImpl implements BoardMapper {

  @Override
  public Board toBoard(String projectId) {
    Board board = new Board();
    board.setProjectId(projectId);
    return board;
  }
}
