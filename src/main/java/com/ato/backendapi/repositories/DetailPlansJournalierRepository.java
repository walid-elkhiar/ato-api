package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.DetailPlansJournalier;
import com.ato.backendapi.entities.PlansTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailPlansJournalierRepository extends JpaRepository<DetailPlansJournalier, Long> {
    @Query("SELECT dpj FROM DetailPlansJournalier dpj WHERE dpj.plansJournalier.idPlansJournalier = :plansJournalierId")
    List<DetailPlansJournalier> findByPlansJournalier_Id(@Param("plansJournalierId") Long plansJournalierId);
//    List<DetailPlansJournalier> findByIdPlansTravail(Long idPlansTravail);
}


