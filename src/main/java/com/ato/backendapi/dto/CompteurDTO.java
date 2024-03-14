package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class CompteurDTO {

    private Long idCompteur;
    private float droitAnnuel;
    private float droit;
    private float pris;
    private float solde;
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
    // Informations sur le département
    private Long departementId;
    private String departementDescription;
}
