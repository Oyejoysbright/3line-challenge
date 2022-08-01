package org.jpc.users.repos;

import java.util.Optional;

import org.jpc.users.entities.TransactionHistory;
import org.jpc.tool.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoTransactionHistory extends JpaRepository<TransactionHistory, String> {

    Optional<TransactionHistory> findByRef(String ref);
    Page<TransactionHistory> getByCustomerIdAndType(String customerId, TransactionType type, Pageable pageable);
    Page<TransactionHistory> getByCustomerId(String customerId, Pageable pageable);
    
}
