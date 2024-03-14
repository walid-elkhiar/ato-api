package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity @AllArgsConstructor @Data @NoArgsConstructor
public class Habilitation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHabilitaion;
    private int page;
    private int actif_package;
    private int actif;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profil_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Profil profil;
}
