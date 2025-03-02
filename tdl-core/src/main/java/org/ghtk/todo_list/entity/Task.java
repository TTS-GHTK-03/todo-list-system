package org.ghtk.todo_list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
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
  private Integer point;
  private String checklist;
  private String status;
  private LocalDate startDate;
  private LocalDate dueDate;
  private String keyProjectTask;
  private String sprintId;
  private String userId;
  private String columnId;
  private String projectId;
  private String typeId;
  private Boolean isSendMail;
}
