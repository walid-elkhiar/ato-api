package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class Traitement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTraitement;
    @Temporal(TemporalType.TIMESTAMP)
    private Date journeePointage;
    @Temporal(TemporalType.TIMESTAMP)
    private Date indifinie;
    @Temporal(TemporalType.TIMESTAMP)
    private Date absence;
    @Temporal(TemporalType.TIMESTAMP)
    private Date absenceJustifiee;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTraitement;
    private String typeTraitement;
    @Temporal(TemporalType.TIMESTAMP)
    private Date trn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dej;
    @Temporal(TemporalType.TIMESTAMP)
    private Date presence;
    @Temporal(TemporalType.TIMESTAMP)
    private Date diff;
    @Temporal(TemporalType.TIMESTAMP)
    private Date objectif;
    private String justificationAbsence;
    @Temporal(TemporalType.TIMESTAMP)
    private Date retard;
    @Temporal(TemporalType.TIMESTAMP)
    private Date tolerance;
    private String pointage;
    @Temporal(TemporalType.TIMESTAMP)
    private Date _25;
    @Temporal(TemporalType.TIMESTAMP)
    private Date _50;
    @Temporal(TemporalType.TIMESTAMP)
    private Date _100;
    private int jrn;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateurs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurs;


}
