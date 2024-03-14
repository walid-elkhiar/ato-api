package com.ato.backendapi.security.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String identifiant; // Peut contenir soit l'adresse mail, soit le matricule
    private String password;

}