package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class TracabiliteDTO {

    private Long idTracabilite;
    private String operation;
    private String concerne;
    //@Temporal(TemporalType.TIMESTAMP)
    private String dateOperation;
    private String adreesePc;
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
    private String faitesPar; // ID of the user who made the changes
}
