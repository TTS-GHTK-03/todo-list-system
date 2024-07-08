package org.ghtk.todo_list.entity.base;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@MappedSuperclass
public class BaseEntity {
  @Id
  private String id;

  @CreatedDate
  private Long createdAt;
  @LastModifiedDate
  private Long lastUpdatedAt;

  @PrePersist
  public void ensureId() {
    this.id = Objects.isNull(this.id) ? UUID.randomUUID().toString() : this.id;
  }
}
