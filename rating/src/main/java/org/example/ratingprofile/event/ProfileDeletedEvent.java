package org.example.ratingprofile.event;

import java.time.Instant;

public record ProfileDeletedEvent(
        String profileId,
        Instant deletedAt
) {}