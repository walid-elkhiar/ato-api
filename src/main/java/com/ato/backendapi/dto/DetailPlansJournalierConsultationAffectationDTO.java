package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class DetailPlansJournalierConsultationAffectationDTO {
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    private String libellePlanJournalier;
}
