package com.ato.backendapi.dto;

import com.ato.backendapi.entities.TypeScTraitement;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class SousCodeTraitementDTO {

    private Long idSousCodeTraitement;
    private String code;
    private String libelle;
    @Enumerated(EnumType.STRING)
    private TypeScTraitement type;
    private String presence;
    private int absence;
    // Nouveau champ
    private boolean isChecked;
    //Informations CodeTraitement
    private Long idCodeTraitement;
    private String codeCTraitement;
    private String libelleCTraitement;
    private String couleurCTraitement;
}
