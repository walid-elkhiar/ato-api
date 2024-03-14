package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SocieteRepository extends JpaRepository<Societe,Long> {
}
