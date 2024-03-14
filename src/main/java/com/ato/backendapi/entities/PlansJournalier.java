package com.ato.backendapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class PlansJournalier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlansJournalier;
    private String code;
    private String libelle;
    @OneToMany(mappedBy = "plansJournalier", cascade = CascadeType.ALL)
    private List<DetailPlansJournalier> detailPlansJournaliers;
    @OneToMany(mappedBy = "plansJournalier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailPlansTravail> detailPlansTravail;

    @Override
    public String toString() {
        return "PlansJournalier{" +
                "idPlansJournalier=" + idPlansJournalier +
                ", code='" + code + '\'' +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idPlansJournalier")
//    private List<DetailPlansJournalier> detailPlansJournaliers;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idPlansJournalier")
//    private List<DetailPlansTravail> detailPlansTravails;
//
//    public void removeDetailPlansJournalier() {
//        this.detailPlansJournaliers.clear();
//    }
//    public void removeDetailPlansTravail() {
//        this.detailPlansTravails.clear();
//    }

