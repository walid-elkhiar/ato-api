package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Contrats;
import com.ato.backendapi.repositories.ContratsRepository;
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
public class ContratsController {

    @Autowired
    private ContratsRepository contratsRepository;

    //GET
    @GetMapping("/AllContrats")
    public ResponseEntity<List<Contrats>> listeContrats(){
        List<Contrats> contrats = new ArrayList<Contrats>();

        contratsRepository.findAll().forEach(contrats::add);
        if (contrats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(contrats, HttpStatus.OK);
    }
    @GetMapping("/contrats/{id}")
    public ResponseEntity<Contrats> getContratById(@PathVariable("id") Long id) {
        Contrats contrats = contratsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Contrat with id = " + id));

        return new ResponseEntity<>(contrats, HttpStatus.OK);
    }
    //POST
    @PostMapping("/contrats")
    public ResponseEntity<Contrats> add(@RequestBody Contrats contrats){
        Contrats c =contratsRepository.save(contrats);
        return new ResponseEntity<>(c,HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/contrats/{id}")
    public ResponseEntity<Contrats> edit(@PathVariable("id") Long id, @RequestBody Contrats contrats){
        Contrats contrats1 = contratsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Contrat with id = " + id));
        contrats1.setDesignation(contrats.getDesignation());
        //contrats1.setUtilisateurs(contrats.getUtilisateurs());

        return new ResponseEntity<>(contratsRepository.save(contrats1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/contrats/{id}")
    public ResponseEntity<HttpStatus> deleteContratById(@PathVariable("id") Long id) {
        contratsRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/contrats")
    public ResponseEntity<HttpStatus> deleteAllContrats() {
        contratsRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
