package org.ghtk.todo_list.mapper.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.mapper.SprintMapper;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.SprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;
import org.springframework.stereotype.Component;

@Component
public class SprintMapperImpl implements SprintMapper {

  @Override
  public CreateSprintResponse toCreateSprintResponse(Sprint sprint) {
    return CreateSprintResponse.builder()
        .id(sprint.getId())
        .title(sprint.getTitle())
        .status(sprint.getStatus())
        .build();
  }

  @Override
  public StartSprintResponse toStartSprintResponse(Sprint sprint) {
    return StartSprintResponse.builder()
        .id(sprint.getId())
        .title(sprint.getTitle())
        .status(sprint.getStatus())
        .startDate(sprint.getStartDate())
        .endDate(sprint.getEndDate())
        .build();
  }

  @Override
  public List<SprintResponse> toSprintResponses(List<Sprint> sprints) {
    return sprints.stream()
        .map(sprint -> {
          return SprintResponse.builder()
              .id(sprint.getId())
              .title(sprint.getTitle())
              .status(sprint.getStatus())
              .startDate(sprint.getStartDate())
              .endDate(sprint.getEndDate())
              .createdAt(sprint.getCreatedAt())
              .lastUpdatedAt(sprint.getLastUpdatedAt())
              .build();
        })
        .collect(Collectors.toList());
  }

  @Override
  public SprintResponse toSprintResponse(Sprint sprint) {
    return SprintResponse.builder()
        .id(sprint.getId())
        .title(sprint.getTitle())
        .status(sprint.getStatus())
        .startDate(sprint.getStartDate())
        .endDate(sprint.getEndDate())
        .createdAt(sprint.getCreatedAt())
        .lastUpdatedAt(sprint.getLastUpdatedAt())
        .build();
  }
}
