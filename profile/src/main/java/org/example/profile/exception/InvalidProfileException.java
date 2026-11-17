package org.example.profile.exception;

public class InvalidProfileException extends RuntimeException{
    public InvalidProfileException(String message){
        super(message);
    }
}