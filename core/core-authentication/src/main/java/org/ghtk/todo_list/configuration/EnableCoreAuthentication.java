package org.ghtk.todo_list.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.ghtk.todo_list.controller.AuthUserController;
import org.ghtk.todo_list.controller.UserController;
import org.springframework.context.annotation.Import;

@Import({
    CoreAuthenticationConfiguration.class,
    WebSecurityConfiguration.class,
    AuthUserController.class,
    UserController.class,
    RedisConfiguration.class
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableCoreAuthentication {
}
