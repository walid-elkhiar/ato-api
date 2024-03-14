package com.ato.backendapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class ListeAttestation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idListeAttestation;
    private String code;
    private String motif;
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "idListeAttestation")
//    private List<DocumentRH> documentRHList;
//
//
//    public void removeDocumentRH() {
//        this.documentRHList.clear();
//    }




}
