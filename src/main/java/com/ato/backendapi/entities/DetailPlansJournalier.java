package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;
import java.util.Date;
@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class DetailPlansJournalier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetailPlansJournalier;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private int tDebut;
    private int tFin;
    private boolean flexible;
    private Date objectif;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sousCodeTraitement_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SousCodeTraitement sousCodeTraitement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plansJournalier_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PlansJournalier plansJournalier;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "affectation_plan_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AffectationPlan affectationPlan;

}
