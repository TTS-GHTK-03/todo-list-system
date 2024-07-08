package org.ghtk.todo_list.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@ComponentScan(basePackages = {
    "org.ghtk.todo_list.filter",
    "org.ghtk.todo_list.error_handle"
})
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration {


}
