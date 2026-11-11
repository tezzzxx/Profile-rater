package org.example.ratingprofile.event;

import org.example.ratingprofile.model.Rating;
import java.time.Instant;

public record RatingCreatedEvent(
        String ratingId,
        String profileId,
        String raterId,
        String bioVersion,
        int picRating,
        int bioRating,
        int overallRating,
        Instant createdAt
) {
    public static RatingCreatedEvent from(Rating rating) {
        return new RatingCreatedEvent(
                rating.getRatingId(),
                rating.getProfileId(),
                rating.getRaterId(),
                rating.getBioVersion(),
                rating.getPicRating(),
                rating.getBioRating(),
                rating.getOverallRating(),
                rating.getCreatedAt()
        );
    }
}