package org.example.profile.dto;

import java.util.List;

public record ResultsResponse(
        String profileId,
        int ratingCount,
        int minRatingsToShow,
        boolean ready,
        Scores scores,
        BioTest bioTest,
        List<String> comments
){
    public record Scores(Double photos, Double bio, Double overall){}

    public record BioVersionResult(Double average, int count){}

    public record BioTest(BioVersionResult a, BioVersionResult b){}

}
