package org.ghtk.todo_list.exception;
import org.ghtk.todo_list.core_exception.exception.NotFoundException;

public class ProjectNotFoundException extends NotFoundException {
    public ProjectNotFoundException() {
        setStatus(404);
        setCode("ProjectNotFoundException");
    }
}
