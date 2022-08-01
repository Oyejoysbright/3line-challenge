package org.jpc.providers.repos;


import org.jpc.providers.entities.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoTransactionHistory extends JpaRepository<TransactionHistory, String> {
    
}
