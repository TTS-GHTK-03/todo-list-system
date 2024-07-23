package org.ghtk.todo_list.filter;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;
import org.springframework.data.jpa.domain.Specification;

public class FilterProject {

  public static Specification<Project> getProjectsByCriteria(String title, String keyProject, String userId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (title != null && !title.isEmpty()) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
      }

      if (keyProject != null && !keyProject.isEmpty()) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("keyProject")), "%" + keyProject.toLowerCase() + "%"));
      }

      if (userId != null && !userId.isEmpty()) {
        Subquery<String> subquery = query.subquery(String.class);
        Root<ProjectUser> projectUserRoot = subquery.from(ProjectUser.class);
        subquery.select(projectUserRoot.get("projectId"));
        subquery.where(
            criteriaBuilder.equal(projectUserRoot.get("userId"), userId),
            criteriaBuilder.equal(projectUserRoot.get("projectId"), root.get("id"))
        );

        predicates.add(criteriaBuilder.exists(subquery));
      }

      query.distinct(true);

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
