package pl.nogacz.forum.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This email exist in database")
public class EmailExistException extends Exception {}
