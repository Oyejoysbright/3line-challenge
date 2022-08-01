package org.jpc.providers.repos;

import java.util.Optional;
import java.util.UUID;

import org.jpc.providers.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoInstitution extends JpaRepository<Institution, String> {

    Optional<Institution> findByEmailAddressOrPhoneNumber(String emailAddress, String phoneNumber);

    Optional<Institution> findByResetPasswordRef(String ref);

    Optional<Institution> findByResetPasswordToken(String token);
    
}
