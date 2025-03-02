package org.ghtk.todo_list.core_email.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Import({
    CoreEmailConfiguration.class,
    AsyncConfiguration.class,
    ThymeleafTemplateConfiguration.class
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface EnableCoreEmail {
}
