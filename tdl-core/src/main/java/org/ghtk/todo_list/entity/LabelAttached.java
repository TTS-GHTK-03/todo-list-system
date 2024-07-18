package org.ghtk.todo_list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "label_attached")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LabelAttached extends BaseEntity{
  private String taskId;
  private String labelId;
}
