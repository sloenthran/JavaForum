package pl.nogacz.forum.exception.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Invalid credentials")
public class InvalidCredentialsException extends Exception {}