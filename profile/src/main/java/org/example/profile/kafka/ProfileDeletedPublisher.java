package org.example.profile.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.profile.config.KafkaTopicConfig;
import org.example.profile.event.ProfileDeletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

// sender ProfilDeleted til kafka når en profil er slettet
// meldingen sendes som JSON-test med profilId som nøkkel

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileDeletedPublisher{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishProfileDeleted(String profileId){
        ProfileDeletedEvent event = new ProfileDeletedEvent(profileId, Instant.now());
        String messageJson = objectMapper.writeValueAsString(event);

        kafkaTemplate.send(KafkaTopicConfig.PROFILE_DELETED_TOPIC, profileId, messageJson);
        log.info("Sent profileDeleted for profile {}", profileId);
    }
}
