package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.dto.request.*;
import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.dto.response.VerifyResetPasswordResponse;

public interface AuthFacadeService {

  void register(RegisterRequest request);

  void activeAccount(ActiveAccountRequest request);

  void forgotPassword (ForgotPasswordRequest request);

  VerifyResetPasswordResponse verifyResetPassword(VerifyResetPasswordRequest request);

  void resetPassword(ResetPasswordRequest request);
}
