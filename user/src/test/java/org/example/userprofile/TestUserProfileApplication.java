package org.example.userprofile;

import org.springframework.boot.SpringApplication;

public class TestUserProfileApplication {

	public static void main(String[] args) {
		SpringApplication.from(UserProfileApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
