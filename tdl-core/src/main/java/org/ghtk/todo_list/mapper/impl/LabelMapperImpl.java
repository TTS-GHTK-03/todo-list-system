package org.ghtk.todo_list.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.ghtk.todo_list.entity.Label;
import org.ghtk.todo_list.mapper.LabelMapper;
import org.ghtk.todo_list.model.response.LabelResponse;
import org.springframework.stereotype.Component;

@Component
public class LabelMapperImpl implements LabelMapper {

  @Override
  public LabelResponse toLabelResponse(Label label) {
    return LabelResponse.builder()
        .id(label.getId())
        .typeId(label.getTypeId())
        .title(label.getTitle())
        .description(label.getDescription())
        .build();
  }

  @Override
  public List<LabelResponse> toLabelResponses(List<Label> labels) {
    return labels.stream()
        .map(label -> {
          return LabelResponse.builder()
              .id(label.getId())
              .typeId(label.getTypeId())
              .title(label.getTitle())
              .description(label.getDescription())
              .build();
        })
        .collect(Collectors.toList());
  }
}
