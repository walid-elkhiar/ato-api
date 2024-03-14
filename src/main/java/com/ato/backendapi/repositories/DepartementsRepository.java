package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Departements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartementsRepository extends JpaRepository<Departements,Long> {
    List<Departements> findByDirection_IdDirection(Long idDirection);
    //List<Departements> findByUtilisateurs_IdUtilisateur(Long idUtilisateur);
}
