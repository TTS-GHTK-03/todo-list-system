package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.Comment;
import org.ghtk.todo_list.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

}
