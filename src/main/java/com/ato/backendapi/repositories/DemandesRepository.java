package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Demandes;
import com.ato.backendapi.entities.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandesRepository extends JpaRepository<Demandes,Long> {

        @Query("SELECT d FROM Demandes d " +
                "JOIN Validation v ON d.idDemande = v.demandes.idDemande " +
                "WHERE v.validateur.idValidateur = :validateurId " +
                "AND d.etatValidation = :etatValidation")
        List<Demandes> findDemandesByValidateurAndEtat(@Param("validateurId") Long validateurId,
                                                       @Param("etatValidation") String etatValidation);
        List<Demandes> findByUtilisateurs_Profil_IdProfil(Long idProfil);

        // Récupérer les demandes validées par un validateur spécifique
        List<Demandes> findByUtilisateurs(Utilisateurs validateur);

        // Récupérer les demandes créées par un utilisateur spécifique
        List<Demandes> findByUtilisateurSaisiDemande(Utilisateurs utilisateur);
}