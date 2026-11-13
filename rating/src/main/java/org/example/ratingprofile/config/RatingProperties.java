package org.example.ratingprofile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rating")
public class RatingProperties {

    private int maxScore = 5;
}