package org.ghtk.todo_list.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@MappedSuperclass
public class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  @CreatedDate
  private Long createdAt;
  @LastModifiedDate
  private Long lastUpdatedAt;


}
