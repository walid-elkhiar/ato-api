package com.ato.backendapi.repositories;

import com.ato.backendapi.dto.AffectationPlanDTO;
import com.ato.backendapi.entities.AffectationPlan;
import com.ato.backendapi.entities.Utilisateurs;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @Query("SELECT a FROM AffectationPlan a " +
            "JOIN FETCH a.utilisateurs u " +
            "JOIN FETCH a.plansTravail pt " +
            "WHERE u.idUtilisateur = :userId " +
            "AND a.datePlan BETWEEN :startDate AND :endDate " +
            "AND a.typePlan IS NOT NULL")
    List<AffectationPlan> findByUtilisateursAndDatePlanWithTypePlan(
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    List<AffectationPlan> findByDatePlan(Date datePlan);
    List<AffectationPlan> findByUtilisateurs_IdUtilisateur(Long userId);
    List<AffectationPlan> findByUtilisateursAndDatePlanBetween(Utilisateurs utilisateurs, LocalDate startDate, LocalDate endDate);
    //List<AffectationPlan> findByUtilisateursAndDatePlanBetween(Utilisateurs utilisateurs, Date startDate, Date endDate);
    Optional<AffectationPlan> findFirstByUtilisateursOrderByDatePlanDesc(Utilisateurs utilisateurs);
    //List<AffectationPlanDTO> findByUtilisateurAndDateRange(Long userId, Date startDate, Date endDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM AffectationPlan a WHERE a.utilisateurs = :utilisateurs AND a.datePlan BETWEEN :startDate AND :endDate")
    void deleteAffectationsInInterval(@Param("utilisateurs") Utilisateurs utilisateurs,
                                      @Param("startDate") Date startDate,
                                      @Param("endDate") Date endDate);

    @Query("SELECT a FROM AffectationPlan a WHERE a.utilisateurs.idUtilisateur = :userId AND a.datePlan BETWEEN :startDate AND :endDate")
    List<AffectationPlan> findAffectationPlanByUserAndDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<AffectationPlan> findByUtilisateurs_IdUtilisateurAndDatePlan(Long userId, Date datePlan);
}
