package org.example.ratingprofile.service;

import lombok.RequiredArgsConstructor;
import org.example.ratingprofile.client.ProfileClient;
import org.example.ratingprofile.dto.CommentResponse;
import org.example.ratingprofile.event.RatingCreatedEvent;
import org.example.ratingprofile.event.RatingEventPublisher;
import org.example.ratingprofile.exception.AlreadyRatedException;
import org.example.ratingprofile.exception.ProfileNotFoundException;
import org.example.ratingprofile.model.Rating;
import org.example.ratingprofile.repo.RatingRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepo ratingRepo;
    private final ProfileClient profileClient;
    private final RatingEventPublisher eventPublisher;

    public Rating createRating(Rating rating) {
        if (ratingRepo.existsByProfileIdAndRaterId(rating.getProfileId(), rating.getRaterId())) {
            throw new AlreadyRatedException();
        }

        if (!profileClient.profileExists(rating.getProfileId())) {
            throw new ProfileNotFoundException(rating.getProfileId());
        }

        rating.setRatingId(null);
        rating.setCreatedAt(Instant.now());
        Rating saved = ratingRepo.save(rating);

        eventPublisher.publish(RatingCreatedEvent.from(saved));

        return saved;
    }

    public List<CommentResponse> getCommentsForProfile(String profileId) {
        return ratingRepo.findByProfileIdOrderByCreatedAtDesc(profileId).stream()
                .filter(rating -> rating.getProfileComment() != null
                        && !rating.getProfileComment().isBlank())
                .map(CommentResponse::from)
                .toList();
    }
}
