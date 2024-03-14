package com.ato.backendapi.dto;

import com.ato.backendapi.entities.TypePlansTravail;
import lombok.Data;


@Data
public class PlanTravailDTO {

    private Long planTravailId;
    private String code;
    private String libelle;
    private String type;
    private int nbrJour;

}
