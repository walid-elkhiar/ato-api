package com.ato.backendapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@Data
public class DepartementDTO {

    private Long idDepartement;
    private String description;

    // Informations sur la direction
    private Long directionId;
    private String directionDescription;
    private int userCount;

}
