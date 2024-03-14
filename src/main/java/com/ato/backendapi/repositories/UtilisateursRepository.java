package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UtilisateursRepository extends JpaRepository<Utilisateurs,Long> {
}
