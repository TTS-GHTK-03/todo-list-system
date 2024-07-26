package org.ghtk.todo_list.filter;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.ProjectUser;
import org.springframework.data.jpa.domain.Specification;

public class FilterUser {

  public static Specification<AuthUser> getUsersByCriteria(String searchValue, List<String> roles, String projectId, String userId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (searchValue != null && !searchValue.isEmpty()) {
        Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + searchValue.toLowerCase() + "%");
        Predicate middleNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName")), "%" + searchValue.toLowerCase() + "%");
        Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + searchValue.toLowerCase() + "%");
        predicates.add(criteriaBuilder.or(firstNamePredicate, middleNamePredicate, lastNamePredicate));
      }

      if (projectId != null && !projectId.isEmpty()) {
        Subquery<String> permissionSubquery = query.subquery(String.class);
        Root<ProjectUser> permissionRoot = permissionSubquery.from(ProjectUser.class);
        permissionSubquery.select(permissionRoot.get("userId"));
        permissionSubquery.where(
            criteriaBuilder.equal(permissionRoot.get("projectId"), projectId),
            criteriaBuilder.equal(permissionRoot.get("userId"), userId)
        );

        Predicate userHasPermission = criteriaBuilder.exists(permissionSubquery);
        predicates.add(userHasPermission);

        Subquery<String> projectUserSubquery = query.subquery(String.class);
        Root<ProjectUser> projectUserRoot = projectUserSubquery.from(ProjectUser.class);
        projectUserSubquery.select(projectUserRoot.get("userId"));
        projectUserSubquery.where(criteriaBuilder.equal(projectUserRoot.get("projectId"), projectId));

        predicates.add(criteriaBuilder.in(root.get("id")).value(projectUserSubquery));

        if (roles != null && !roles.isEmpty()) {
          List<Predicate> rolePredicates = new ArrayList<>();
          for (String role : roles) {
            rolePredicates.add(criteriaBuilder.equal(projectUserRoot.get("role"), role));
          }
          projectUserSubquery.where(criteriaBuilder.and(
              criteriaBuilder.equal(projectUserRoot.get("projectId"), projectId),
              criteriaBuilder.or(rolePredicates.toArray(new Predicate[0]))
          ));
          predicates.add(criteriaBuilder.in(root.get("id")).value(projectUserSubquery));
        }
      }
      query.distinct(true);

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
