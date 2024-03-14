package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habilitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabilitaion;
    private String page;
    private boolean actif_package;
    private boolean actif;
    @Enumerated(EnumType.STRING)
    private NiveauHabilitation niveau;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profil_id", nullable = false)
    @JsonIgnore
    private Profil profil;

    public Habilitation(String page, boolean actifPackage, boolean actif, NiveauHabilitation niveau, Profil profil) {
        this.page = page;
        this.actif_package = actifPackage;
        this.actif = actif;
        this.niveau = niveau;
        this.profil = profil;
    }
}

