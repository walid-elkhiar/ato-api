package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity @NoArgsConstructor @AllArgsConstructor @Data
public class Validateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idValidateur;
    private int niveau;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilisateurs_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs utilisateurs;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Validateur", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Utilisateurs validateur;
}
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idValidateur")
//    private List<Validation> validations;
//
//    public void removeValidations() {
//        this.validations.clear();
//    }

