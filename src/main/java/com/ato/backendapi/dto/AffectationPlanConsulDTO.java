package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
@Data
public class AffectationPlanConsulDTO {

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
}
