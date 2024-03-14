package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Traitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TraitementRepository extends JpaRepository<Traitement,Long> {
}
