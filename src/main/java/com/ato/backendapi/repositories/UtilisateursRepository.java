package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Departements;
import com.ato.backendapi.entities.Utilisateurs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateursRepository extends JpaRepository<Utilisateurs, Long> {

    // Méthodes existantes pour la pagination
    @RestResource(path = "findAllByDepartements_IdDepartement_paginated", rel = "findAllByDepartements_IdDepartement_paginated")
    Page<Utilisateurs> findAllByDepartements_IdDepartement(Long idDepartement, Pageable pageable);

    @RestResource(path = "findAllByProfil_IdProfil_paginated", rel = "findAllByProfil_IdProfil_paginated")
    Page<Utilisateurs> findAllByProfil_IdProfil(Long idProfil, Pageable pageable);

    // Méthode pour trouver les utilisateurs par contrat avec pagination
    @Query("SELECT u FROM Utilisateurs u WHERE u.contrats.idContrat = :idContrat")
    @RestResource(path = "findAllByContrats_IdContrat_paginated", rel = "findAllByContrats_IdContrat_paginated")
    Page<Utilisateurs> findPaginatedByContrats_IdContrat(@Param("idContrat") Long idContrat, Pageable pageable);

    @RestResource(path = "findAllByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil_paginated", rel = "findAllByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil_paginated")
    Page<Utilisateurs> findAllByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil(
            Long idDepartement, Long idContrat, Long idProfil, Pageable pageable);

    @RestResource(path = "findAllByDepartements_IdDepartementAndContrats_IdContrat_paginated", rel = "findAllByDepartements_IdDepartementAndContrats_IdContrat_paginated")
    Page<Utilisateurs> findAllByDepartements_IdDepartementAndContrats_IdContrat(
            Long idDepartement, Long idContrat, Pageable pageable);

    @RestResource(path = "findAllByDepartements_IdDepartementAndProfil_IdProfil_paginated", rel = "findAllByDepartements_IdDepartementAndProfil_IdProfil_paginated")
    Page<Utilisateurs> findAllByDepartements_IdDepartementAndProfil_IdProfil(
            Long idDepartement, Long idProfil, Pageable pageable);

    @RestResource(path = "findAllByContrats_IdContratAndProfil_IdProfil_paginated", rel = "findAllByContrats_IdContratAndProfil_IdProfil_paginated")
    Page<Utilisateurs> findAllByContrats_IdContratAndProfil_IdProfil(
            Long idContrat, Long idProfil, Pageable pageable);

    // Méthode pour trouver les utilisateurs par contrat sans pagination
    @Query("SELECT u FROM Utilisateurs u WHERE u.contrats.idContrat = :idContrat")
    @RestResource(path = "findAllByContrats_IdContrat_list", rel = "findAllByContrats_IdContrat_list")
    List<Utilisateurs> findListByContrats_IdContrat(@Param("idContrat") Long idContrat);

    @RestResource(path = "findListByDepartements_IdDepartementAndContrats_IdContrat", rel = "findListByDepartements_IdDepartementAndContrats_IdContrat")
    List<Utilisateurs> findListByDepartements_IdDepartementAndContrats_IdContrat(Long idDepartement, Long idContrat);

    @RestResource(path = "findListByDepartements_IdDepartementAndProfil_IdProfil", rel = "findListByDepartements_IdDepartementAndProfil_IdProfil")
    List<Utilisateurs> findListByDepartements_IdDepartementAndProfil_IdProfil(Long idDepartement, Long idProfil);

    @RestResource(path = "findListByContrats_IdContratAndProfil_IdProfil", rel = "findListByContrats_IdContratAndProfil_IdProfil")
    List<Utilisateurs> findListByContrats_IdContratAndProfil_IdProfil(Long idContrat, Long idProfil);

    @RestResource(path = "findListByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil", rel = "findListByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil")
    List<Utilisateurs> findListByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil(Long idDepartement, Long idContrat, Long idProfil);

    @RestResource(path = "findListByDepartements_IdDepartement", rel = "findListByDepartements_IdDepartement")
    List<Utilisateurs> findByDepartements_IdDepartement(Long idDepartement);

    @RestResource(path = "findListByProfil_IdProfil", rel = "findListByProfil_IdProfil")
    List<Utilisateurs> findAllByProfil_IdProfil(Long idProfil);

    Utilisateurs findByAdresseMail(String email);
    Utilisateurs findByMatricule(String matricule);
    Utilisateurs findByAdresseMailAndMatricule(String email, String matricule);
    int countByDepartements(Departements departement);

    @Query(value = "SELECT * FROM utilisateurs u " +
            "JOIN validateur v ON u.id_utilisateur = v.utilisateurs_id " +
            "WHERE v.validateur = :validateurId AND v.niveau = :niveau", nativeQuery = true)
    List<Utilisateurs> getUsersByValidator(@Param("validateurId") Long validateurId, @Param("niveau") int niveau);
}


