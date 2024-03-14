package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;
import java.util.Date;
import java.util.List;
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Utilisateurs {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUtilisateur;
    private String matricule;
    private String nom;

    private String prenom;
    @Email
    private String adresseMail;
    private Poste posteUtilisateur;
    private String telephone;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntree;
    private String actif;
    @Lob
    private byte[] photo;
    private String password;
    private String adresseIpTel;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePassModifie;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFinContrat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profil_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Profil profil;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departements_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Departements departements;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contrats_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Contrats contrats;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUtilisateur")
//    private List<Demandes> demandes;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUtilisateur")
//    private List<Tracabilite> tracabilites;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUtilisateur")
//    private List<Validateur> validateurs;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUtilisateur")
//    private List<Traitement> traitements;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUtilisateur")
//    private List<Compteur> compteurs;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUtilisateur")
//    private List<DocumentRH> documentRH;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUtilisateur")
//    private List<Pointage> pointages;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idUtilisateur")
//    private List<AffectationPlan> affectations;
//    public void removeDemandes() {
//        this.demandes.clear();
//    }
//
//    public void removeTracabilites() {
//        this.tracabilites.clear();
//    }
//
//    public void removeValidateurs() {
//        this.validateurs.clear();
//    }
//
//    public void removeTraitements() {
//        this.traitements.clear();
//    }
//
//    public void removeCompteurs() {
//        this.compteurs.clear();
//    }
//
//    public void removeDocumentRH() {
//        this.documentRH.clear();
//    }
//
//    public void removePointages() {
//        this.pointages.clear();
//    }
//
//    public void removeAffectations() {
//        this.affectations.clear();
//    }

}
