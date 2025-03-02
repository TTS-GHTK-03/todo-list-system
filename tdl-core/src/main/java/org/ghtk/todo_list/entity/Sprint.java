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
@Table(name = "sprint")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Sprint extends BaseEntity {

  private String title;
  private String status;
  private LocalDate startDate;
  private LocalDate endDate;
  private String projectId;
}
