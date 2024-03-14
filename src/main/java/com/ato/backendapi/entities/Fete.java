package com.ato.backendapi.entities;

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
    private Date dateDebut;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;
    private int duree;

}
