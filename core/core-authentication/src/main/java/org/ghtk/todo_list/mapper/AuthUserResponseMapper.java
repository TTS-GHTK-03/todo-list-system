package org.ghtk.todo_list.mapper;

import java.util.List;
import org.ghtk.todo_list.dto.response.AuthUserResponse;
import org.ghtk.todo_list.entity.AuthUser;

public interface AuthUserResponseMapper {
  List<AuthUserResponse> toAuthUserResponseList(List<AuthUser> authUserList);
}
