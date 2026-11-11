package org.example.profile.controller;

import lombok.RequiredArgsConstructor;
import org.example.profile.dto.CreateProfileRequest;
import org.example.profile.dto.CreateProfileResponse;
import org.example.profile.dto.ProfileResponse;
import org.example.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * REST-endepunktene for profiler:
 * - POST /profiles             → opprett profil
 * - GET  /profiles/next        → neste profil å vurdere
 * - GET  /profiles/{id}        → hent én profil
 * Tar imot kallet og sender det videre til ProfileService.
 */

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

    @GetMapping("/next")
    public ResponseEntity<ProfileResponse> getNextProfileToRate(@RequestParam String raterId){
        return service.getNextProfileToRate(raterId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public ProfileResponse getById(@PathVariable String id){
        return service.getById(id);
    }
}