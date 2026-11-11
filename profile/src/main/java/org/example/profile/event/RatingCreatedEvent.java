package org.example.profile.event;

import java.time.Instant;

public record RatingCreatedEvent(
        String ratingId,
        String raterId,
        String profileId,
        String bioVersion,
        int picRating,
        int bioRating,
        int overallRating,
        Instant createdAt
) {}