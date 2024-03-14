package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.EmailConfiguration;
import com.ato.backendapi.repositories.EmailConfigurationRepository;
import com.ato.backendapi.repositories.SocieteRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class EmailConfigurationController {

    @Autowired
    private EmailConfigurationRepository emailConfigurationRepository;

    @Autowired
    private SocieteRepository societeRepository;

    @GetMapping("/AllEmails")
    public ResponseEntity<List<EmailConfiguration>> getAllEmails(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<EmailConfiguration> emailConfigurations;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<EmailConfiguration> emailConfigurationPage = emailConfigurationRepository.findAll(PageRequest.of(offset, pageSize));
            emailConfigurations = emailConfigurationPage.getContent();
        } else {
            emailConfigurations = emailConfigurationRepository.findAll();
        }

        if (emailConfigurations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(emailConfigurations, HttpStatus.OK);
    }


    @GetMapping({ "/emailConfiguration/{id}", "/societe/{id}/emailConfiguration" })
    public ResponseEntity<EmailConfiguration> getEmailConfigurationById(@PathVariable(value = "id") Long id) {
        EmailConfiguration emailConfiguration = emailConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found EmailConfiguration with id = " + id));

        return new ResponseEntity<>(emailConfiguration, HttpStatus.OK);
    }

    @PostMapping("/emailConfiguration")
    public ResponseEntity<?> add(@RequestBody EmailConfiguration emailConfiguration) {
        try {
            EmailConfiguration e = emailConfigurationRepository.save(emailConfiguration);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Configuration de l'e-mail créée avec succès."); // Message de succès
            responseObject.put("data", e); // Les informations de la configuration de l'e-mail créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de la configuration de l'e-mail
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de la configuration de l'e-mail."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    @PostMapping("/societe/{societeId}/emailConfiguration")
    public ResponseEntity<EmailConfiguration> createEmailConfigurationBySocieteId(@PathVariable(value = "societeId") Long societeId,
                                                         @RequestBody EmailConfiguration emailConfiguration) {
        EmailConfiguration emailConfiguration1 = societeRepository.findById(societeId).map(soc -> {
            soc.setEmailConfiguration(emailConfiguration);
            return emailConfigurationRepository.save(emailConfiguration);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Societe with id = " + societeId));

        return new ResponseEntity<>(emailConfiguration1, HttpStatus.CREATED);
    }

    @PutMapping("/emailConfiguration/{id}")
    public ResponseEntity<EmailConfiguration> edit(@PathVariable("id") Long id, @RequestBody EmailConfiguration configuration){
        EmailConfiguration emailConfiguration = emailConfigurationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found EmailConfiguration with id = " + id));
        emailConfiguration.setSociete(configuration.getSociete());
        emailConfiguration.setSendPassword(configuration.getSendPassword());
        emailConfiguration.setSendUsing(configuration.getSendUsing());
        emailConfiguration.setSendUsername(configuration.getSendUsername());
        emailConfiguration.setSmtpAuthenticate(configuration.getSmtpAuthenticate());
        emailConfiguration.setSmtpServer(configuration.getSmtpServer());
        emailConfiguration.setSmtpServerPort(configuration.getSmtpServerPort());
        emailConfiguration.setSmtpUseSSL(configuration.isSmtpUseSSL());

        return new ResponseEntity<>(emailConfigurationRepository.save(emailConfiguration), HttpStatus.OK);
    }

    @DeleteMapping("/emailConfiguration/{id}")
    public ResponseEntity<HttpStatus> deleteEmailConfiguration(@PathVariable("id") long id) {
        emailConfigurationRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/societe/{societeId}/emailConfiguration")
    public ResponseEntity<EmailConfiguration> deleteEmailConfigurationOfSociete(@PathVariable(value = "societeId") Long societeId) {
        if (!societeRepository.existsById(societeId)) {
            throw new ResourceNotFoundException("Not found Societe with id = " + societeId);
        }

        emailConfigurationRepository.deleteBySociete_IdSociete(societeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/emailConfiguration")
    public ResponseEntity<HttpStatus> deleteAllEmailConfiguration() {
        emailConfigurationRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
