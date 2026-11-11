package org.example.ratingprofile.event;

import org.example.ratingprofile.model.Rating;

import java.time.LocalDateTime;

public record RatingCreatedEvent(
        String ratingId,
        String profileId,
        String raterId,
        int picRating,
        int bioRating,
        int overAllRating,
        LocalDateTime createdAt
) {
    public static RatingCreatedEvent from(Rating rating) {
        return new RatingCreatedEvent(
                rating.getId(),
                rating.getProfileId(),
                rating.getRaterId(),
                rating.getPicRating(),
                rating.getBioRating(),
                rating.getOverAllRating(),
                rating.getCreatedAt()
        );
    }
}