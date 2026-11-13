package org.example.ratingprofile.dto;

import org.example.ratingprofile.model.Rating;

import java.time.Instant;

public record CommentResponse(
        String comment,
        String bioVersion,
        Instant createdAt
) {
    public static CommentResponse from(Rating rating) {
        return new CommentResponse(
                rating.getProfileComment(),
                rating.getBioVersion(),
                rating.getCreatedAt()
        );
    }
}