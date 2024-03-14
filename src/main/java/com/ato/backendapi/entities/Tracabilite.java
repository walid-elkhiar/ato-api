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
public class Tracabilite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTracabilite;
    private String operation;
    private String concerne;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOperation;
    private String adreesePc;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateurs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurs;

}
