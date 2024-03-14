package com.ato.backendapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class ZoneGPS {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idZone;
    private String libelleZone;
    private String centreLat;
    private String centreLong;
    private int rayon;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idZone")
//    private List<AffectationPlan> affectationPlans;
//
//    public void removeAffectationsPlan() {
//        this.affectationPlans.clear();
//    }

}
