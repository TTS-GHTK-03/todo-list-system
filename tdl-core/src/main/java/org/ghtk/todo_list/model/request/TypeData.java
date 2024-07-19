package org.ghtk.todo_list.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeData {

  private String title;
  private String url;
}
