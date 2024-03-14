package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Tracabilite;
import com.ato.backendapi.entities.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TracabiliteRepository extends JpaRepository<Tracabilite, Long> {
    List<Tracabilite> findByUtilisateurs_IdUtilisateur(Long idUtilisateur);
    List<Tracabilite> findByUtilisateurs(Utilisateurs utilisateurs);
    List<Tracabilite> findByDateOperationBetween(Date startDate, Date endDate);
}