package pl.nogacz.forum.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This password is too short")
public class PasswordTooShortException extends Exception {}