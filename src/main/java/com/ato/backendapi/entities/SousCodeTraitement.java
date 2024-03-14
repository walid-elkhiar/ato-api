package com.ato.backendapi.entities;

import com.ato.backendapi.entities.CodeTraitement;
import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.entities.TypeScTraitement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SousCodeTraitement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSousCodeTraitement;
    private String code;
    private String libelle;
    private String presence;
    @Enumerated(EnumType.STRING)
    private TypeScTraitement type;
    private int absence;

    @ManyToMany
    @JoinTable(name = "Parametrage_Demande",
            joinColumns = { @JoinColumn(name = "sousCodeTraitement_id")},
            inverseJoinColumns = { @JoinColumn(name = "profil_id")})
    @ToString.Exclude
    @JsonIgnore // Ignorer la sérialisation de cette relation
    private List<Profil> profil = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codeTraitement_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private CodeTraitement codeTraitement;

    public void addProfil(Profil profil) {
        this.profil.add(profil);
        profil.getSousCodeTraitement().add(this);
    }

    public void removeProfil(Long profilId) {
        Profil p = this.profil.stream().filter(t -> t.getIdProfil() == profilId).findFirst().orElse(null);
        if (p != null) {
            this.profil.remove(p);
            p.getSousCodeTraitement().remove(this);
        }
    }
}
