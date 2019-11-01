package pl.nogacz.forum.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User role not found")
public class UserRoleNotFoundException extends Exception {}
