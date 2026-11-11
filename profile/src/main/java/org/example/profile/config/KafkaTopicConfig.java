package org.example.profile.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

// oppretter kafka topicene profile bruker, hvis de ikke finnes fra før



@Configuration
public class KafkaTopicConfig {

    public static final String RATING_CREATED_TOPIC = "rating-created";

    //topicet rating sender vurdering til, og profil leser fra.
    @Bean
    public NewTopic ratingCreatedTopic() {
        return TopicBuilder.name(RATING_CREATED_TOPIC)
                .partitions(2) //to profile-instanser kan dele arbeidet
                .replicas(1)       // har bare 1 kafka server, så bare 1 kopi
                .build();
    }
}