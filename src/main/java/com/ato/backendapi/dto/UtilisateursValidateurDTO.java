package com.ato.backendapi.dto;

import lombok.Data;

@Data
public class UtilisateursValidateurDTO {
    private Long IdUtilisateur;
    private String matricule;
    private String nom;
    private String prenom;
    private String photo;

}
