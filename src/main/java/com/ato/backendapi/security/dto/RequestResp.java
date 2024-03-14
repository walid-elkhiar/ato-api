package com.ato.backendapi.security.dto;

import com.ato.backendapi.entities.Utilisateurs;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestResp {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String role;
    private Utilisateurs users;
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
    // Informations sur le profil
    private Long profilId;
    private String profilDesignation;
    // Informations sur le département
    private Long departementId;
    private String departementDescription;
    // Informations sur le contrat
    private Long contratId;
    private String contratDesignation;
}