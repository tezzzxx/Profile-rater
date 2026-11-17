package org.example.ratingprofile;

import org.example.ratingprofile.client.ProfileClient;
import org.example.ratingprofile.dto.CommentResponse;
import org.example.ratingprofile.exception.AlreadyRatedException;
import org.example.ratingprofile.exception.InvalidRatingException;
import org.example.ratingprofile.exception.ProfileNotFoundException;
import org.example.ratingprofile.model.Rating;
import org.example.ratingprofile.repo.RatingRepo;
import org.example.ratingprofile.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class RatingServiceTest {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingRepo ratingRepo;

    @MockitoBean
    private ProfileClient profileClient;

    @BeforeEach
    void setUp() {
        ratingRepo.deleteAll();
        when(profileClient.profileExists(anyString())).thenReturn(true);
    }

    private Rating rating(String profileId, String raterId, int score, String comment) {
        Rating rating = new Rating();
        rating.setProfileId(profileId);
        rating.setRaterId(raterId);
        rating.setBioVersion("A");
        rating.setPicRating(score);
        rating.setBioRating(score);
        rating.setOverallRating(score);
        rating.setProfileComment(comment);
        return rating;
    }

    @Test
    void validRatingIsSavedWithIdAndTimestamp() {
        Rating saved = ratingService.createRating(rating("profile-1", "rater-1", 4, "Fint bilde"));

        assertThat(saved.getRatingId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(ratingRepo.count()).isEqualTo(1);
    }

    @Test
    void sameRaterCannotRateSameProfileTwice() {
        ratingService.createRating(rating("profile-1", "rater-1", 4, null));

        assertThatThrownBy(() -> ratingService.createRating(rating("profile-1", "rater-1", 3, null)))
                .isInstanceOf(AlreadyRatedException.class);
        assertThat(ratingRepo.count()).isEqualTo(1);
    }

    @Test
    void scoreOutsideRangeIsRejected() {
        assertThatThrownBy(() -> ratingService.createRating(rating("profile-1", "rater-1", 6, null)))
                .isInstanceOf(InvalidRatingException.class)
                .hasMessageContaining("between 1 and 5");
        assertThat(ratingRepo.count()).isZero();
    }

    @Test
    void ratingOfUnknownProfileIsRejected() {
        when(profileClient.profileExists("missing")).thenReturn(false);

        assertThatThrownBy(() -> ratingService.createRating(rating("missing", "rater-1", 4, null)))
                .isInstanceOf(ProfileNotFoundException.class);
        assertThat(ratingRepo.count()).isZero();
    }

    @Test
    void commentsAreNewestFirstAndEmptyCommentsAreSkipped() throws InterruptedException {
        ratingService.createRating(rating("profile-1", "rater-1", 4, "Første"));
        Thread.sleep(10);
        ratingService.createRating(rating("profile-1", "rater-2", 3, ""));
        Thread.sleep(10);
        ratingService.createRating(rating("profile-1", "rater-3", 5, "Siste"));

        List<CommentResponse> comments = ratingService.getCommentsForProfile("profile-1");

        assertThat(comments)
                .extracting(CommentResponse::comment)
                .containsExactly("Siste", "Første");
    }

    @Test
    void deletingProfileRemovesOnlyItsRatings() {
        ratingService.createRating(rating("profile-1", "rater-1", 4, null));
        ratingService.createRating(rating("profile-1", "rater-2", 3, null));
        ratingService.createRating(rating("profile-2", "rater-1", 5, null));

        long deleted = ratingService.deleteRatingsForProfile("profile-1");

        assertThat(deleted).isEqualTo(2);
        assertThat(ratingRepo.count()).isEqualTo(1);
    }
}