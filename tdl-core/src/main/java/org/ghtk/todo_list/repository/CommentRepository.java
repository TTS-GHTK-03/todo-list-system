package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

  List<Comment> findAllByTaskId(String taskId);

  List<Comment> findAllByParentId(String parentId);

  boolean existsById(String id);
  void deleteAllByParentId(String parentId);
}
