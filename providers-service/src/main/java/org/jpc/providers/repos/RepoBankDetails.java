package org.jpc.providers.repos;

import java.util.Optional;

import org.jpc.providers.entities.BankDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoBankDetails extends JpaRepository<BankDetail, String> {

    Optional<BankDetail> findByCustomerIdAndAccountNumber(String id, String accountNumber);
    
}
