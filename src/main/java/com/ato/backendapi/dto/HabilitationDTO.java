package com.ato.backendapi.dto;

import lombok.Data;

@Data
public class HabilitationDTO {

    private Long idHabilitaion;
    private int page;
    private int actif_package;
    private int actif;
    //Informations du Profil
    private Long idProfil;
    private String designation;
}
