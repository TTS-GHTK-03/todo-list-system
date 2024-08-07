package org.ghtk.todo_list.configuration;

import lombok.RequiredArgsConstructor;
import org.ghtk.todo_list.error_handle.AuthenticationErrorHandle;
import org.ghtk.todo_list.filter.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@ComponentScan(basePackages = {
    "org.ghtk.todo_list.filter",
    "org.ghtk.todo_list.error_handle"
})
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration {

  private final TokenAuthenticationFilter tokenAuthenticationFilter;
  private final AuthenticationErrorHandle authenticationErrorHandle;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/v1/accept").permitAll()
            .requestMatchers("/api/v1/view_share").permitAll()
            .requestMatchers("/api/v1/projects/**").authenticated()
            .requestMatchers("/api/v1/users/**").authenticated()
            .requestMatchers("/api/v1/sprints/**").authenticated()
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/swagger**", "/swagger-ui**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                authenticationErrorHandle))
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .build();
  }

}
