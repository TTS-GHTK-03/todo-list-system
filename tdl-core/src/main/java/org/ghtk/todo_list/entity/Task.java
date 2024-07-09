package org.ghtk.todo_list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.entity.base.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "task")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task extends BaseEntity {

  private String title;
  private String description;
  private String label;
  private Integer point;
  private String checklist;
  private String status;
  private LocalDate dueDate;
  private String sprintId;
  private String userId;
  private String columnId;
  private String projectId;
}
