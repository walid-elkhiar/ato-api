package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.PlansJournalier;
import com.ato.backendapi.entities.PlansTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlansJournalierRepository extends JpaRepository<PlansJournalier,Long> {
}
