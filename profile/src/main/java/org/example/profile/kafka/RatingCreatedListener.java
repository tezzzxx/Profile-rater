package org.example.profile.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.profile.event.RatingCreatedEvent;
import org.example.profile.service.ProfileScoreService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.example.profile.config.KafkaTopicConfig;


// lytter på rating-created topic og oppdaterer profilen med ny score
// rating sender en melding hit hver gang noen har vurdert en profil

@Slf4j
@Component
@RequiredArgsConstructor

public class RatingCreatedListener {

    private final ProfileScoreService profileScoreService;
    private final ObjectMapper objectMapper; //gjør JSON-tekst om til javaobjekter

    // Alle profile-instanser er i gruppen "profile".
    // Kafka gir hver melding til bare én av dem, så samme vurdering ikke telles to ganger

    @KafkaListener(topics = KafkaTopicConfig.RATING_CREATED_TOPIC, groupId = "profile")
    public void handleRatingCreated(String messageJson) {
        try {
            RatingCreatedEvent event = objectMapper.readValue(messageJson, RatingCreatedEvent.class);
            log.info("Received RatingCreatedEvent: {}", event);
            profileScoreService.recordRating(event);

        } catch (JacksonException e) {
            log.error("Failed to read  RatingCreated-message: {}", messageJson, e);
        }
    }
}