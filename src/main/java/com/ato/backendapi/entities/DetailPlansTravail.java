package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class DetailPlansTravail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetailPlansTravail;
    private String nomJour;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plansJournalier_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PlansJournalier plansJournalier;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plansTravail_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PlansTravail plansTravail;

    @Override
    public String toString() {
        return "DetailPlansTravail{" +
                "idDetailPlansTravail=" + idDetailPlansTravail +
                ", nomJour='" + nomJour + '\'' +
                '}';
    }
}
