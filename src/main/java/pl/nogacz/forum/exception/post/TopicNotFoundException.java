package pl.nogacz.forum.exception.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This topic is not found")
public class TopicNotFoundException extends Exception {}