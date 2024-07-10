package org.ghtk.todo_list.configuration;

import org.ghtk.todo_list.core_email.configuration.EnableCoreEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"org.ghtk.todo_list.repository"})
@Configuration
@EnableJpaRepositories(
    basePackages = {"org.ghtk.todo_list.repository"},
    transactionManagerRef = "jpaAuthTransactionManager"
)
@EntityScan(basePackages = {"org.ghtk.todo_list.entity"})
@EnableJpaAuditing
@EnableCoreEmail
public class CoreAuthenticationConfiguration {

  @Value("${application.authentication.access_token.jwt_secret:xxx}")
  private String accessTokenJwtSecret;

  @Value("${application.authentication.access_token.life_time}")
  private Long accessTokenLifeTime;

  @Value("${application.authentication.refresh_token.jwt_secret:xxx}")
  private String refreshTokenJwtSecret;

  @Value("${application.authentication.refresh_token.life_time}")
  private Long refreshTokenLifeTime;

  @Value("${application.authentication.redis.otp_time_out:3}")
  private Integer redisOtpTimeOut;
}
