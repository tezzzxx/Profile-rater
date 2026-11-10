package org.example.ratingprofile.service;

import lombok.RequiredArgsConstructor;
import org.example.ratingprofile.exception.AlreadyRatedException;
import org.example.ratingprofile.model.Rating;
import org.example.ratingprofile.repo.RatingRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepo ratingRepo;

    public Rating createRating(Rating rating) {
        if (ratingRepo.existsByProfileIdAndRaterId(rating.getProfileId(), rating.getRaterId())) {
            throw new AlreadyRatedException();
        }

        rating.setId(null);
        rating.setCreatedAt(LocalDateTime.now());
        return ratingRepo.save(rating);
    }
}
