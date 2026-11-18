package org.example.profile.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// oppretter kafka topicene profile bruker, hvis de ikke finnes fra før

@Configuration
public class KafkaTopicConfig {

    public static final String RATING_CREATED_TOPIC = "rating-created";
    public static final String PROFILE_DELETED_TOPIC = "profile-deleted";

    @Bean
    public NewTopic ratingCreatedTopic() {
        return TopicBuilder.name(RATING_CREATED_TOPIC)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic profileDeletedTopi(){
        return TopicBuilder.name(PROFILE_DELETED_TOPIC)
                .partitions(2)
                .replicas(1)
                .build();
    }
}

