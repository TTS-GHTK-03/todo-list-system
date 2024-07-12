package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, String> {

}
