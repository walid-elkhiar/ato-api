package com.ato.backendapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Contrats {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;
    //@NotBlank @NotNull
    private String designation;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idContrat")
//    private List<Utilisateurs> utilisateurs;

//    public void removeUtilisateurs() {
//        this.utilisateurs.clear();
//    }

}
