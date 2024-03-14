package com.ato.backendapi.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ValidateurDTO {
        private Long idValidateur;
        private int niveau;
        private UtilisateursDTO utilisateurs;
        private UtilisateursDTO validateur;
}