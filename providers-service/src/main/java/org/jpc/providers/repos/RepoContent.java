/*
 * This file is generate by Joysbright for 3line
 */
package org.jpc.providers.repos;

import org.jpc.providers.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Ayomide Joysbright Oyediran
 */
@Repository
public interface RepoContent extends JpaRepository<Content, String> {
}
