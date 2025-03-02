package org.ghtk.todo_list.mapper;

import java.util.List;
import org.ghtk.todo_list.dto.response.AuthUserResponse;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.entity.AuthUser;

public interface UserResponseMapper {
  List<UserResponse> toUserResponseList(List<AuthUser> authUser);
  UserResponse toUserResponse(AuthUser authUser);
}
