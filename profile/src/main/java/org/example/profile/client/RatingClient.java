package org.example.profile.client;

import lombok.extern.slf4j.Slf4j;
import org.example.profile.dto.RatingCommentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

//henter kommentarer som er skrevet om en profil

@Slf4j
@Component
public class RatingClient {

    private final RestClient restClient;

    //rating.base-url = http://rating (navnet i Consul)
    public RatingClient(RestClient.Builder builder, @Value("${rating.base-url}") String ratingBaseUrl) {
        this.restClient = builder.baseUrl(ratingBaseUrl).build();
    }

    //henter kommentarer --> hvis ikke rating svarer så får vi en tom liste og ikke en feil
    public List<RatingCommentResponse> getCommentsForProfile(String profileId){
        try{
            List<RatingCommentResponse> comments = restClient.get()
                    .uri("/ratings/profile/{profileId}", profileId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<RatingCommentResponse>>(){});
            return comments != null ? comments : List.of();
        }catch (RestClientException | IllegalStateException e){
            log.warn("Could not fetch comments from rating for profile {}: {}", profileId, e.getMessage());
            return List.of();
        }
    }
}

