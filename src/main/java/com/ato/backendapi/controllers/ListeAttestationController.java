package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.ListeAttestation;
import com.ato.backendapi.repositories.ListeAttestationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class ListeAttestationController {

    @Autowired
    private ListeAttestationRepository listeAttestationRepository;

    //GET
    @GetMapping("/AllAttestations")
    public ResponseEntity<List<ListeAttestation>> listeAttestation(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<ListeAttestation> attestations;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<ListeAttestation> attestationPage = listeAttestationRepository.findAll(PageRequest.of(offset, pageSize));
            attestations = attestationPage.getContent();
        } else {
            attestations = listeAttestationRepository.findAll();
        }

        if (attestations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(attestations, HttpStatus.OK);
    }



    @GetMapping("/attestations/{id}")
    public ResponseEntity<ListeAttestation> getAttestationById(@PathVariable("id") Long id) {
        ListeAttestation listeAttestation = listeAttestationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ListeAttestation with id = " + id));

        return new ResponseEntity<>(listeAttestation, HttpStatus.OK);
    }
    //POST
    @PostMapping("/attestations")
    public ResponseEntity<?> addListeAttestation(@RequestBody ListeAttestation listeAttestation) {
        try {
            ListeAttestation savedListeAttestation = listeAttestationRepository.save(listeAttestation);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Attestation créée avec succès."); // Message de succès
            responseObject.put("data", savedListeAttestation); // Les informations de l'attestation créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de l'attestation
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de l'attestation."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    //PUT
    @PutMapping("/attestations/{id}")
    public ResponseEntity<ListeAttestation> editListeAttestation(@PathVariable("id") Long id, @RequestBody ListeAttestation listeAttestation){
        ListeAttestation listeAttestation1 = listeAttestationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ListeAttestation with id = " + id));
        //listeAttestation1.setDocumentRHList(listeAttestation.getDocumentRHList());
        listeAttestation1.setCode(listeAttestation.getCode());
        listeAttestation1.setMotif(listeAttestation.getMotif());

        return new ResponseEntity<>(listeAttestationRepository.save(listeAttestation1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/attestations/{id}")
    public ResponseEntity<HttpStatus> deleteListeAttestationById(@PathVariable("id") Long id) {
        listeAttestationRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/attestations")
    public ResponseEntity<HttpStatus> deleteAllAttestations() {
        listeAttestationRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
