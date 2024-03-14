package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Contrats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ContratsRepository extends JpaRepository<Contrats,Long> {
}
