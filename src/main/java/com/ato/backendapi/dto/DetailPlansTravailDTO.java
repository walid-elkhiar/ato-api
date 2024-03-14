package com.ato.backendapi.dto;

import lombok.Data;

@Data
public class DetailPlansTravailDTO {

    private Long idDetailPlansTravail;
    private String nomJour;
    //Informations du Plan Journalier
    private Long idPlansJournalier;
    private String codePlansJournalier;
    private String libellePlansJournalier;
    //Informations du Plan Travail
    private Long idPlansTravail;
    private String codePlanTravail;
    private String libellePlanTravail;
    private String type;
    private int nbrJour;

}
