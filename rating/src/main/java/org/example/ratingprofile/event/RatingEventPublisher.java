package org.example.ratingprofile.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingEventPublisher {

    public static final String TOPIC = "rating-created";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public void publish(RatingCreatedEvent event) {
        String json = jsonMapper.writeValueAsString(event);

        kafkaTemplate.send(TOPIC, event.profileId(), json)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Could not send RatingCreated for rating {}", event.ratingId(), error);
                    } else {
                        log.info("Sent RatingCreated for rating {} to {}", event.ratingId(), TOPIC);
                    }
                });
    }
}