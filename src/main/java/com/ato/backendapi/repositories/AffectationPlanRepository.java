package com.ato.backendapi.repositories;

import com.ato.backendapi.dto.AffectationPlanDTO;
import com.ato.backendapi.entities.AffectationPlan;
import com.ato.backendapi.entities.Utilisateurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AffectationPlanRepository extends JpaRepository<AffectationPlan,Long> {
    //List<AffectationPlan> findByUtilisateurs_IdUtilisateurAndDatePlanBetween(Long userId, Date startDate, Date endDate);
    @Query("SELECT a FROM AffectationPlan a " +
            "JOIN FETCH a.utilisateurs u " +
            "JOIN FETCH a.plansTravail pt " +
            "WHERE u.idUtilisateur = :userId " +
            "AND a.datePlan BETWEEN :startDate AND :endDate")
    List<AffectationPlan> findByUtilisateurs_IdUserAndDatePlanBetween(
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    List<AffectationPlan> findByDatePlan(Date datePlan);
    List<AffectationPlan> findByUtilisateursAndDatePlanBetween(Utilisateurs utilisateurs, Date startDate, Date endDate);
    //List<AffectationPlanDTO> findByUtilisateurAndDateRange(Long userId, Date startDate, Date endDate);

}
