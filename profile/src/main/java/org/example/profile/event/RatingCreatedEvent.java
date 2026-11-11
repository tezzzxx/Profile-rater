package org.example.profile.event;

import java.time.Instant;

public record RatingCreatedEvent(
        String ratingId,
        String raterId,
        String profileId,
        String bioVersion,
        int photos,
        int bio,
        int overall,
        Instant createdAt
) {}