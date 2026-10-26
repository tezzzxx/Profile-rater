package org.example.matchingprofile;

import org.springframework.boot.SpringApplication;

public class TestMatchingProfileApplication {

    public static void main(String[] args) {
        SpringApplication.from(MatchingProfileApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
