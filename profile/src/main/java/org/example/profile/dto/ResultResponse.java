package org.example.profile.dto;

import java.util.List;

//Info får eieren av profilen se på resultatsiden

public record ResultResponse(
        String profileId,
        int ratingCount,
        int minRatingsToShow,
        boolean ready,
        Scores scores, //snitt per kategori
        BioTest bioTest, //snitt per bioversjon A/B
        List<String> comments
){
    public record Scores(Double photos, Double bio, Double overall){}

    public record BioVersionResult(Double average, int count){}

    public record BioTest(BioVersionResult a, BioVersionResult b){}

}
