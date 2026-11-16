package org.example.profile.dto;

import java.time.Instant;

public record RatingCommentResponse(
        String comment,
        String bioVersion,
        Instant createdAt
) {}