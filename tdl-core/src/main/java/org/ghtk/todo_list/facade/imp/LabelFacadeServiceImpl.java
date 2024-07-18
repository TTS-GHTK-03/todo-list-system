package org.ghtk.todo_list.facade.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Label;
import org.ghtk.todo_list.exception.LabelAlreadyExistsException;
import org.ghtk.todo_list.exception.SprintStatusNotFoundException;
import org.ghtk.todo_list.exception.TypeNotFoundException;
import org.ghtk.todo_list.facade.LabelFacadeService;
import org.ghtk.todo_list.mapper.LabelMapper;
import org.ghtk.todo_list.model.response.LabelResponse;
import org.ghtk.todo_list.service.LabelService;
import org.ghtk.todo_list.service.TypeService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LabelFacadeServiceImpl implements LabelFacadeService {

  private final LabelService labelService;
  private final LabelMapper labelMapper;
  private final TypeService typeService;

  @Override
  public LabelResponse createLabel(String typeId, String title, String description) {
    log.info("(createLabel)");

    if(!typeService.existById(typeId)) {
      log.error("(createLabel) typeId not found: {}", typeId);
      throw new TypeNotFoundException();
    }
    if(labelService.existByTypeIdAndTitle(typeId, title)) {
      log.error("(createLabel)Invalid typeId: {}, title: {}", typeId, title);
      throw new LabelAlreadyExistsException();
    }

    Label label = new Label();
    label.setTitle(title);
    label.setTypeId(typeId);
    label.setDescription(description);
    label = labelService.save(label);

    log.info("(createLabel) label: {}", label);
    return labelMapper.toLabelResponse(label);
  }
}
