package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class DocumentRhDTO {
    private Long idDocument;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSaisie;
    private String statut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidationStatut;
    private String chemin;
    private String libelle;
    //Informations de l'utilisateur
    private UtilisateursDTO demandeur;
    private UtilisateursDTO saisiePar;
    private UtilisateursDTO validateur;
    //Informations du ListeAttestation
    private Long idListeAttestation;
    private String code;
    private String motif;
}
