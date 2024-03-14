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

import java.util.Date;
import java.util.List;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Demandes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDemande;
    private Date dateSaisie;
    private String codeMotif;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebutMission;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFinMission;
    private float duree;
    private String objet;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReprise;
    private String plage;
    private String etatValidation;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateurs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurs;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateur_saisi_demande_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurSaisiDemande;
}
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idDemande")
//    private List<Validation> validations;
//
//    public void removeValidations() {
//        this.validations.clear();
//    }

