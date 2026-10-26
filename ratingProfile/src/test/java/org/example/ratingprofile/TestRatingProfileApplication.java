package org.example.ratingprofile;

import org.springframework.boot.SpringApplication;

public class TestRatingProfileApplication {

    public static void main(String[] args) {
        SpringApplication.from(RatingProfileApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
