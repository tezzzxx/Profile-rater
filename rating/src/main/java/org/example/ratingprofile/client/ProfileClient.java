package org.example.ratingprofile.client;

import org.example.ratingprofile.exception.ProfileServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ProfileClient {

    private final RestClient restClient;

    public ProfileClient(RestClient.Builder builder,
                         @Value("${profile.base-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public boolean profileExists(String profileId) {
        try {
            restClient.get()
                    .uri("/profiles/{id}", profileId)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (RestClientException e) {
            throw new ProfileServiceUnavailableException();
        }
    }
}