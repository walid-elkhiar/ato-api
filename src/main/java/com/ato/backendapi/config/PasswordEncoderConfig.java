package com.ato.backendapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
//
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    ModelMapper simplifie le processus de mapping entre les objets
//    en automatisant autant que possible les tâches répétitives et en fournissant
//    des options de configuration flexibles pour répondre aux besoins spécifiques de votre application.
}
