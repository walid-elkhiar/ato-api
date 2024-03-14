package com.ato.backendapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class Direction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDirection;
    private String description;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idDirection")
//    private List<Departements> departements;
//
//    public void removeDepartements() {
//        this.departements.clear();
//    }
}
