package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.DetailPlansJournalier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DetailPlansJournalierRepository extends JpaRepository<DetailPlansJournalier,Long> {
}
