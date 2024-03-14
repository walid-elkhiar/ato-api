package com.ato.backendapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class PlansTravail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlansTravail;
    private String code;
    private String libelle;
    private TypePlansTravail type;
    private int nbrJour;
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



}
