package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.DetailPlansJournalier;
import com.ato.backendapi.entities.PlansJournalier;
import com.ato.backendapi.entities.PlansTravail;
import com.ato.backendapi.entities.SousCodeTraitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailPlansJournalierRepository extends JpaRepository<DetailPlansJournalier, Long> {
    @Query("SELECT dpj FROM DetailPlansJournalier dpj WHERE dpj.plansJournalier.idPlansJournalier = :plansJournalierId")
    List<DetailPlansJournalier> findByPlansJournalier_Id(@Param("plansJournalierId") Long plansJournalierId);
    List<DetailPlansJournalier> findByPlansJournalierAndSousCodeTraitement(PlansJournalier plansJournalier, SousCodeTraitement sousCodeTraitement);
    //List<DetailPlansJournalier> findByIdPlansTravail(Long idPlansTravail);
    //List<DetailPlansJournalier> findByPlansJournalier_PlansTravailId(Long idPlansTravail);
    @Query("SELECT dpj FROM DetailPlansJournalier dpj WHERE dpj.plansJournalier IN (SELECT pt.plansJournalier FROM DetailPlansTravail pt WHERE pt.plansTravail.idPlansTravail = :plansTravailId)")
    List<DetailPlansJournalier> findByPlansTravailId(@Param("plansTravailId") Long plansTravailId);
    List<DetailPlansJournalier> findByPlansJournalier_IdPlansJournalierAndFlexible(Long plansJournalierId, boolean flexible);

//        @Query("SELECT dpj FROM DetailPlansJournalier dpj WHERE dpj.plansJournalier.idPlansJournalier = :plansJournalierId")
//        List<DetailPlansJournalier> findByPlansJournalier_Id(@Param("plansJournalierId") Long plansJournalierId);

//        @Query("SELECT dpj FROM DetailPlansJournalier dpj WHERE dpj.plansJournalier.plansTravail.idPlansTravail = :plansTravailId")
//        List<DetailPlansJournalier> findByPlansTravailId(@Param("plansTravailId") Long plansTravailId);

        List<DetailPlansJournalier> findByPlansJournalier(PlansJournalier plansJournalier);


}


