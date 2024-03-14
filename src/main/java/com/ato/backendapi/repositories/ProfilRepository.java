package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfilRepository extends JpaRepository<Profil,Long> {
    List<Profil> findProfilsBySousCodeTraitement_IdSousCodeTraitement(Long sousCodeTraitementId);
    Optional<Profil> findByDesignation(String designation);
}
