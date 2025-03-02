package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;
import org.ghtk.todo_list.core_exception.exception.ConflictException;

public class ProjectTitleAlreadyExistedException extends BadRequestException {
    public ProjectTitleAlreadyExistedException(){
        setStatus(400);
        setCode("ProjectTitleAlreadyExistedException");
    }
}
