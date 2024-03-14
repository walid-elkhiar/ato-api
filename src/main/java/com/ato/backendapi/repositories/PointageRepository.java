package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Pointage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PointageRepository extends JpaRepository<Pointage,Long> {
}
