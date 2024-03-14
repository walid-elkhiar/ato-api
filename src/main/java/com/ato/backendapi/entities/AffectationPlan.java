package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffectationPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAffectationPlan;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePlan;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCycle;
    private String typePlan;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateurs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurs;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zoneGPS_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ZoneGPS zoneGPS;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plansTravail_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private PlansTravail plansTravail;
}
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idAffectationPlan")
//    private List<Pointage> pointages;
//
//    public void removePointages() {
//        this.pointages.clear();
//    }


//    @OneToMany(mappedBy = "affectationPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<DetailPlansJournalier> detailPlansJournaliers;


