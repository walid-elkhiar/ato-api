package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Fete {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFete;
    private String evenement;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateDebut;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateFin;
    private int duree;

}
