package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.DetailPlansJournalierRepository;
import com.ato.backendapi.repositories.DetailPlansTravailRepository;
import com.ato.backendapi.repositories.PlansJournalierRepository;
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
public class DetailPlansTravailController {

    @Autowired
    private PlansJournalierRepository plansJournalierRepository;

    @Autowired
    private DetailPlansTravailRepository detailPlansTravailRepository;

    @Autowired
    private PlansTravailRepository plansTravailRepository;


    //GET
    @GetMapping("/AllDetailPlansTravail")
    public ResponseEntity<List<DetailPlansTravail>> getAllDetailPlansTravail() {
        List<DetailPlansTravail> detailPlansTravails = new ArrayList<DetailPlansTravail>();

        detailPlansTravailRepository.findAll().forEach(detailPlansTravails::add);
        if (detailPlansTravails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(detailPlansTravails, HttpStatus.OK);
    }
    //GET
//    @GetMapping("/detailPlansTravail/{plansJournalierId}/AllDetailPlansTravail")
//    public ResponseEntity<List<DetailPlansTravail>> getAllDetailPlansTravailByPlansJournalierId(@PathVariable(value = "plansJournalierId") Long plansJournalierId) {
//        PlansJournalier plansJournalier = plansJournalierRepository.findById(plansJournalierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        List<DetailPlansTravail> detailPlansTravails = new ArrayList<>();
//        detailPlansTravails.addAll(plansJournalier.getDetailPlansTravails());
//
//        return new ResponseEntity<>(detailPlansTravails, HttpStatus.OK);
//    }

//    @GetMapping("/detailPlansTravail/{plansTravailId}/AllDetailPlansTravail")
//    public ResponseEntity<List<DetailPlansTravail>> getAllDetailPlansTravailByPlansTravailId(@PathVariable(value = "plansTravailId") Long plansTravailId) {
//        PlansTravail plansTravail = plansTravailRepository.findById(plansTravailId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansTravail with id = " + plansTravailId));
//
//        List<DetailPlansTravail> detailPlansTravails = new ArrayList<>();
//        detailPlansTravails.addAll(plansTravail.getDetailPlansTravails());
//
//        return new ResponseEntity<>(detailPlansTravails, HttpStatus.OK);
//    }

    //GET
//    @GetMapping("/detailPlansTravail/{id}")
//    public ResponseEntity<DetailPlansTravail> getDetailPlansTravailById(@PathVariable(value = "id") Long id) {
//        DetailPlansTravail detailPlansTravail = detailPlansTravailRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found DetailPlansTravail with id = " + id));
//
//        return new ResponseEntity<>(detailPlansTravail, HttpStatus.OK);
//    }
    //POST
//    @PostMapping("/plansJournalier/{plansJournalierId}/detailPlansTravail")
//    public ResponseEntity<DetailPlansTravail> createDetailPlansTravailByPlansJournalier(@PathVariable(value = "plansJournalierId") Long plansJournalierId,
//                                                                                              @RequestBody DetailPlansTravail detailPlansTravail) {
//        DetailPlansTravail detailPlansTravail1 = plansJournalierRepository.findById(plansJournalierId).map(planJour -> {
//            planJour.getDetailPlansTravails().add(detailPlansTravail);
//            return detailPlansTravailRepository.save(detailPlansTravail);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        return new ResponseEntity<>(detailPlansTravail1, HttpStatus.CREATED);
//    }

//    @PostMapping("/plansTravail/{plansTravailId}/detailPlansTravail")
//    public ResponseEntity<DetailPlansTravail> createDetailPlansTravailByPlansTravail(@PathVariable(value = "plansTravailId") Long plansTravailId,
//                                                                                        @RequestBody DetailPlansTravail detailPlansTravail) {
//        DetailPlansTravail detailPlansTravail1 = plansTravailRepository.findById(plansTravailId).map(planTrav -> {
//            planTrav.getDetailPlansTravails().add(detailPlansTravail);
//            return detailPlansTravailRepository.save(detailPlansTravail);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found PlansTravail with id = " + plansTravailId));
//
//        return new ResponseEntity<>(detailPlansTravail1, HttpStatus.CREATED);
//    }

    @PutMapping("/detailPlansTravail/{id}")
    public ResponseEntity<DetailPlansTravail> updateDetailPlansTravail(@PathVariable("id") Long id, @RequestBody DetailPlansTravail detailPlansTravaiReq) {
        DetailPlansTravail detailPlansTravail = detailPlansTravailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetailPlansTravailId " + id + "not found"));

        detailPlansTravail.setNomJour(detailPlansTravaiReq.getNomJour());

        return new ResponseEntity<>(detailPlansTravailRepository.save(detailPlansTravail), HttpStatus.OK);
    }

    @DeleteMapping("/detailPlansTravail/{id}")
    public ResponseEntity<HttpStatus> deleteDetailPlansTravail(@PathVariable("id") Long id) {
        detailPlansTravailRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/plansJournalier/{plansJournalierId}/detailPlansTravail")
//    public ResponseEntity<List<DetailPlansTravail>> deleteAllDetailPlansTravailOfPlanJournalier(@PathVariable(value = "plansJournalierId") Long plansJournalierId) {
//        PlansJournalier plansJournalier = plansJournalierRepository.findById(plansJournalierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        plansJournalier.removeDetailPlansTravail();
//        plansJournalierRepository.save(plansJournalier);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/plansTravail/{plansTravailId}/detailPlansTravail")
//    public ResponseEntity<List<DetailPlansTravail>> deleteAllDetailPlansTravailOfPlanTravail(@PathVariable(value = "plansTravailId") Long plansTravailId) {
//        PlansTravail plansTravail = plansTravailRepository.findById(plansTravailId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansTravail with id = " + plansTravailId));
//
//        plansTravail.removeDetailPlansTravails();
//        plansTravailRepository.save(plansTravail);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/detailPlansTravail")
    public ResponseEntity<HttpStatus> deleteAllDetailPlansTravail() {
        detailPlansTravailRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
