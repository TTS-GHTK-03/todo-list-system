package org.ghtk.todo_list.facade.imp;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Label;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.exception.InvalidTypeException;
import org.ghtk.todo_list.exception.LabelAlreadyExistsException;
import org.ghtk.todo_list.exception.LabelNotExistsException;
import org.ghtk.todo_list.exception.LabelNotFoundException;
import org.ghtk.todo_list.exception.ProjectIdMismatchException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.SprintNotExistProjectException;
import org.ghtk.todo_list.exception.TypeNotExistProjectException;
import org.ghtk.todo_list.facade.LabelFacadeService;
import org.ghtk.todo_list.mapper.LabelMapper;
import org.ghtk.todo_list.model.response.LabelAttachedResponse;
import org.ghtk.todo_list.model.response.LabelResponse;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.ghtk.todo_list.service.LabelService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.TypeService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LabelFacadeServiceImpl implements LabelFacadeService {

  private final LabelService labelService;
  private final LabelMapper labelMapper;
  private final TypeService typeService;
  private final LabelAttachedService labelAttachedService;
  private final ProjectService projectService;

  @Override
  public LabelResponse createLabel(String projectId, String typeId, String title,
      String description) {
    log.info("(createLabel)");
    validProjectId(projectId);
    validProjectInType(projectId, typeId);

    if (labelService.existByTypeIdAndTitle(typeId, title)) {
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

  @Override
  public LabelResponse updateLabel(String projectId, String typeId, String labelId, String title,
      String description) {
    log.info("(updateLabel)");
    validProjectId(projectId);
    validProjectInType(projectId, typeId);
    validateLabelId(labelId);
    validateTypeIdAndLabelId(typeId, labelId);

    Label label = labelService.findById(labelId);

    if (!label.getTypeId().equals(typeId)) {
      log.error("(updateLabel)Invalid typeId: {}", typeId);
      throw new InvalidTypeException();
    }

    if (labelService.existByTypeIdAndTitle(typeId, title) && !label.getTitle().equals(title)) {
      log.error("(updateLabel)Invalid typeId: {}, title: {}", typeId, title);
      throw new LabelAlreadyExistsException();
    }
    label.setTitle(title);
    label.setDescription(description);
    label = labelService.save(label);

    log.info("(updateLabel) label: {}", label);
    return labelMapper.toLabelResponse(label);
  }

  @Override
  public List<LabelResponse> getLabelsByTypeId(String projectId, String typeId) {
    log.info("(getLabelsByTypeId)");
    validProjectId(projectId);
    validProjectInType(projectId, typeId);
    List<Label> labels = labelService.getLabelsByType(typeId);

    log.info("(getLabelsByTypeId)labels: {}", labels);
    return labelMapper.toLabelResponses(labels);
  }

  @Override
  public List<LabelResponse> getAllLabelByProjectIdAndLabelAttached(String userId, String projectId) {
    log.info("(getAllLabelByProjectIdAndLabelAttached) userId: {}, projectId: {}", userId, projectId);
    validProjectId(projectId);

    List<Label> labelList = labelService.getAllLabelByProjectIdAndLabelAttached(projectId);

    return labelMapper.toLabelResponses(labelList);
  }

  @Override
  @Transactional
  public void deleteLabel(String projectId, String typeId, String labelId) {
    log.info("(deleteLabel) label: {}", labelId);
    validProjectId(projectId);
    validProjectInType(projectId, typeId);
    validateLabelId(labelId);
    validateTypeIdAndLabelId(typeId, labelId);

    labelAttachedService.deleteByLabelId(labelId);
    labelService.deleteLabel(labelId);
    log.info("(deleteLabel) successfully");
  }

  private void validProjectId(String projectId) {
    log.info("(validProjectId) projectId: {}", projectId);
    if(!projectService.existById(projectId)) {
      log.error("(validProjectId) ProjectId: {} not found", projectId);
      throw new ProjectNotFoundException();
    }
  }
  private void validProjectInType(String projectId, String typeId) {
    log.info("(validProjectInType)");
    Type type = typeService.findById(typeId);
    if (!type.getProjectId().equals(projectId)) {
      log.error("(validProjectInType)typeId {} not part of projectId {}", typeId, projectId);
      throw new TypeNotExistProjectException();
    }
  }

  private void validateLabelId(String labelId){
    log.info("(validateLabelId)labelId: {}", labelId);
    if(!labelService.existsById(labelId)){
      log.error("(validateLabelId)labelId: {} not found", labelId);
      throw new LabelNotFoundException();
    }
  }

  private void validateTypeIdAndLabelId(String typeId, String labelId){
    log.info("(validateTypeIdAndLabelId)typeId: {}, labelId: {}", typeId, labelId);
    if(!labelService.existsByTypeIdAndLabelId(typeId, labelId)){
      log.error("(validateTypeIdAndLabelId)labelId: {} not exists in typeId: {}", labelId, typeId);
      throw new LabelNotExistsException();
    }
  }
}
