package org.jpc.providers.repos;

import java.util.Optional;
import java.util.UUID;
import org.jpc.providers.entities.ContentCreator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoContentCreator extends JpaRepository<ContentCreator, String> {

    Optional<ContentCreator> findByEmailAddressOrPhoneNumber(String emailAddress, String phoneNumber);

    Optional<ContentCreator> findByResetPasswordRef(String ref);

    Optional<ContentCreator> findByResetPasswordToken(String token);
    
}
