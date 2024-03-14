package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.SousCodeTraitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SousCodeTraitementRepository extends JpaRepository<SousCodeTraitement,Long> {
    List<SousCodeTraitement> findSousCodeTraitementsByProfil_IdProfil(Long profilId);
}
