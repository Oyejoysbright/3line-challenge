package org.jpc.providers.repos;

import java.util.UUID;

import org.jpc.providers.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoWallet extends JpaRepository<Wallet, UUID> {
    
}
