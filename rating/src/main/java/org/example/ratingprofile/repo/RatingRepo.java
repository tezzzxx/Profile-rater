package org.example.ratingprofile.repo;

import org.example.ratingprofile.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingRepo extends MongoRepository <Rating, String>{
    boolean existsByProfileIdAndRaterId(String profileId, String raterId);

    List<Rating> findByProfileIdOrderByCreatedAtDesc(String profileId);
}