package com.ato.backendapi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity @AllArgsConstructor @NoArgsConstructor @Data
public class Profil {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfil;
    @Column(unique = true, nullable = false) // Ajout de la contrainte unique sur 'designation'
    private String designation;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idProfil")
//    private List<Utilisateurs> utilisateurs;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idProfil")
//    private List<Habilitation> habilitations;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "profil", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<SousCodeTraitement> sousCodeTraitement;
    public void removeSousCodeTraitements() {
        this.sousCodeTraitement.clear();
    }
}
//    public void removeUtilisateurs() {
//        this.utilisateurs.clear();
//    }
//    public void removeHabilitations() {
//        this.habilitations.clear();
//    }

