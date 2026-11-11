package org.example.ratingprofile.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.ratingprofile.event.RatingEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic ratingCreatedTopic() {
        return TopicBuilder.name(RatingEventPublisher.TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}