package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.PlansTravail;
import com.ato.backendapi.repositories.PlansTravailRepository;
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
public class PlansTravailController {

    @Autowired
    private PlansTravailRepository plansTravailRepository;


    //GET
    @GetMapping("/AllPlansTravail")
    public ResponseEntity<List<PlansTravail>> listePlansTravail(){
        List<PlansTravail> plansTravails = new ArrayList<PlansTravail>();

        plansTravailRepository.findAll().forEach(plansTravails::add);
        if (plansTravails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(plansTravails, HttpStatus.OK);
    }


    @GetMapping("/plansTravail/{id}")
    public ResponseEntity<PlansTravail> getPlansTravailById(@PathVariable("id") Long id) {
        PlansTravail plansTravail = plansTravailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansTravail with id = " + id));

        return new ResponseEntity<>(plansTravail, HttpStatus.OK);
    }
    //POST
    @PostMapping("/plansTravail")
    public ResponseEntity<PlansTravail> add(@RequestBody PlansTravail plansTravail){
        PlansTravail p =plansTravailRepository.save(plansTravail);
        return new ResponseEntity<>(p,HttpStatus.CREATED);
    }


    //PUT
    @PutMapping("/plansTravail/{id}")
    public ResponseEntity<PlansTravail> edit(@PathVariable("id") Long id, @RequestBody PlansTravail plansTravail){
        PlansTravail plansTravail1 = plansTravailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found PlanTravail with id = " + id));
        //plansTravail1.setAffectationPlans(plansTravail.getAffectationPlans());
        plansTravail1.setCode(plansTravail.getCode());
        //plansTravail1.setDetailPlansTravails(plansTravail.getDetailPlansTravails());
        plansTravail1.setLibelle(plansTravail.getLibelle());
        plansTravail1.setNbrJour(plansTravail.getNbrJour());
        plansTravail1.setNbrJour(plansTravail.getNbrJour());

        return new ResponseEntity<>(plansTravailRepository.save(plansTravail1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/plansTravail/{id}")
    public ResponseEntity<HttpStatus> deletePlansTravailById(@PathVariable("id") Long id) {
        plansTravailRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/plansTravail")
    public ResponseEntity<HttpStatus> deleteAllPlansTravail() {
        plansTravailRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
