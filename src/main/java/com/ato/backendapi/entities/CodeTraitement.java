package com.ato.backendapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class CodeTraitement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCodeTraitement;
    private String code;
    private String libelle;
    private String couleur;
}
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idCodeTraitement")
//    private List<SousCodeTraitement> sousCodeTraitements;
//
//    public void removeSousCodeTraitement() {
//        this.sousCodeTraitements.clear();
//    }
