package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Validateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ValidateurRepository extends JpaRepository<Validateur,Long> {
}
