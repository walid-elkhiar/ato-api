package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.AffectationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AffectationPlanRepository extends JpaRepository<AffectationPlan,Long> {
}
