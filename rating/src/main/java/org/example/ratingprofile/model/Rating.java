package org.example.ratingprofile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("ratings")
public class Rating {
    @Id
    private String id;
    private String raterId;
    private String profileId;
    private int picRating;
    private int bioRating;
    private int overAllRating;
    private String profileComment;
    private LocalDateTime createdAt;
}