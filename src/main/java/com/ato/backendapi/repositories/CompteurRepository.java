package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Compteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteurRepository extends JpaRepository<Compteur,Long> {
    List<Compteur> findByUtilisateurs_IdUtilisateur(Long idUtilisateur);
}
