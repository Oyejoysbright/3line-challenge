package org.jpc.users.repos;

import java.util.Optional;
import java.util.UUID;

import org.jpc.users.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoCustomer extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByEmailAddressOrPhoneNumber(String emailAddress, String phoneNumber);

    Optional<Customer> findByResetPasswordRef(String ref);

    Optional<Customer> findByResetPasswordToken(String token);
    
}
