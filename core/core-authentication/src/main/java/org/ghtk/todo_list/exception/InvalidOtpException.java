package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class InvalidOtpException extends BadRequestException {

    public InvalidOtpException() {
        setStatus(400);
        setCode("InvalidOtpException");
    }
}
