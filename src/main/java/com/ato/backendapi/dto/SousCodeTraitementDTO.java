package com.ato.backendapi.dto;

import lombok.Data;

@Data
public class SousCodeTraitementDTO {

    private Long idSousCodeTraitement;
    private String code;
    private String libelle;
    private String type;
    private String presence;
    private int absence;
    //Informations CodeTraitement
    private Long idCodeTraitement;
    private String codeCTraitement;
    private String libelleCTraitement;
    private String couleurCTraitement;
}
