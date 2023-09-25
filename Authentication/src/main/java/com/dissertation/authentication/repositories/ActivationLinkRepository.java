package com.dissertation.authentication.repositories;

import com.dissertation.authentication.entities.ActivationLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationLinkRepository extends JpaRepository<ActivationLink, Long> {

    Optional<ActivationLink> findByLink(String link);

}
