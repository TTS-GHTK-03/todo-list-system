package org.ghtk.todo_list.filter;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import org.ghtk.todo_list.entity.LabelAttached;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.entity.Task;
import org.springframework.data.jpa.domain.Specification;

public class FilterTask {

  public static Specification<Task> getTasksByCriteria(String searchValue,
      String userId, String projectId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (searchValue != null && !searchValue.isEmpty()) {
        Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + searchValue.toLowerCase() + "%");
        Predicate keyProjectTaskPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("keyProjectTask")), "%" + searchValue.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(titlePredicate, keyProjectTaskPredicate));
      }

      if (userId != null && !userId.isEmpty()) {
        Subquery<String> subquery = query.subquery(String.class);
        Root<ProjectUser> projectUserRoot = subquery.from(ProjectUser.class);
        subquery.select(projectUserRoot.get("projectId"));
        subquery.where(
            criteriaBuilder.equal(projectUserRoot.get("userId"), userId),
            criteriaBuilder.equal(projectUserRoot.get("projectId"), root.get("projectId"))
        );

        predicates.add(criteriaBuilder.exists(subquery));
      }

      if (projectId != null && !projectId.isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("projectId"), projectId));
      }

      query.distinct(true);

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<Task> getTasksByTypeOrLabelAndCriteria(String typeId, String labelId,
      String userId, String projectId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (typeId != null && !typeId.isEmpty()) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("typeId")), "%" + typeId.toLowerCase() + "%"));
      }

      if (labelId != null && !labelId.isEmpty()) {
        Subquery<String> labelSubquery = query.subquery(String.class);
        Root<LabelAttached> labelAttachedRoot = labelSubquery.from(LabelAttached.class);
        labelSubquery.select(labelAttachedRoot.get("taskId"));
        labelSubquery.where(criteriaBuilder.equal(labelAttachedRoot.get("labelId"), labelId));

        predicates.add(criteriaBuilder.in(root.get("id")).value(labelSubquery));
      }

      if (userId != null && !userId.isEmpty()) {
        Subquery<String> userSubquery = query.subquery(String.class);
        Root<ProjectUser> projectUserRoot = userSubquery.from(ProjectUser.class);
        userSubquery.select(projectUserRoot.get("taskId"));
        userSubquery.where(
            criteriaBuilder.equal(projectUserRoot.get("userId"), userId),
            criteriaBuilder.equal(projectUserRoot.get("projectId"), root.get("projectId"))
        );

        predicates.add(criteriaBuilder.exists(userSubquery));
      }

      if (projectId != null && !projectId.isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("projectId"), projectId));
      }

      query.distinct(true);

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
