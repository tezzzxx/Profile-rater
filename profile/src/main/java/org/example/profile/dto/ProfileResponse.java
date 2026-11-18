package org.example.profile.dto;

import org.example.profile.model.Profile;

public record ProfileResponse(
        String id,
        int age,
        String city,
        String imageUrl,
        String bio,
        String bioVersion
){
    public static ProfileResponse from (Profile profile){
        return from (profile, "A");
    }

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

