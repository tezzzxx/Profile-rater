package org.example.ratingprofile.exception;

public class ProfileServiceUnavailableException extends RuntimeException {
    public ProfileServiceUnavailableException() {
        super("Profile service is unavailable");
    }
}