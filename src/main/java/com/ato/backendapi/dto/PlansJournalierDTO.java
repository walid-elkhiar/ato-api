package com.ato.backendapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlansJournalierDTO {
    private Long idPlansJournalier;
    private String code;
    private String libelle;
    private List<DetailPlansJournalierDTO> detailPlansJournaliers;
    private List<DetailPlansTravailDTO> detailPlansTravail;
}
