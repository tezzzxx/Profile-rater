package org.example.ratingprofile.controller;

import lombok.RequiredArgsConstructor;
import org.example.ratingprofile.dto.CommentResponse;
import org.example.ratingprofile.model.Rating;
import org.example.ratingprofile.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rating createRating(@RequestBody Rating rating) {
        return service.createRating(rating);
    }

    @GetMapping("/profile/{profileId}")
    public List<CommentResponse> getCommentsForProfile(@PathVariable String profileId) {
        return service.getCommentsForProfile(profileId);
    }
}

