package org.example.profile.controller;

import lombok.RequiredArgsConstructor;
import org.example.profile.dto.CreateProfileRequest;
import org.example.profile.dto.CreateProfileResponse;
import org.example.profile.dto.ProfileResponse;
import org.example.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateProfileResponse createProfile(@RequestBody CreateProfileRequest request){
        return service.createProfile(request);
    }

    @GetMapping("/{id}")
    public ProfileResponse getById(@PathVariable String id){
        return service.getById(id);
    }
}