package com.ato.backendapi.dto;

import com.ato.backendapi.entities.Validateur;

public class ValidateurMapper {

    public static ValidateurDTO toDTO(Validateur validateur) {
        if (validateur == null) {
            return null;
        }

        ValidateurDTO dto = new ValidateurDTO();
        dto.setIdValidateur(validateur.getIdValidateur());
        dto.setNiveau(validateur.getNiveau());
        dto.setUtilisateurs(UtilisateursMapper.toDTO(validateur.getUtilisateurs()));
        dto.setValidateur(UtilisateursMapper.toDTO(validateur.getValidateur()));

        return dto;
    }
}
