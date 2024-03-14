package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class DetailPlansJournalier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetailPlansJournalier;
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureDebut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureFin;
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


}
