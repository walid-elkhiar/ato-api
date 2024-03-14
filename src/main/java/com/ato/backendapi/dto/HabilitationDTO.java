package com.ato.backendapi.dto;

import com.ato.backendapi.entities.NiveauHabilitation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class HabilitationDTO {

    private Long idHabilitaion;
    private String page;
    private boolean actif_package;
    private boolean actif;
    @Enumerated(EnumType.STRING)
    private NiveauHabilitation niveau;
    //Informations du Profil
    private Long idProfil;
    private String designation;
}
