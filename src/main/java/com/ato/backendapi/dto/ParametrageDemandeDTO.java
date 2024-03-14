package com.ato.backendapi.dto;

import com.ato.backendapi.entities.TypeScTraitement;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ParametrageDemandeDTO {
    private Long sousCodeTraitementId;
    private String sousCodeTraitementCode;
    private String sousCodeTraitementLibelle;
    @Enumerated(EnumType.STRING)
    private TypeScTraitement sousCodeTraitementType;
    private int sousCodeTraitementAbsence;
    private Long profilId;
    private String profilDesignation;
}
