package pl.nogacz.forum.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Old password is wrong")
public class BadOldPasswordException extends Exception {}
