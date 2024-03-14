package com.ato.backendapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @NoArgsConstructor @AllArgsConstructor @Data
public class EmailConfiguration {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConfig;
    private String smtpServer;
    private int smtpServerPort;
    private String sendUsing;
    private String smtpAuthenticate;
    private String sendUsername;
    private String sendPassword;
    private boolean smtpUseSSL;
    private boolean statut;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "societe_id")
//    private Societe societe;
}