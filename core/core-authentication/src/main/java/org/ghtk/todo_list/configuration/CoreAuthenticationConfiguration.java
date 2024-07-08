package org.ghtk.todo_list.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"org.ghtk.todo_list.repository"})
@Configuration
@EnableJpaRepositories(
    basePackages = {"org.ghtk.todo_list.repository"},
    transactionManagerRef = "jpaAuthTransactionManager"
)
@EntityScan(basePackages = {"org.ghtk.todo_list.entity"})
public class CoreAuthenticationConfiguration {

}
