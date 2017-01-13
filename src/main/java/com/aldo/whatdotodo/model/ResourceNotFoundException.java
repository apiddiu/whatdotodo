package com.aldo.whatdotodo.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String resourceId) {
        super(String.format("%s: %s not found!", resourceName, resourceId));
    }
}
