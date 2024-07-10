package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.dto.request.ForgotPasswordRequest;
import org.ghtk.todo_list.dto.request.OTPResetPasswordRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.dto.response.OTPResetPasswordResponse;

public interface AuthFacadeService {

  void register(RegisterRequest request);

  void forgotPassword (ForgotPasswordRequest request);

  OTPResetPasswordResponse resetPasswordOtpValidate(OTPResetPasswordRequest request);
}
