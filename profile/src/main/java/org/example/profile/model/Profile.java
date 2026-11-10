package org.example.profile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "profiles")
public class Profile {

    @Id
    private String id;
    private String ownerCode;
    private int age;
    private String city;
    private String imageUrl;
    private String bioA;
    private String bioB;
    private Instant createdAt;

    private int ratingCount;

    private int photosScoreSum;
    private int bioScoreSum;
    private int overallScoreSum;

    private int bioAScoreSum;
    private int bioARatingCount;
    private int bioBScoreSum;
    private int bioBRatingCount;

    private Set<String> ratedByRatersIds = new HashSet<>();
}