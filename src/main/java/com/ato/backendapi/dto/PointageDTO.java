package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data

public class PointageDTO {

    private Long idPointage;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePointage;
    private String type;
    private String descriptions;
    private int device;
    private int adresseIp;
    //Informations du Utilisateurs
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
    //Informations du AffectationPlan
    private Long idAffectationPlan;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePlan;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCycle;
    private String typePlan;
}
