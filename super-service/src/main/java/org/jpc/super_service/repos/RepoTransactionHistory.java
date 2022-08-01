/*
 * This file is generate by Joysbright for 3line
 */
package org.jpc.super_service.repos;

import org.jpc.super_service.entities.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Ayomide Joysbright Oyediran
 */
@Repository
public interface RepoTransactionHistory extends JpaRepository<TransactionHistory, String> {
    
}
