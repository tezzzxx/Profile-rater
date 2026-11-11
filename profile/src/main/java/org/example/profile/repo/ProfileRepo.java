package org.example.profile.repo;

import org.example.profile.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

// Snakker med MongoDB samlingen "profiles"
public interface ProfileRepo extends MongoRepository<Profile, String> {

    Optional<Profile> findByOwnerCode(String ownerCode);

    boolean existsByOwnerCode(String ownerCode);

    // finn profiler der ratedByRaterIds IKKE inneholder raterId
    // = profiler denne nettleseren ikke har vurdert ennå
    List<Profile> findByRatedByRaterIdsNotContaining(String raterId);
}