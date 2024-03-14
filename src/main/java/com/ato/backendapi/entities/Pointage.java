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

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Pointage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPointage;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePointage;
    private String type;
    private String descriptions;
    private int device;
    private int adresseIp;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateurs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurs;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "affectationPlan_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AffectationPlan affectationPlan;
}
