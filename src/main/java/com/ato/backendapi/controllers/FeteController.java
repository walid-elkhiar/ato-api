package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Fete;
import com.ato.backendapi.repositories.FeteRepository;
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
public class FeteController {

    @Autowired
    private FeteRepository feteRepository;


    //GET
    @GetMapping("/fetes")
    public ResponseEntity<List<Fete>> getAllFetes() {
        List<Fete> fetes = new ArrayList<>();

        feteRepository.findAll().forEach(fetes::add);

        if (fetes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(fetes, HttpStatus.OK);
    }

    @GetMapping("/fetes/{id}")
    public ResponseEntity<Fete> getFeteById(@PathVariable("id") long id) {
        Fete fete = feteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Fete with id = " + id));

        return new ResponseEntity<>(fete, HttpStatus.OK);
    }

    //POST
    @PostMapping("/fetes")
    public ResponseEntity<Fete> createFete(@RequestBody Fete fete) {
        Fete _f = feteRepository.save(fete);
        return new ResponseEntity<>(_f, HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/fetes/{id}")
    public ResponseEntity<Fete> updateFete(@PathVariable("id") long id, @RequestBody Fete fete) {
        Fete _f = feteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Fete with id = " + id));

        _f.setDateDebut(fete.getDateDebut());
        _f.setDuree(fete.getDuree());
        _f.setEvenement(fete.getEvenement());
        _f.setDateFin(fete.getDateFin());


        return new ResponseEntity<>(feteRepository.save(_f), HttpStatus.OK);
    }


    //DELETE
    @DeleteMapping("/fetes/{id}")
    public ResponseEntity<HttpStatus> deleteFeteById(@PathVariable("id") long id) {

        feteRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/fetes")
    public ResponseEntity<HttpStatus> deleteAllFetes() {
        feteRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
