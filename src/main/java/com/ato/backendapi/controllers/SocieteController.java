package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Societe;
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
public class SocieteController {

    @Autowired
    private SocieteRepository societeRepository;

    @Autowired
    private EmailConfigurationRepository emailConfigurationRepository;

    //GET
    @GetMapping("/AllSocietes")
    public ResponseEntity<List<Societe>> listeSocietes(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Societe> societes;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Societe> societePage = societeRepository.findAll(PageRequest.of(offset, pageSize));
            societes = societePage.getContent();
        } else {
            societes = societeRepository.findAll();
        }

        if (societes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(societes, HttpStatus.OK);
    }

    @GetMapping("/societe/{id}")
    public ResponseEntity<Societe> getSocieteById(@PathVariable("id") long id) {
        Societe societe = societeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Societe with id = " + id));

        return new ResponseEntity<>(societe, HttpStatus.OK);
    }

    //POST
    @PostMapping("/societe")
    public ResponseEntity<?> addSociete(@RequestBody Societe societe) {
        try {
            Societe _societe = societeRepository.save(societe);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Société créée avec succès."); // Message de succès
            responseObject.put("data", _societe); // Les informations de la société créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de la société
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de la société."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    //PUT
    @PutMapping("/societe/{id}")
    public ResponseEntity<Societe> updateSociete(@PathVariable("id") long id, @RequestBody Societe societe) {
        Societe _societe = societeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Societe with id = " + id));

        _societe.setAdresseSociete(societe.getAdresseSociete());
        _societe.setEmailSociete(societe.getEmailSociete());
        _societe.setTelSociete(societe.getTelSociete());
        _societe.setLogo(societe.getLogo());
        _societe.setPack(societe.getPack());
        _societe.setDateValidite(societe.getDateValidite());
        _societe.setNbrPersonne(societe.getNbrPersonne());
        _societe.setRaisonSociale(societe.getRaisonSociale());


        return new ResponseEntity<>(societeRepository.save(_societe), HttpStatus.OK);
    }


    //DELETE
    @DeleteMapping("/societe/{id}")
    public ResponseEntity<HttpStatus> deleteSocieteById(@PathVariable("id") long id) {
        if (emailConfigurationRepository.existsById(id)) {
            emailConfigurationRepository.deleteById(id);
        }

        societeRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/societe")
    public ResponseEntity<HttpStatus> deleteAllSociete() {
        societeRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
