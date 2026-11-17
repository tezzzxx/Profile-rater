package org.example.profile.service;

import org.example.profile.TestcontainersConfiguration;
import org.example.profile.dto.CreateProfileRequest;
import org.example.profile.dto.CreateProfileResponse;
import org.example.profile.dto.ProfileResponse;
import org.example.profile.exception.InvalidProfileException;
import org.example.profile.exception.ProfileNotFoundException;
import org.example.profile.repo.ProfileRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Tester opprettelse, henting, sletting, validering og A/B-valg av bio

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class ProfileServiceTest{

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepo repo;

    @BeforeEach
    void cleanDatabase(){
        repo.deleteAll();
    }

    private CreateProfileRequest validRequest() {
        return new CreateProfileRequest(25, "Oslo", null, "Glad i tennis", "Lager verdens beste lasagne");
    }

    /* ------------------------------------------ CREATE  -------------------------------------------- */

    @Test
    void createProfile_savesProfileAndGivesOwnerCode() {
        CreateProfileResponse created = profileService.createProfile(validRequest());

        assertThat(created.ownerCode()).matches("[A-Z2-9]{3}-[A-Z2-9]{4}");
        assertThat(repo.findById(created.id())).isPresent();
    }

    @Test
    void createProfile_under18_isRejected() {
        CreateProfileRequest tooYoung = new CreateProfileRequest(17, "Oslo", null, "Bio", null);

        assertThatThrownBy(() -> profileService.createProfile(tooYoung))
                .isInstanceOf(InvalidProfileException.class)
                .hasMessage("Age must be 18 or above");
    }

    @Test
    void createProfile_numbersInCity_isRejected() {
        CreateProfileRequest badCity = new CreateProfileRequest(25, "Oslo123", null, "Bio", null);

        assertThatThrownBy(() -> profileService.createProfile(badCity))
                .isInstanceOf(InvalidProfileException.class)
                .hasMessage("City can only contain letters");
    }

    @Test
    void createProfile_withoutBioA_isRejected() {
        CreateProfileRequest noBio = new CreateProfileRequest(25, "Oslo", null, "   ", null);

        assertThatThrownBy(() -> profileService.createProfile(noBio))
                .isInstanceOf(InvalidProfileException.class);
    }

    /* ------------------------------------------ FETCH & DELETE  ----------------------------------------- */

    @Test
    void getById_unknownId_throwsNotFound() {
        assertThatThrownBy(() -> profileService.getById("not-found"))
                .isInstanceOf(ProfileNotFoundException.class);
    }

    @Test
    void deleteByOwnerCode_removesProfile() {
        CreateProfileResponse created = profileService.createProfile(validRequest());

        profileService.deleteByOwnerCode(created.ownerCode());

        assertThat(repo.findById(created.id())).isEmpty();
    }

    @Test
    void deleteByOwnerCode_wrongCode_throwsNotFound() {
        assertThatThrownBy(() -> profileService.deleteByOwnerCode("WRONG-CODE"))
                .isInstanceOf(ProfileNotFoundException.class);
    }

    /* ---------------------------------------- NEXT PROFILE & A/B TEST  ----------------------------------------- */

    @Test
    void getNextProfileToRate_noProfiles_returnsEmpty() {
        assertThat(profileService.getNextProfileToRate("new-browser")).isEmpty();
    }

    @Test
    void getNextProfileToRate_withoutBioB_alwaysShowsA() {
        profileService.createProfile(new CreateProfileRequest(25, "Oslo", null, "Only bio A", null));

        for (int i = 0; i < 20; i++) {
            Optional<ProfileResponse> next = profileService.getNextProfileToRate("browser-" + i);
            assertThat(next).isPresent();
            assertThat(next.get().bioVersion()).isEqualTo("A");
        }
    }

    @Test
    void getNextProfileToRate_withBioB_showsBothVersions() {
        profileService.createProfile(validRequest());

        Set<String> versionsSeen = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            versionsSeen.add(profileService.getNextProfileToRate("browser-" + i).get().bioVersion());
        }
        assertThat(versionsSeen).containsExactlyInAnyOrder("A", "B");
    }
}