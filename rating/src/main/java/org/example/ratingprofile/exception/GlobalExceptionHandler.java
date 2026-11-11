package org.example.ratingprofile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyRatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyRated(AlreadyRatedException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleProfileNotFound(ProfileNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ProfileServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, String> handleProfileUnavailable(ProfileServiceUnavailableException e) {
        return Map.of("error", e.getMessage());
    }
}