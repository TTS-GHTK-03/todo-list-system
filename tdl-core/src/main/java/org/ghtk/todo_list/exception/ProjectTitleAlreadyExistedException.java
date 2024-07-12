package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.ConflictException;

public class ProjectTitleAlreadyExistedException extends ConflictException {
    public ProjectTitleAlreadyExistedException(){
        setStatus(409);
        setCode("org.ghtk.todo_list.exception.ProjectTitleAlreadyExistedException");
    }
}
