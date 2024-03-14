package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Utilisateurs implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUtilisateur;
    private String matricule;
    private String nom;

    private String prenom;
    @Email
    private String adresseMail;
    private String posteUtilisateur;
    private String telephone;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntree;
    private boolean actif;
    @Lob
    private String photo;
    private String password;
    private String adresseIpTel;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePassModifie;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFinContrat;
    private String role;

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


    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("User role cannot be null or empty");
        }
        return List.of(new SimpleGrantedAuthority(role));
    }


    @Override
    @JsonIgnore
    public String getUsername() {
        return adresseMail;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}

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

