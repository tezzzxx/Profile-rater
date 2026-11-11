package org.example.profile.service;

import lombok.RequiredArgsConstructor;
import org.example.profile.dto.CreateProfileRequest;
import org.example.profile.dto.CreateProfileResponse;
import org.example.profile.dto.ProfileResponse;
import org.example.profile.model.Profile;
import org.example.profile.repo.ProfileRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

 // oppretter profil, henter en profil, velger neste profil en nettleser skal vurdere (MED a/B test av bioRating)

@Service
@RequiredArgsConstructor
public class ProfileService {

    private static final String OWNER_CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ProfileRepo repo;

    public CreateProfileResponse createProfile(CreateProfileRequest request) {
        if (request.age() == null || request.age() < 18) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age must be 18 or above");
        }
        if (request.bioA() == null || request.bioA().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bio A can not be empty");
        }

        Profile profile = new Profile();
        profile.setOwnerCode(newOwnerCode());
        profile.setAge(request.age());
        profile.setCity(request.city());
        profile.setImageUrl(request.imageUrl());
        profile.setBioA(request.bioA().trim());
        profile.setBioB(blankToNull(request.bioB()));
        profile.setCreatedAt(Instant.now());

        Profile saved = repo.save(profile);
        return new CreateProfileResponse(saved.getId(), saved.getOwnerCode());
    }

    public ProfileResponse getById(String id){
        return repo.findById(id)
                .map(ProfileResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }

    public Optional<ProfileResponse> getNextProfileToRate(String raterId) {

        List<Profile> notYetRated = repo.findByRatedByRaterIdsNotContaining(raterId);
        if (notYetRated.isEmpty()) {
            return Optional.empty();
        }

        Profile chosenProfile = notYetRated.get(RANDOM.nextInt(notYetRated.size()));

        String bioVersion = chooseBioVersion(chosenProfile);
        return Optional.of(ProfileResponse.from(chosenProfile, bioVersion));
    }

    private String chooseBioVersion(Profile profile){
        if (profile.getBioB() == null) {
            return "A";
        }
        return RANDOM.nextBoolean() ? "A" : "B";
    }

    private String newOwnerCode(){
        String code;
        do {
            code = randomChars(3) + "-" + randomChars(4);
        } while (repo.existsByOwnerCode(code));
        return code;
    }

    private String randomChars(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(OWNER_CODE_CHARS.charAt(RANDOM.nextInt(OWNER_CODE_CHARS.length())));
        }
        return sb.toString();
    }


    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}

