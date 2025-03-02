package org.ghtk.todo_list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "activity_log")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ActivityLog {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String action;
  private String taskId;
  private String sprintId;
  private String userId;
  @CreatedDate
  private LocalDateTime createdAt;
}
