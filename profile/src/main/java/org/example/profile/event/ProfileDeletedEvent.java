package org.example.profile.event;

import java.time.Instant;

// Meldingen profile sender til kafka når en profil slettes
// rating bruker den til å slette alle vurderinger av profilen

public record ProfileDeletedEvent(
        String profileId,
        Instant deletedAt
){}
