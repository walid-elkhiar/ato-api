package com.ato.backendapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Collection;
import java.util.List;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class Departements {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDepartement;
    private String description;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idDepartement")
//    private List<Utilisateurs> utilisateurs;

//    public void removeUtilisateurs() {
//        this.utilisateurs.clear();
//    }
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "direction_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Direction direction;
    @OneToMany(mappedBy = "departements")
    private List<Utilisateurs> utilisateurs;

    @Override
    public String toString() {
        return "Departements{" +
                "idDepartement=" + idDepartement +
                ", description='" + description + '\'' +
                // Ne pas inclure la liste des utilisateurs ici pour éviter la boucle infinie
                '}';
    }
}
