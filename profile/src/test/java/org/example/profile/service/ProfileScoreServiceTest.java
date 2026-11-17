package org.example.profile.service;

import org.example.profile.TestcontainersConfiguration;
import org.example.profile.dto.CreateProfileRequest;
import org.example.profile.dto.CreateProfileResponse;
import org.example.profile.dto.ResultsResponse;
import org.example.profile.event.RatingCreatedEvent;
import org.example.profile.model.Profile;
import org.example.profile.repo.ProfileRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

// Tester at vurderinger fra Kafka (RatingCreated) oppdaterer poengsummene

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class ProfileScoreServiceTest{

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileScoreService scoreService;

    @Autowired
    private ProfileResultsService resultsService;

    @Autowired
    private ProfileRepo repo;

    @BeforeEach
    void cleanDatabase() {
        repo.deleteAll();
    }

    private CreateProfileResponse createProfile(){
        return profileService.createProfile(
                new CreateProfileRequest(25, "Oslo", null, "Glad i tennis", "Lager lasagne"));
    }

    private RatingCreatedEvent rating(String profileId, String raterId, String bioVersion, int bioScore) {
        return new RatingCreatedEvent("rating-" + raterId, raterId, profileId, bioVersion,
                4, bioScore, 3, Instant.now());
    }

    @Test
    void recordRating_addsScoresAndCountsPerBioVersion() {
        String profileId = createProfile().id();

        scoreService.recordRating(rating(profileId, "person-1", "A", 5));
        scoreService.recordRating(rating(profileId, "person-2", "B", 2));

        Profile profile = repo.findById(profileId).orElseThrow();
        assertThat(profile.getRatingCount()).isEqualTo(2);
        assertThat(profile.getBioScoreSum()).isEqualTo(7);
        assertThat(profile.getBioARatingCount()).isEqualTo(1);
        assertThat(profile.getBioBRatingCount()).isEqualTo(1);
        assertThat(profile.getRatedByRaterIds()).contains("person-1", "person-2");
    }

    @Test
    void recordRating_sameRaterTwice_isOnlyCountedOnce() {
        String profileId = createProfile().id();

        scoreService.recordRating(rating(profileId, "person-1", "A", 5));
        scoreService.recordRating(rating(profileId, "person-1", "A", 5));

        assertThat(repo.findById(profileId).orElseThrow().getRatingCount()).isEqualTo(1);
    }

    @Test
    void recordRating_unknownProfile_isSkippedWithoutError() {
        assertThatCode(() -> scoreService.recordRating(rating("doesnt-exist", "person-1", "A", 5)))
                .doesNotThrowAnyException();
    }

    @Test
    void getResults_calculatesAveragesPerBioVersion() {
        CreateProfileResponse created = createProfile();
        scoreService.recordRating(rating(created.id(), "person-1", "A", 5));
        scoreService.recordRating(rating(created.id(), "person-2", "A", 4));
        scoreService.recordRating(rating(created.id(), "person-3", "B", 2));

        ResultsResponse results = resultsService.getResults(created.ownerCode());

        assertThat(results.ready()).isTrue();
        assertThat(results.ratingCount()).isEqualTo(3);
        assertThat(results.bioTest().a().average()).isEqualTo(4.5);
        assertThat(results.bioTest().b().average()).isEqualTo(2.0);
        assertThat(results.comments()).isEmpty();
    }
}
