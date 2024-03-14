package com.ato.backendapi.repositories;


import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.entities.Validateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ValidateurRepository extends JpaRepository<Validateur,Long> {
    Page<Validateur> findAll(Pageable pageable);
    List<Validateur> findByNiveau(int niveau);
    // Récupérer les validateurs associés à un utilisateur
    List<Validateur> findByUtilisateurs(Utilisateurs utilisateurs);
    Optional<Validateur> findByUtilisateurs_IdUtilisateur(Long idUtilisateur);
    Optional<Validateur> findByUtilisateursAndValidateurAndNiveau(Utilisateurs utilisateurs, Utilisateurs validateur, int niveau);
}