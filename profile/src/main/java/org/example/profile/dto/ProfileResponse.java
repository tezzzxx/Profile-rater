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
    public static ProfileResponse from (Profile p){
        return new ProfileResponse(p.getId(), p.getAge(),p.getCity(),p.getImageUrl(), p.getBioA(), "A");
    }
}

