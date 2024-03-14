package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Departements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DepartementsRepository extends JpaRepository<Departements,Long> {
}
