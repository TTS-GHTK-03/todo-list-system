package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.dto.request.ForgotPasswordRequest;
import org.ghtk.todo_list.dto.request.VerifyResetPasswordRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.dto.response.VerifyResetPasswordResponse;

public interface AuthFacadeService {

  void register(RegisterRequest request);

  void forgotPassword (ForgotPasswordRequest request);

  VerifyResetPasswordResponse verifyResetPassword(VerifyResetPasswordRequest request);
}
