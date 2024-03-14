package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.PlansJournalier;
import com.ato.backendapi.repositories.PlansJournalierRepository;
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
public class PlansJournalierController {

    @Autowired
    private PlansJournalierRepository plansJournalierRepository;


    //GET
    @GetMapping("/AllPlansJournalier")
    public ResponseEntity<List<PlansJournalier>> listePlansJournalier(){
        List<PlansJournalier> plansJournaliers = new ArrayList<PlansJournalier>();

        plansJournalierRepository.findAll().forEach(plansJournaliers::add);
        if (plansJournaliers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(plansJournaliers, HttpStatus.OK);
    }


    @GetMapping("/plansJournalier/{id}")
    public ResponseEntity<PlansJournalier> getPlansJournalierById(@PathVariable("id") Long id) {
        PlansJournalier plansJournalier = plansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + id));

        return new ResponseEntity<>(plansJournalier, HttpStatus.OK);
    }
    //POST
    @PostMapping("/plansJournalier")
    public ResponseEntity<PlansJournalier> add(@RequestBody PlansJournalier plansJournalier){
        PlansJournalier p =plansJournalierRepository.save(plansJournalier);
        return new ResponseEntity<>(p,HttpStatus.CREATED);
    }


    //PUT
    @PutMapping("/plansJournalier/{id}")
    public ResponseEntity<PlansJournalier> edit(@PathVariable("id") Long id, @RequestBody PlansJournalier plansJournalier){
        PlansJournalier plansJournalier1 = plansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found PlanJournalier with id = " + id));
        plansJournalier1.setCode(plansJournalier.getCode());
        plansJournalier1.setLibelle(plansJournalier.getLibelle());
        //plansJournalier1.setDetailPlansJournaliers(plansJournalier.getDetailPlansJournaliers());
        //plansJournalier1.setDetailPlansTravails(plansJournalier.getDetailPlansTravails());

        return new ResponseEntity<>(plansJournalierRepository.save(plansJournalier1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/plansJournalier/{id}")
    public ResponseEntity<HttpStatus> deletePlansJournalierById(@PathVariable("id") Long id) {
        plansJournalierRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/plansJournalier")
    public ResponseEntity<HttpStatus> deleteAllPlansJournalier() {
        plansJournalierRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
