package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Societe;
import com.ato.backendapi.repositories.EmailConfigurationRepository;
import com.ato.backendapi.repositories.SocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @GetMapping("/societe")
    public ResponseEntity<List<Societe>> getAllSociete() {
        List<Societe> societeList = new ArrayList<>();

            societeRepository.findAll().forEach(societeList::add);

        if (societeList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(societeList, HttpStatus.OK);
    }

    @GetMapping("/societe/{id}")
    public ResponseEntity<Societe> getSocieteById(@PathVariable("id") long id) {
        Societe societe = societeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Societe with id = " + id));

        return new ResponseEntity<>(societe, HttpStatus.OK);
    }

    //POST
    @PostMapping("/societe")
    public ResponseEntity<Societe> createSociete(@RequestBody Societe societe) {
        Societe _societe = societeRepository.save(societe);
        return new ResponseEntity<>(_societe, HttpStatus.CREATED);
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
