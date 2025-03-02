package org.ghtk.todo_list.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.mapper.UserResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapperImpl implements UserResponseMapper {

  @Override
  public List<UserResponse> toUserResponseList(List<AuthUser> authUserList) {
    List<UserResponse> userResponseList = new ArrayList<>();
    for (AuthUser authUser : authUserList) {
      UserResponse userResponse = new UserResponse();
      userResponse.setId(authUser.getId());
      userResponse.setFirstName(authUser.getFirstName());
      userResponse.setMiddleName(authUser.getMiddleName());
      userResponse.setLastName(authUser.getLastName());
      userResponse.setEmail(authUser.getEmail());
      userResponseList.add(userResponse);
    }
    return userResponseList;
  }

  @Override
  public UserResponse toUserResponse(AuthUser authUser) {
    UserResponse userResponse = new UserResponse();
    userResponse.setId(authUser.getId());
    userResponse.setFirstName(authUser.getFirstName());
    userResponse.setMiddleName(authUser.getMiddleName());
    userResponse.setLastName(authUser.getLastName());
    userResponse.setEmail(authUser.getEmail());
    return userResponse;
  }
}