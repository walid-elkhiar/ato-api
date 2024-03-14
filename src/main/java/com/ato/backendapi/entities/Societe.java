package com.ato.backendapi.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Societe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSociete;
    private String raisonSociale;
    private String adresseSociete;
    private String telSociete;
    private String emailSociete;
    @Lob
    private String logo;
    private String pack;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidite;
    private int nbrPersonne;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "emailConfiguration_id")
//    private EmailConfiguration emailConfiguration;

}
