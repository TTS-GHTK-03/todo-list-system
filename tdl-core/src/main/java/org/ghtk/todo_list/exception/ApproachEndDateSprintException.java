package org.ghtk.todo_list.exception;

import org.ghtk.todo_list.core_exception.exception.BadRequestException;

public class ApproachEndDateSprintException extends BadRequestException {
 public ApproachEndDateSprintException() {
   setStatus(400);
   setCode("ApproachEndDateSprintException");
 }
}
