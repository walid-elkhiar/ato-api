package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Compteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteurRepository extends JpaRepository<Compteur,Long> {
    // Méthode qui retourne une liste de compteurs pour un utilisateur
    List<Compteur> findByUtilisateurs_IdUtilisateur(Long idUtilisateur);

    // Méthode qui retourne un seul compteur pour un utilisateur (ou le premier trouvé)
    Compteur findOneByUtilisateurs_IdUtilisateur(Long idUtilisateur);

        @Query("SELECT c FROM Compteur c " +
                "JOIN c.utilisateurs u " +
                "JOIN u.departements d " +
                "JOIN d.direction dir " +
                "WHERE (:directionId IS NULL OR dir.idDirection = :directionId) " +
                "AND (:departementId IS NULL OR d.idDepartement = :departementId) " +
                "AND (:matricule IS NULL OR u.matricule LIKE %:matricule%) " +
                "AND (:nomPrenom IS NULL OR CONCAT(u.nom, ' ', u.prenom) LIKE %:nomPrenom%)")
        List<Compteur> findCompteursFiltre(@Param("directionId") Long directionId,
                                            @Param("departementId") Long departementId,
                                            @Param("matricule") String matricule,
                                            @Param("nomPrenom") String nomPrenom);

    Optional<Compteur> findFirstByUtilisateurs_IdUtilisateur(Long idUser);
}
