package org.example.profile.repo;

import org.example.profile.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepo extends MongoRepository<Profile, String> {

    Optional<Profile> findByOwnerCode(String ownerCode);

    boolean existsByOwnerCode(String ownerCode);
}