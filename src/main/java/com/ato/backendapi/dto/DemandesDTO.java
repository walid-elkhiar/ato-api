package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class DemandesDTO {

    private Long idDemande;
    private Date dateSaisie;
    private String codeMotif;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebutMission;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFinMission;
    private float duree;
    private String objet;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReprise;
    private String plage;
    private String etatValidation;

    private UtilisateursDTO utilisateur_demande;
    private UtilisateursDTO utilisateur_saisi_demande;
}
