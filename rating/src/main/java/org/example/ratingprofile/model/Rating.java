package org.example.ratingprofile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("ratings")
public class Rating {
    @Id
    private String ratingId;
    private String raterId;
    private String profileId;
    private String bioVersion;
    private int picRating;
    private int bioRating;
    private int overallRating;
    private String profileComment;
    private Instant createdAt;
}