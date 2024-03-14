package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class DocumentRH {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDocument;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSaisie;
    private String statut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidationStatut;
    private String chemin;
    private String libelle;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateurs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurs;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listeAttestation_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ListeAttestation listeAttestation;




}
