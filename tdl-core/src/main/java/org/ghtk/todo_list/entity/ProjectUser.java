package org.ghtk.todo_list.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.entity.base.BaseEntity;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "project_user")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProjectUser extends BaseEntity implements Serializable {

  private String userId;
  private String projectId;
  private String role;
  private LocalDateTime expireDateShare;
}
