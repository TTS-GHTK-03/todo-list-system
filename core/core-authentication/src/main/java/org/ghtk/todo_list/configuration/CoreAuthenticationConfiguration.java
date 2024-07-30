package org.ghtk.todo_list.configuration;

import org.ghtk.todo_list.core_email.configuration.EnableCoreEmail;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.facade.AuthFacadeService;
import org.ghtk.todo_list.facade.AuthFacadeServiceImpl;
import org.ghtk.todo_list.mapper.UserResponseMapper;
import org.ghtk.todo_list.repository.AuthAccountRepository;
import org.ghtk.todo_list.repository.AuthUserRepository;
import org.ghtk.todo_list.service.AuthAccountService;
import org.ghtk.todo_list.service.AuthTokenService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.OtpService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.service.impl.AuthAccountServiceImpl;
import org.ghtk.todo_list.service.impl.AuthUserServiceImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = {"org.ghtk.todo_list.entity"})
@EnableCoreEmail
public class CoreAuthenticationConfiguration {

  @Bean
  public AuthAccountService authAccountService(AuthAccountRepository repository) {
    return new AuthAccountServiceImpl(repository);
  }

  @Bean
  public AuthFacadeService authFacadeService(
      AuthAccountService authAccountService,
      AuthUserService authUserService,
      OtpService otpService,
      RedisCacheService redisCacheService,
      EmailHelper emailHelper,
      AuthTokenService authTokenService
  ) {
    return new AuthFacadeServiceImpl(
        authAccountService, authUserService, otpService, redisCacheService, emailHelper,
        authTokenService);
  }

  @Bean
  public AuthUserService authUserService(AuthUserRepository repository, UserResponseMapper authUserResponseMapper) {
    return new AuthUserServiceImpl(repository, authUserResponseMapper);
  }
}
