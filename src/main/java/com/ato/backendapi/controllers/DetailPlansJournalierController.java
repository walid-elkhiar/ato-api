package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
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
public class DetailPlansJournalierController {

    @Autowired
    private PlansJournalierRepository plansJournalierRepository;

    @Autowired
    private DetailPlansJournalierRepository detailPlansJournalierRepository;

    @Autowired
    private SousCodeTraitementRepository sousCodeTraitementRepository;


    //GET
    @GetMapping("/AllDetailPlansJournalier")
    public ResponseEntity<List<DetailPlansJournalier>> getAllDetailPlansJournalier() {
        List<DetailPlansJournalier> detailPlansJournaliers = new ArrayList<DetailPlansJournalier>();

        detailPlansJournalierRepository.findAll().forEach(detailPlansJournaliers::add);
        if (detailPlansJournaliers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(detailPlansJournaliers, HttpStatus.OK);
    }
    //GET
//    @GetMapping("/detailPlansJournalier/{plansJournalierId}/AllDetailPlansJournalier")
//    public ResponseEntity<List<DetailPlansJournalier>> getAllDetailPlansJournalierByPlansJournalierId(@PathVariable(value = "plansJournalierId") Long plansJournalierId) {
//        PlansJournalier plansJournalier = plansJournalierRepository.findById(plansJournalierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        List<DetailPlansJournalier> detailPlansJournaliers = new ArrayList<>();
//        detailPlansJournaliers.addAll(plansJournalier.getDetailPlansJournaliers());
//
//        return new ResponseEntity<>(detailPlansJournaliers, HttpStatus.OK);
//    }

//    @GetMapping("/detailPlansJournalier/{sousCodeTraitementId}/AllDetailPlansJournalier")
//    public ResponseEntity<List<DetailPlansJournalier>> getAllDetailPlansJournalierBySousCodeTraitementId(@PathVariable(value = "sousCodeTraitementId") Long sousCodeTraitementId) {
//        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(sousCodeTraitementId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + sousCodeTraitementId));
//
//        List<DetailPlansJournalier> detailPlansJournaliers = new ArrayList<>();
//        detailPlansJournaliers.addAll(sousCodeTraitement.getDetailPlansJournaliers());
//
//        return new ResponseEntity<>(detailPlansJournaliers, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/detailPlansJournalier/{id}")
    public ResponseEntity<DetailPlansJournalier> getDetailPlanJournalierById(@PathVariable(value = "id") Long id) {
        DetailPlansJournalier detailPlansJournalier = detailPlansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Pointage with id = " + id));

        return new ResponseEntity<>(detailPlansJournalier, HttpStatus.OK);
    }
    //POST
//    @PostMapping("/plansJournalier/{plansJournalierId}/detailPlansJournalier")
//    public ResponseEntity<DetailPlansJournalier> createDetailPlansJournalierByPlansJournalier(@PathVariable(value = "plansJournalierId") Long plansJournalierId,
//                                                   @RequestBody DetailPlansJournalier detailPlansJournalier) {
//        DetailPlansJournalier detailPlansJournalier1 = plansJournalierRepository.findById(plansJournalierId).map(planJour -> {
//            planJour.getDetailPlansJournaliers().add(detailPlansJournalier);
//            return detailPlansJournalierRepository.save(detailPlansJournalier);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        return new ResponseEntity<>(detailPlansJournalier1, HttpStatus.CREATED);
//    }

//    @PostMapping("/sousCodeTraitement/{sousCodeTraitementId}/detailPlansJournalier")
//    public ResponseEntity<DetailPlansJournalier> createDetailPlansJournalierBySousCodeTraitement(@PathVariable(value = "sousCodeTraitementId") Long sousCodeTraitementId,
//                                                                                              @RequestBody DetailPlansJournalier detailPlansJournalier) {
//        DetailPlansJournalier detailPlansJournalier1 = sousCodeTraitementRepository.findById(sousCodeTraitementId).map(sousCodeTrai -> {
//            sousCodeTrai.getDetailPlansJournaliers().add(detailPlansJournalier);
//            return detailPlansJournalierRepository.save(detailPlansJournalier);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + sousCodeTraitementId));
//
//        return new ResponseEntity<>(detailPlansJournalier1, HttpStatus.CREATED);
//    }

    @PutMapping("/detailPlansJournalier/{id}")
    public ResponseEntity<DetailPlansJournalier> updateDetailPlansJournalier(@PathVariable("id") Long id, @RequestBody DetailPlansJournalier detailPlansJournalierReq) {
        DetailPlansJournalier detailPlansJournalier = detailPlansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetailPlansJournalierId " + id + "not found"));

        detailPlansJournalier.setFlexible(detailPlansJournalierReq.isFlexible());
        detailPlansJournalier.setObjectif(detailPlansJournalierReq.getObjectif());
        detailPlansJournalier.setHeureDebut(detailPlansJournalierReq.getHeureDebut());
        detailPlansJournalier.setTDebut(detailPlansJournalierReq.getTDebut());
        detailPlansJournalier.setTFin(detailPlansJournalierReq.getTFin());
        detailPlansJournalier.setHeureFin(detailPlansJournalierReq.getHeureFin());

        return new ResponseEntity<>(detailPlansJournalierRepository.save(detailPlansJournalier), HttpStatus.OK);
    }

    @DeleteMapping("/detailPlansJournalier/{id}")
    public ResponseEntity<HttpStatus> deleteDetailPlansJournalier(@PathVariable("id") Long id) {
        detailPlansJournalierRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/plansJournalier/{plansJournalierId}/detailPlansJournalier")
//    public ResponseEntity<List<DetailPlansJournalier>> deleteAllDetailPlansJournalierOfPlanJournalier(@PathVariable(value = "plansJournalierId") Long plansJournalierId) {
//        PlansJournalier plansJournalier = plansJournalierRepository.findById(plansJournalierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        plansJournalier.removeDetailPlansJournalier();
//        plansJournalierRepository.save(plansJournalier);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/sousCodeTraitement/{sousCodeTraitementId}/detailPlansJournalier")
//    public ResponseEntity<List<DetailPlansJournalier>> deleteAllDetailPlansJournalierOfSousCodeTraitement(@PathVariable(value = "sousCodeTraitementId") Long sousCodeTraitementId) {
//        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(sousCodeTraitementId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + sousCodeTraitementId));
//
//        sousCodeTraitement.removeDetailPlansJournaliers();
//        sousCodeTraitementRepository.save(sousCodeTraitement);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/detailPlansJournalier")
    public ResponseEntity<HttpStatus> deleteAllDetailPlansJournalier() {
        detailPlansJournalierRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
