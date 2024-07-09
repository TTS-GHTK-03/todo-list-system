package org.ghtk.todo_list.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"org.ghtk.todo_list.repository"})
@ComponentScan(basePackages = {"org.ghtk.todo_list.repository"})
@EnableJpaAuditing
@EntityScan(basePackages = "org.ghtk.todo_list.entity")
public class TdlCoreConfiguration {

}
