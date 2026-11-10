package org.example.ratingprofile.exception;

public class AlreadyRatedException extends RuntimeException {
    public AlreadyRatedException() {
        super("You have already rated this profile");
    }
}