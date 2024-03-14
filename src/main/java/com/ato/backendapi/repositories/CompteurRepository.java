package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Compteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CompteurRepository extends JpaRepository<Compteur,Long> {
}
