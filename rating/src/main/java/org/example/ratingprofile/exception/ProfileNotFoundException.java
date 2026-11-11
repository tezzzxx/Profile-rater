package org.example.ratingprofile.exception;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(String profileId) {
        super("Profile " + profileId + " does not exist");
    }
}