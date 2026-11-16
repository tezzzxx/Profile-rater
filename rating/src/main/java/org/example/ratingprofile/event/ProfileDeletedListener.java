package org.example.ratingprofile.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ratingprofile.service.RatingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileDeletedListener {

    public static final String TOPIC = "profile-deleted";

    private final RatingService ratingService;
    private final JsonMapper jsonMapper;

    @KafkaListener(topics = TOPIC, groupId = "rating")
    public void handleProfileDeleted(String messageJson) {
        try {
            ProfileDeletedEvent event = jsonMapper.readValue(messageJson, ProfileDeletedEvent.class);
            long deleted = ratingService.deleteRatingsForProfile(event.profileId());
            log.info("Deleted {} ratings for deleted profile {}", deleted, event.profileId());
        } catch (JacksonException e) {
            log.error("Could not read ProfileDeleted message: {}", messageJson, e);
        }
    }
}