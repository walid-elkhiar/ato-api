package com.ato.backendapi.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class AffectationPlanDetailDTO {
    private Long idAffectationPlan;
    private Date datePlan;
    private Date dateCycle;
    private String typePlan;
    private UtilisateursValidateurDTO utilisateur;
    private List<DetailPlansJournalierConsultationAffectationDTO> detailPlansJournaliers;
}