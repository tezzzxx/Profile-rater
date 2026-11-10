package org.example.ratingprofile.repo;

import org.example.ratingprofile.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RatingRepo extends MongoRepository <Rating, String>{
    boolean existsByProfileIdAndRaterId(String profileId, String raterId);
}