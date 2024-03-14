package com.ato.backendapi.dto;

import com.ato.backendapi.entities.TypeScTraitement;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
public class DetailPlansJournalierDTO {
    private Long idDetailPlansJournalier;
    //@Temporal(TemporalType.TIME)
    private LocalTime heureDebut;
    //@Temporal(TemporalType.TIME)
    private LocalTime heureFin;
    private int tDebut;
    private int tFin;
    private boolean flexible;
    private LocalTime objectif; // Changement de Date à LocalDate
    // Informations du SousCodeTraitement
    private Long idSousCodeTraitement;
    private String code;
    private String libelle;
    @Enumerated(EnumType.STRING)
    private TypeScTraitement type;
    //private String presence;
    private int absence;
    //Informations CodeTraitement
    private Long idCodeTraitement;
    private String codeCTraitement;
    private String libelleCTraitement;
    private String couleurCTraitement;
    //Informations du PlansJournalier
    private Long idPlansJournalier;
    private String codePlanJournalier;
    private String libellePlanJournalier;
}
//    //Information du AffectationPlan
//    private Long idAffectationPlan;
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date datePlan;
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateCycle;
//    private String typePlan;
//        // Informations d'utilisateurs
//    private Long idUtilisateur;
//    private String matricule;
//    private String nom;
//    private String prenom;
//    private String adresseMail;
//    private String posteUtilisateur;
//    private String telephone;
//    private Date dateEntree;
//    private String actif;
//    private String photo;
//    private String password;
//    private String adresseIpTel;
//    private Date datePassModifie;
//    private Date dateFinContrat;
//    private String role;
//        // Informations sur le ZoneGps
//    private Long zoneGpsId;
//    private String libelleZone;
//    private String centreLat;
//    private String centreLong;
//    private int rayon;
//
//        // Informations sur le PlanTravail
//    private Long planTravailId;
//    private String codePlanTravail;
//    private String libellePlanTravail;
//    private String typePlanTravail;
//    private int nbrJour;
