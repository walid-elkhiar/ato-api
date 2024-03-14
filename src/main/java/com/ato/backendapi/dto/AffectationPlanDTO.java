package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
@Data
public class AffectationPlanDTO {

    private Long idAffectationPlan;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePlan;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCycle;
    private String typePlan;

    // Informations sur le Utilisateurs
    private Long idUtilisateur;
    private String matricule;
    private String nom;
    private String prenom;
    private String adresseMail;
    private String posteUtilisateur;
    private String telephone;
    private Date dateEntree;
    private boolean actif;
    private String photo;
    private String password;
    private String adresseIpTel;
    private Date datePassModifie;
    private Date dateFinContrat;
    private String role;

    // Informations sur le ZoneGps
    private Long zoneGpsId;
    private String libelleZone;
    private String centreLat;
    private String centreLong;
    private int rayon;

    // Informations sur le PlanTravail
    private Long planTravailId;
    private String code;
    private String libelle;
    private String type;
    private int nbrJour;

}
