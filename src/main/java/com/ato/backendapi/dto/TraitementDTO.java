package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class TraitementDTO {
    private Long idTraitement;
    @Temporal(TemporalType.TIMESTAMP)
    private Date journeePointage;
    @Temporal(TemporalType.TIMESTAMP)
    private Date indifinie;
    @Temporal(TemporalType.TIMESTAMP)
    private Date absence;
    @Temporal(TemporalType.TIMESTAMP)
    private Date absenceJustifiee;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTraitement;
    private String typeTraitement;
    @Temporal(TemporalType.TIMESTAMP)
    private Date trn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dej;
    @Temporal(TemporalType.TIMESTAMP)
    private Date presence;
    @Temporal(TemporalType.TIMESTAMP)
    private Date diff;
    @Temporal(TemporalType.TIMESTAMP)
    private Date objectif;
    private String justificationAbsence;
    @Temporal(TemporalType.TIMESTAMP)
    private Date retard;
    @Temporal(TemporalType.TIMESTAMP)
    private Date tolerance;
    private String pointage;
    @Temporal(TemporalType.TIMESTAMP)
    private Date _25;
    @Temporal(TemporalType.TIMESTAMP)
    private Date _50;
    @Temporal(TemporalType.TIMESTAMP)
    private Date _100;
    private int jrn;
    //Informations de l'utilisateur
    private Long idUtilisateur;
    private String matricule;
    private String nom;
    private String prenom;
    private String adresseMail;
    private String posteUtilisateur;
    private String telephone;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntree;
    private boolean actif;
    private String photo;
    private String password;
    private String adresseIpTel;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePassModifie;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFinContrat;
    private String role;
}
