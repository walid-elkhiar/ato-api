package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Compteur {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompteur;
    //@NotBlank @NotNull
    private float droitAnnuel;
    //@NotBlank @NotNull
    private float droit;
    //@NotBlank @NotNull
    private float pris;
    //@NotBlank @NotNull
    private float solde;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateurs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurs;
}
