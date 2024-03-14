package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.PlansJournalier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PlansJournalierRepository extends JpaRepository<PlansJournalier,Long> {
}
