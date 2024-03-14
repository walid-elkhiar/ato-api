package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Demandes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DemandesRepository extends JpaRepository<Demandes,Long> {
}
