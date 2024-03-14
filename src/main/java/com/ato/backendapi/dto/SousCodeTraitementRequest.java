package com.ato.backendapi.dto;

import com.ato.backendapi.entities.TypeScTraitement;
import lombok.Data;

@Data
public class SousCodeTraitementRequest {
    private String code;
    private String libelle;
    private TypeScTraitement type;
    private int absence;
    private Long codeTraitementId;
    // Informations Profil
    private Long profilId;
}
