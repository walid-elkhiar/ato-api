package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.ListeAttestation;
import com.ato.backendapi.repositories.ListeAttestationRepository;
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
public class ListeAttestationController {

    @Autowired
    private ListeAttestationRepository listeAttestationRepository;

    //GET
    @GetMapping("/AllAttestations")
    public ResponseEntity<List<ListeAttestation>> listeAttestation(){
        List<ListeAttestation> listeAttestations = new ArrayList<ListeAttestation>();

        listeAttestationRepository.findAll().forEach(listeAttestations::add);
        if (listeAttestations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(listeAttestations, HttpStatus.OK);
    }

    @GetMapping("/attestations/{id}")
    public ResponseEntity<ListeAttestation> getAttestationById(@PathVariable("id") Long id) {
        ListeAttestation listeAttestation = listeAttestationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ListeAttestation with id = " + id));

        return new ResponseEntity<>(listeAttestation, HttpStatus.OK);
    }
    //POST
    @PostMapping("/attestations")
    public ResponseEntity<ListeAttestation> add(@RequestBody ListeAttestation listeAttestation){
        ListeAttestation l = listeAttestationRepository.save(listeAttestation);
        return new ResponseEntity<>(l,HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/attestations/{id}")
    public ResponseEntity<ListeAttestation> edit(@PathVariable("id") Long id, @RequestBody ListeAttestation listeAttestation){
        ListeAttestation listeAttestation1 = listeAttestationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ListeAttestation with id = " + id));
        //listeAttestation1.setDocumentRHList(listeAttestation.getDocumentRHList());
        listeAttestation1.setCode(listeAttestation.getCode());
        listeAttestation1.setMotif(listeAttestation.getMotif());

        return new ResponseEntity<>(listeAttestationRepository.save(listeAttestation1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/attestations/{id}")
    public ResponseEntity<HttpStatus> deleteAttestationById(@PathVariable("id") Long id) {
        listeAttestationRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/attestations")
    public ResponseEntity<HttpStatus> deleteAllAttestations() {
        listeAttestationRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
