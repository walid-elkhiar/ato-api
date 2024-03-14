package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class SignInResponseDTO {
    private String token;
    private String expirationTime;
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
    private Long profilId;
    private String profilDesignation;
    private Long departementId;
    private String departementDescription;
    private Long contratId;
    private String contratDesignation;
    private Long directionId;
    private String directionDesignation;
}
