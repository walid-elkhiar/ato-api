package com.ato.backendapi.dto;

import com.ato.backendapi.entities.Poste;
import lombok.Data;

import java.util.Date;
@Data
public class UtilisateursDTO {
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

    // Informations sur le profil
    private Long profilId;
    private String profilDesignation;

    // Informations sur le département
    private Long departementId;
    private String departementDescription;

    // Informations sur le contrat
    private Long contratId;
    private String contratDesignation;

    // Informations sur la direction
    private Long directionId;
    private String directionDescription;

}
