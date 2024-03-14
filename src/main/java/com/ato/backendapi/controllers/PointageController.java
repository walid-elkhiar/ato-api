package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.AffectationPlan;
import com.ato.backendapi.entities.Pointage;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.AffectationPlanRepository;
import com.ato.backendapi.repositories.PointageRepository;
import com.ato.backendapi.repositories.UtilisateursRepository;
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
public class PointageController {
    @Autowired
    private AffectationPlanRepository affectationPlanRepository;

    @Autowired
    private PointageRepository pointageRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;


    //GET
    @GetMapping("/AllPointages")
    public ResponseEntity<List<Pointage>> getAllPointages() {
        List<Pointage> pointages = new ArrayList<Pointage>();

        pointageRepository.findAll().forEach(pointages::add);
        if (pointages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(pointages, HttpStatus.OK);
    }
    //GET
//    @GetMapping("/pointages/{affectationId}/AllPointages")
//    public ResponseEntity<List<Pointage>> getAllPointagesByAffectationId(@PathVariable(value = "affectationId") Long affectationId) {
//        AffectationPlan affectationPlan = affectationPlanRepository.findById(affectationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + affectationId));
//
//        List<Pointage> pointages = new ArrayList<>();
//        pointages.addAll(affectationPlan.getPointages());
//
//        return new ResponseEntity<>(pointages, HttpStatus.OK);
//    }

//    @GetMapping("/pointages/{userId}/AllPointages")
//    public ResponseEntity<List<Pointage>> getAllPointagesByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        List<Pointage> pointages = new ArrayList<>();
//        pointages.addAll(utilisateurs.getPointages());
//
//        return new ResponseEntity<>(pointages, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/pointages/{id}")
    public ResponseEntity<Pointage> getPointageById(@PathVariable(value = "id") Long id) {
        Pointage pointage = pointageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Pointage with id = " + id));

        return new ResponseEntity<>(pointage, HttpStatus.OK);
    }
    //POST
//    @PostMapping("/affectations/{affectationId}/pointages")
//    public ResponseEntity<Pointage> createPointage(@PathVariable(value = "affectationId") Long affectationId,
//                                                           @RequestBody Pointage pointage) {
//        Pointage pointage1 = affectationPlanRepository.findById(affectationId).map(aff -> {
//            aff.getPointages().add(pointage);
//            return pointageRepository.save(pointage);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + affectationId));
//
//        return new ResponseEntity<>(pointage1, HttpStatus.CREATED);
//    }

//    @PostMapping("/users/{userId}/pointages")
//    public ResponseEntity<Pointage> createPointageByUser(@PathVariable(value = "userId") Long userId,
//                                                   @RequestBody Pointage pointage) {
//        Pointage pointage1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getPointages().add(pointage);
//            return pointageRepository.save(pointage);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        return new ResponseEntity<>(pointage1, HttpStatus.CREATED);
//    }

    @PutMapping("/pointages/{id}")
    public ResponseEntity<Pointage> updatePointage(@PathVariable("id") Long id, @RequestBody Pointage pointageReq) {
        Pointage pointage = pointageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PointageId " + id + "not found"));

        pointage.setDatePointage(pointageReq.getDatePointage());
        pointage.setType(pointageReq.getType());
        pointage.setDescriptions(pointageReq.getDescriptions());
        pointage.setDevice(pointageReq.getDevice());
        pointage.setAdresseIp(pointageReq.getAdresseIp());


        return new ResponseEntity<>(pointageRepository.save(pointage), HttpStatus.OK);
    }

    @DeleteMapping("/pointages/{id}")
    public ResponseEntity<HttpStatus> deletePointage(@PathVariable("id") Long id) {
        pointageRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/affectations/{affectationId}/pointages")
//    public ResponseEntity<List<Pointage>> deleteAllPointagesOfAffectation(@PathVariable(value = "affectationId") Long affectationId) {
//        AffectationPlan affectationPlan = affectationPlanRepository.findById(affectationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + affectationId));
//
//        affectationPlan.removePointages();
//        affectationPlanRepository.save(affectationPlan);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/users/{userId}/pointages")
//    public ResponseEntity<List<Pointage>> deleteAllPointagesOfUser(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        utilisateurs.removePointages();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/pointages")
    public ResponseEntity<HttpStatus> deleteAllPointages() {
        pointageRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
