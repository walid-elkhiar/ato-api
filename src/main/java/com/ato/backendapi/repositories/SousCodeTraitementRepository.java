package com.ato.backendapi.repositories;

import com.ato.backendapi.dto.ParametrageDemandeDTO;
import com.ato.backendapi.entities.SousCodeTraitement;
import com.ato.backendapi.entities.TypeScTraitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SousCodeTraitementRepository extends JpaRepository<SousCodeTraitement,Long> {
    List<SousCodeTraitement> findSousCodeTraitementsByProfil_IdProfil(Long profilId);
    List<SousCodeTraitement> findByType(TypeScTraitement type);
    //Optional<SousCodeTraitement> findByType(String typeScTraitement);

    List<SousCodeTraitement> findByTypeNot(TypeScTraitement type);
    @Query("SELECT new com.ato.backendapi.dto.ParametrageDemandeDTO(sct.idSousCodeTraitement, sct.code, sct.libelle,sct.type,sct.absence, p.idProfil, p.designation) " +
            "FROM SousCodeTraitement sct JOIN sct.profil p")
    List<ParametrageDemandeDTO> findAllParametrageDemandes();
}
