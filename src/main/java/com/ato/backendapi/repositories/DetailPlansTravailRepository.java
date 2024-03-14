package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.DetailPlansTravail;
import com.ato.backendapi.entities.PlansTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

@Repository
public interface DetailPlansTravailRepository extends JpaRepository<DetailPlansTravail, Long> {
    @Query("SELECT dpt FROM DetailPlansTravail dpt WHERE dpt.plansTravail = :plansTravail")
    List<DetailPlansTravail> findByPlansTravail(@Param("plansTravail") PlansTravail plansTravail);
    List<DetailPlansTravail> findByPlansTravailAndNomJour(PlansTravail plansTravail, String nomJour);
}

