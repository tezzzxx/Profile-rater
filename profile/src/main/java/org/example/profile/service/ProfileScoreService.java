package  org.example.profile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import org.example.profile.event.RatingCreatedEvent;
import org.example.profile.model.Profile;
import org.example.profile.repo.ProfileRepo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileScoreService {

    private final ProfileRepo repo;

    public void recordRating(RatingCreatedEvent event) {

        // finner profilen og sjekker om den allerede et rated
        Optional<Profile> foundProfile = repo.findById(event.profileId());
        if (foundProfile.isEmpty()){
            log.warn("No profile found for profileId: {}", event.profileId());
            return;
        }
        Profile profile = foundProfile.get();

        if(profile.getRatedByRaterIds().contains(event.raterId())){
            log.info("Profile {} has already been rated by rater {}", event.profileId(), event.raterId());
            return;
        }

        // legger til poeng og teller for bioA eller bioB og overallRating
        profile.setRatingCount(profile.getRatingCount() + 1);
        profile.setPhotosScoreSum(profile.getPhotosScoreSum() + event.picRating());
        profile.setBioScoreSum(profile.getBioScoreSum() + event.bioRating());
        profile.setOverallScoreSum(profile.getOverallScoreSum() + event.overallRating());

        if ("B".equals(event.bioVersion())) {
            profile.setBioBScoreSum(profile.getBioBScoreSum() + event.bioRating());
            profile.setBioBRatingCount(profile.getBioBRatingCount() + 1);
        } else {
            profile.setBioAScoreSum(profile.getBioAScoreSum() + event.bioRating());
            profile.setBioARatingCount(profile.getBioARatingCount() + 1);
        }

        profile.getRatedByRaterIds().add(event.raterId());

        repo.save(profile);
        log.info("Added rating for profile {} by rater {}. New rating count: {}", event.profileId(), event.raterId(), profile.getRatingCount());
    }




}