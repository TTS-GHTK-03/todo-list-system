package org.ghtk.todo_list.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Board;
import org.ghtk.todo_list.mapper.BoardMapper;
import org.ghtk.todo_list.repository.BoardRepository;
import org.ghtk.todo_list.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final BoardRepository boardRepository;

  private final BoardMapper boardMapper;

  @Override
  public Board createBoard(String projectId) {
    Board board = boardMapper.toBoard(projectId);
    return boardRepository.save(board);
  }
}
