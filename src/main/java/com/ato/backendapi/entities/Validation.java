package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Validation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idValidation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValidation;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "demandes_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Demandes demandes;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "validateur_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Validateur validateur;

}
