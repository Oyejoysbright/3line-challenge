package org.jpc.users.repos;

import java.util.Optional;

import org.jpc.users.entities.PaymentSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoPaymentSource extends JpaRepository<PaymentSource, String> {

    Optional<PaymentSource> findByNameOrValue(String psName, String psValue);

    Optional<PaymentSource> findByCustomerIdAndName(String id, String maskCard);
    
}
