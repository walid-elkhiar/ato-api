package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.PlansTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PlansTravailRepository extends JpaRepository<PlansTravail,Long> {
}
