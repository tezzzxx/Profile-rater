package org.example.profile.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//Instillinger for profile so kan endres sentral i Consul KV
//Leser alle properties som starter med profile
//Oppdateres automatisk når verdien endres i Consul

@Data
@Component
@ConfigurationProperties(prefix = "profile")
public class ProfileProperties{

    private int minRatings = 5;
}