package org.example.profile.service;

import lombok.RequiredArgsConstructor;
import org.example.profile.client.RatingClient;
import org.example.profile.dto.RatingCommentResponse;
import org.example.profile.dto.ResultsResponse;
import org.example.profile.model.Profile;
import org.example.profile.repo.ProfileRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//Lager resultatsiden for en profil = finner profilen med eierkoden, regner ut snitt fra summene profile har laget, henter kommentarer fra rating

@Service
@RequiredArgsConstructor
public class ProfileResultsService{

    private final ProfileRepo repo;
    private final RatingClient ratingClient;

    // hvor mange vurderinger som trengs før tallene vises
    @Value("${profile.min-ratings:5}")
    private int minRatingsToShow;

    public ResultsResponse getResults(String ownerCode){
        Profile profile = repo.findByOwnerCode(ownerCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No profile found with that owner code"));

        boolean ready = profile.getRatingCount() >= minRatingsToShow;

        if(!ready){
            return new ResultsResponse(profile.getId(), profile.getRatingCount(), minRatingsToShow,
                    false, null, null, List.of());
        }

        //snitt per kategori
        ResultsResponse.Scores scores = new ResultsResponse.Scores(
                average(profile.getPhotosScoreSum(), profile.getRatingCount()),
                average(profile.getBioScoreSum(), profile.getRatingCount()),
                average(profile.getOverallScoreSum(), profile.getRatingCount())
        );

        //snitt per bio-versjon
        ResultsResponse.BioTest bioTest = new ResultsResponse.BioTest(
                new ResultsResponse.BioVersionResult(
                        average(profile.getBioAScoreSum(), profile.getBioARatingCount()),
                        profile.getBioARatingCount()),
                new ResultsResponse.BioVersionResult(
                        average(profile.getBioBScoreSum(), profile.getBioBRatingCount()),
                        profile.getBioBRatingCount())
        );
        //henter kommentarer fra rating + fjerner tomme
        List<String> comments = ratingClient.getCommentsForProfile(profile.getId()).stream()
                .map(RatingCommentResponse::comment)
                .filter(comment -> comment != null && !comment.isBlank())
                .toList();

        return new ResultsResponse(profile.getId(), profile.getRatingCount(), minRatingsToShow,
                true, scores, bioTest, comments);
    }

    //snitt med en desimal eller null hvis ingen vurdert
    private Double average(int sum, int count){
        if (count == 0){
            return null;
        }
        return Math.round((double) sum / count * 10) / 10.0;
    }

}