package org.example.profile.dto;

import org.example.profile.model.Profile;

// det API-et sender ut når noen henter en profil
// inneholder bare det andre får se -> ingen eierkode og ingen poeng

public record ProfileResponse(
        String id,
        int age,
        String city,
        String imageUrl,
        String bio,
        String bioVersion
){
    // viser alltid bioRating A (brukes av GET /profiles/{id}
    public static ProfileResponse from (Profile profile){
        return from (profile, "A");
    }

    // viser bioRating A eller B, avhengig av bioVersion (brukes av GET (profiles/next)
    public static ProfileResponse from (Profile profile, String bioVersion){

        String bio = "B".equals(bioVersion) ? profile.getBioB() : profile.getBioA();

        return new ProfileResponse(
                profile.getId(),
                profile.getAge(),
                profile.getCity(),
                profile.getImageUrl(),
                bio,
                bioVersion
        );
    }
}

