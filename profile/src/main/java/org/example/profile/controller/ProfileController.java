package org.example.profile.controller;

import lombok.RequiredArgsConstructor;
import org.example.profile.dto.CreateProfileRequest;
import org.example.profile.dto.CreateProfileResponse;
import org.example.profile.dto.ProfileResponse;
import org.example.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.profile.dto.ResultsResponse;
import org.example.profile.service.ProfileResultsService;
import lombok.extern.slf4j.Slf4j;

/*
 * REST-endepunktene for profiler:
 * - POST /profiles                     → opprett profil
 * - GET  /profiles/next                → neste profil å vurdere
 * - GET  /profiles/results/{ownerCode} → resultatene for eieren av profilen
 *  * - GET  /profiles/{id}                → hent én profil
 * Tar imot kallet og sender det videre til ProfileService.
 */

@Slf4j
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;
    private final ProfileResultsService resultsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateProfileResponse createProfile(@RequestBody CreateProfileRequest request) {
        return service.createProfile(request);
    }

    @GetMapping("/next")
    public ResponseEntity<ProfileResponse> getNextProfileToRate(@RequestParam String raterId) {
        return service.getNextProfileToRate(raterId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/results/{ownerCode}")
    public ResultsResponse getResults(@PathVariable String ownerCode) {
        return resultsService.getResults(ownerCode);
    }

    @GetMapping("/{id}")
    public ProfileResponse getById(@PathVariable String id) {
        log.info("GET /profiles/{} handled by this instance", id);
        return service.getById(id);
    }


}
