package com.ato.backendapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class PlansTravail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlansTravail;
    private String code;
    private String libelle;
    private String type;
    private int nbrJour;
    @OneToMany(mappedBy = "plansTravail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailPlansTravail> detailPlansTravail;
    @Override
    public String toString() {
        return "PlansTravail{" +
                "idPlansTravail=" + idPlansTravail +
                ", code='" + code + '\'' +
                ", libelle='" + libelle + '\'' +
                ", type='" + type + '\'' +
                ", nbrJour=" + nbrJour +
                '}';
    }
}

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idPlansTravail")
//    private List<DetailPlansTravail> detailPlansTravails;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idPlansTravail")
//    private List<AffectationPlan> affectationPlans;
//
//    public void removeAffectationsPlan() {
//        this.affectationPlans.clear();
//    }
//    public void removeDetailPlansTravails() {
//        this.detailPlansTravails.clear();
//    }

