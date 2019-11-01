package pl.nogacz.forum.exception.user.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This username exists in database")
public class UserUsernameExistException extends Exception {}
