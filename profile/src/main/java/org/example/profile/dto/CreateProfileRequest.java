package org.example.profile.dto;

public record CreateProfileRequest(
        Integer age,
        String city,
        String imageUrl,
        String bioA,
        String bioB
){}