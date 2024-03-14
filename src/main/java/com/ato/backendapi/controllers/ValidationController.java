package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.AffectationPlan;
import com.ato.backendapi.entities.Demandes;
import com.ato.backendapi.entities.Validateur;
import com.ato.backendapi.entities.Validation;
import com.ato.backendapi.repositories.DemandesRepository;
import com.ato.backendapi.repositories.ValidateurRepository;
import com.ato.backendapi.repositories.ValidationRepository;
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
public class ValidationController {

    @Autowired
    private DemandesRepository demandesRepository;

    @Autowired
    private ValidateurRepository validateurRepository;

    @Autowired
    private ValidationRepository validationRepository;


    //GET
    @GetMapping("/AllValidations")
    public ResponseEntity<List<Validation>> getAllValidations() {
        List<Validation> validations = new ArrayList<Validation>();

        validationRepository.findAll().forEach(validations::add);
        if (validations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(validations, HttpStatus.OK);
    }
    //GET
//    @GetMapping("/validations/{demandeId}/AllValidations")
//    public ResponseEntity<List<Validation>> getAllValidationsByDemandeId(@PathVariable(value = "demandeId") Long demandeId) {
//        Demandes demandes = demandesRepository.findById(demandeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Demande with id = " + demandeId));
//
//        List<Validation> validations = new ArrayList<>();
//        validations.addAll(demandes.getValidations());
//
//        return new ResponseEntity<>(validations, HttpStatus.OK);
//    }

//    @GetMapping("/validations/{validateurId}/AllValidations")
//    public ResponseEntity<List<Validation>> getAllValidationsByValidateurId(@PathVariable(value = "validateurId") Long validateurId) {
//        Validateur validateur = validateurRepository.findById(validateurId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + validateurId));
//
//        List<Validation> validations = new ArrayList<>();
//        validations.addAll(validateur.getValidations());
//
//        return new ResponseEntity<>(validations, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/validations/{id}")
    public ResponseEntity<Validation> getValidationById(@PathVariable(value = "id") Long id) {
        Validation validation = validationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Validation with id = " + id));

        return new ResponseEntity<>(validation, HttpStatus.OK);
    }
    //POST
    @PostMapping("/validations")
    public ResponseEntity<Validation> add(@RequestBody Validation validation){
        Validation v =validationRepository.save(validation);
        return new ResponseEntity<>(v,HttpStatus.CREATED);
    }
//    @PostMapping("/demandes/{demandeId}/validations")
//    public ResponseEntity<Validation> createValidationByDemandeId(@PathVariable(value = "demandeId") Long demandeId,
//                                                   @RequestBody Validation validation) {
//        Validation validation1 = demandesRepository.findById(demandeId).map(dem -> {
//            dem.getValidations().add(validation);
//            return validationRepository.save(validation);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Demande with id = " + demandeId));
//
//        return new ResponseEntity<>(validation1, HttpStatus.CREATED);
//    }

//    @PostMapping("/validateurs/{validateurId}/validations")
//    public ResponseEntity<Validation> createValidationByValidateurId(@PathVariable(value = "validateurId") Long validateurId,
//                                                       @RequestBody Validation validation) {
//        Validation validation1 = validateurRepository.findById(validateurId).map(validt -> {
//            validt.getValidations().add(validation);
//            return validationRepository.save(validation);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + validateurId));
//
//        return new ResponseEntity<>(validation1, HttpStatus.CREATED);
//    }

    @PutMapping("/validations/{id}")
    public ResponseEntity<Validation> updateValidation(@PathVariable("id") Long id, @RequestBody Validation validationReq) {
        Validation validation = validationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ValidationId " + id + "not found"));

        validation.setDateValidation(validationReq.getDateValidation());

        return new ResponseEntity<>(validationRepository.save(validation), HttpStatus.OK);
    }

    @DeleteMapping("/validation/{id}")
    public ResponseEntity<HttpStatus> deleteValidation(@PathVariable("id") Long id) {
        validationRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/validation")
    public ResponseEntity<HttpStatus> deleteAllValidations() {
        validationRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/demandes/{demandeId}/validations")
//    public ResponseEntity<List<Validation>> deleteAllValidationsOfDemande(@PathVariable(value = "demandeId") Long demandeId) {
//        Demandes demandes = demandesRepository.findById(demandeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Demande with id = " + demandeId));
//
//        demandes.removeValidations();
//        demandesRepository.save(demandes);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/validateur/{validateurId}/validations")
//    public ResponseEntity<List<Validation>> deleteAllValidationsOfValidateur(@PathVariable(value = "validateurId") Long validateurId) {
//        Validateur validateur = validateurRepository.findById(validateurId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + validateurId));
//
//        validateur.removeValidations();
//        validateurRepository.save(validateur);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
