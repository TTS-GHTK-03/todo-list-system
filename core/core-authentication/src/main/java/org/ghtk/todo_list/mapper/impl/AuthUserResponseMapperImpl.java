package org.ghtk.todo_list.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import org.ghtk.todo_list.dto.response.AuthUserResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.mapper.AuthUserResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthUserResponseMapperImpl implements AuthUserResponseMapper {

  @Override
  public List<AuthUserResponse> toAuthUserResponseList(List<AuthUser> authUserList) {
    List<AuthUserResponse> authUserResponseList = new ArrayList<>();
    for (AuthUser authUser : authUserList) {
      AuthUserResponse authUserResponse = new AuthUserResponse();
      authUserResponse.setId(authUser.getId());
      authUserResponse.setFirstName(authUser.getFirstName());
      authUserResponse.setMiddleName(authUser.getMiddleName());
      authUserResponse.setLastName(authUser.getLastName());
      authUserResponse.setEmail(authUser.getEmail());
      authUserResponse.setDateOfBirth(authUser.getDateOfBirth());
      authUserResponse.setPhone(authUser.getPhone());
      authUserResponse.setAddress(authUserResponse.getAddress());
      authUserResponse.setGender(authUserResponse.getGender());
      authUserResponseList.add(authUserResponse);
    }
    return authUserResponseList;
  }
}
