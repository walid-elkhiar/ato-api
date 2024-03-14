package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.PlansJournalier;
import com.ato.backendapi.repositories.PlansJournalierRepository;
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
public class PlansJournalierController {

    @Autowired
    private PlansJournalierRepository plansJournalierRepository;


    //GET
    @GetMapping("/AllPlansJournalier")
    public ResponseEntity<List<PlansJournalier>> listePlansJournalier(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<PlansJournalier> plansJournaliers;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<PlansJournalier> plansJournalierPage = plansJournalierRepository.findAll(PageRequest.of(offset, pageSize));
            plansJournaliers = plansJournalierPage.getContent();
        } else {
            plansJournaliers = plansJournalierRepository.findAll();
        }

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
    public ResponseEntity<?> add(@RequestBody PlansJournalier plansJournalier) {
        try {
            PlansJournalier savedPlansJournalier = plansJournalierRepository.save(plansJournalier);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Plan journalier créé avec succès."); // Message de succès
            responseObject.put("data", savedPlansJournalier); // Les informations du plan journalier créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du plan journalier
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du plan journalier."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
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
