package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.dto.request.ActiveAccountRequest;
import org.ghtk.todo_list.dto.request.ForgotPasswordRequest;
import org.ghtk.todo_list.dto.request.LoginRequest;
import org.ghtk.todo_list.dto.request.VerifyResetPasswordRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.dto.response.LoginResponse;
import org.ghtk.todo_list.dto.request.*;
import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.dto.response.VerifyResetPasswordResponse;

public interface AuthFacadeService {

  void register(RegisterRequest request);

  void activeAccount(ActiveAccountRequest request);

  void forgotPassword (ForgotPasswordRequest request);
  VerifyResetPasswordResponse verifyResetPassword(VerifyResetPasswordRequest request);
  LoginResponse login(LoginRequest request);

  void resetPassword(ResetPasswordRequest request);
  void changePassword(ChangePasswordRequest request, String userId);
}
