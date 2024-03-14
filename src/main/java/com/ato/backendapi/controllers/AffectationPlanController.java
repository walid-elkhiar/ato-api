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
public class AffectationPlanController {
    @Autowired
    private AffectationPlanRepository affectationPlanRepository;
    @Autowired
    private UtilisateursRepository utilisateursRepository;
    @Autowired
    private PlansTravailRepository plansTravailRepository;
    @Autowired
    private ZoneGpsRepository zoneGpsRepository;

    //GET
    @GetMapping("/AllAffectationsPlan")
    public ResponseEntity<List<AffectationPlan>> listeAffectations(){
        List<AffectationPlan> affectationPlans = new ArrayList<AffectationPlan>();

        affectationPlanRepository.findAll().forEach(affectationPlans::add);
        if (affectationPlans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(affectationPlans, HttpStatus.OK);
    }
//    @GetMapping("/affectations/{userId}/AllAffectations")
//    public ResponseEntity<List<AffectationPlan>> getAllAffectationsByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<AffectationPlan> affectationPlans = new ArrayList<>();
//        affectationPlans.addAll(utilisateurs.getAffectations());
//
//        return new ResponseEntity<>(affectationPlans, HttpStatus.OK);
//    }

//    @GetMapping("/affectations/{planTravailId}/AllAffectations")
//    public ResponseEntity<List<AffectationPlan>> getAllAffectationsByPlanTravailId(@PathVariable(value = "planTravailId") Long planTravailId) {
//        PlansTravail plansTravail = plansTravailRepository.findById(planTravailId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlanTravail with id = " + planTravailId));
//
//        List<AffectationPlan> affectationPlans = new ArrayList<>();
//        affectationPlans.addAll(plansTravail.getAffectationPlans());
//
//        return new ResponseEntity<>(affectationPlans, HttpStatus.OK);
//    }

//    @GetMapping("/affectations/{zoneGpsId}/AllAffectations")
//    public ResponseEntity<List<AffectationPlan>> getAllAffectationsByZoneGpsId(@PathVariable(value = "zoneGpsId") Long zoneGpsId) {
//        ZoneGPS zoneGPS = zoneGpsRepository.findById(zoneGpsId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found ZoneGps with id = " + zoneGpsId));
//
//        List<AffectationPlan> affectationPlans = new ArrayList<>();
//        affectationPlans.addAll(zoneGPS.getAffectationPlans());
//
//        return new ResponseEntity<>(affectationPlans, HttpStatus.OK);
//    }

    @GetMapping("/affectations/{id}")
    public ResponseEntity<AffectationPlan> getAffectationById(@PathVariable("id") Long id) {
        AffectationPlan affectationPlan = affectationPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Affectation with id = " + id));

        return new ResponseEntity<>(affectationPlan, HttpStatus.OK);
    }
    //POST
    @PostMapping("/affectations")
    public ResponseEntity<AffectationPlan> add(@RequestBody AffectationPlan affectationPlan,
                                               @RequestParam Long idUser,
                                               @RequestParam Long idZone,
                                               @RequestParam Long idPlanTravail){
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        ZoneGPS zoneGPS = zoneGpsRepository.findById(idZone)
                .orElseThrow(() -> new ResourceNotFoundException("ZoneGps non trouvé avec l'ID : " + idZone));
        PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
                .orElseThrow(() -> new ResourceNotFoundException("PlanTravail non trouvé avec l'ID : " + idPlanTravail));

        affectationPlan.setUtilisateurs(utilisateurs);
        affectationPlan.setPlansTravail(plansTravail);
        affectationPlan.setZoneGPS(zoneGPS);

        AffectationPlan a = affectationPlanRepository.save(affectationPlan);
        return new ResponseEntity<>(a,HttpStatus.CREATED);
    }

//    @PostMapping("/users/{userId}/affectations")
//    public ResponseEntity<AffectationPlan> createAffectationByUserId(@PathVariable(value = "userId") Long userId,
//                                                  @RequestBody AffectationPlan affectationPlan) {
//        AffectationPlan affectationPlan1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getAffectations().add(affectationPlan);
//            return affectationPlanRepository.save(affectationPlan);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(affectationPlan1, HttpStatus.CREATED);
//    }

//    @PostMapping("/planTravail/{planTravailId}/affectations")
//    public ResponseEntity<AffectationPlan> createAffectationByPlanTravailId(@PathVariable(value = "planTravailId") Long planTravailId,
//                                                                     @RequestBody AffectationPlan affectationPlan) {
//        AffectationPlan affectationPlan1 = plansTravailRepository.findById(planTravailId).map(plTr -> {
//            plTr.getAffectationPlans().add(affectationPlan);
//            return affectationPlanRepository.save(affectationPlan);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found PlanTravail with id = " + planTravailId));
//
//        return new ResponseEntity<>(affectationPlan1, HttpStatus.CREATED);
//    }

//    @PostMapping("/zoneGps/{zoneGpsId}/affectations")
//    public ResponseEntity<AffectationPlan> createAffectationByZoneGpsId(@PathVariable(value = "zoneGpsId") Long zoneGpsId,
//                                                                            @RequestBody AffectationPlan affectationPlan) {
//        AffectationPlan affectationPlan1 = zoneGpsRepository.findById(zoneGpsId).map(zone -> {
//            zone.getAffectationPlans().add(affectationPlan);
//            return affectationPlanRepository.save(affectationPlan);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found ZoneGps with id = " + zoneGpsId));
//
//        return new ResponseEntity<>(affectationPlan1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/affectations/{id}")
    public ResponseEntity<AffectationPlan> edit(@PathVariable("id") Long id, @RequestBody AffectationPlan affectationPlan,
                                                @RequestParam Long idUser,
                                                @RequestParam Long idZone,
                                                @RequestParam Long idPlanTravail){
        AffectationPlan affectationPlan1 = affectationPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + id));
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        ZoneGPS zoneGPS = zoneGpsRepository.findById(idZone)
                .orElseThrow(() -> new ResourceNotFoundException("ZoneGps non trouvé avec l'ID : " + idZone));
        PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
                .orElseThrow(() -> new ResourceNotFoundException("PlanTravail non trouvé avec l'ID : " + idPlanTravail));
        affectationPlan1.setUtilisateurs(utilisateurs);
        affectationPlan1.setPlansTravail(plansTravail);
        affectationPlan1.setZoneGPS(zoneGPS);
        affectationPlan1.setDatePlan(affectationPlan.getDatePlan());
        affectationPlan1.setTypePlan(affectationPlan.getTypePlan());
        affectationPlan1.setDateCycle(affectationPlan.getDateCycle());
        //affectationPlan1.setPointages(affectationPlan.getPointages());

        return new ResponseEntity<>(affectationPlanRepository.save(affectationPlan1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/affectations/{id}")
    public ResponseEntity<HttpStatus> deleteAffectationById(@PathVariable("id") Long id) {
        affectationPlanRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/affectations")
    public ResponseEntity<HttpStatus> deleteAllAffectations() {
        affectationPlanRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/affectations")
//    public ResponseEntity<List<AffectationPlan>> deleteAllAffectationsOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeAffectations();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/planTravail/{planTravailId}/affectations")
//    public ResponseEntity<List<AffectationPlan>> deleteAllAffectationsOfPlanTravail(@PathVariable(value = "planTravailId") Long planTravailId) {
//        PlansTravail plansTravail = plansTravailRepository.findById(planTravailId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + planTravailId));
//
//        plansTravail.removeAffectationsPlan();
//        plansTravailRepository.save(plansTravail);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/zoneGps/{zoneGpsId}/affectations")
//    public ResponseEntity<List<AffectationPlan>> deleteAllAffectationsOfZoneGps(@PathVariable(value = "zoneGpsId") Long zoneGpsId) {
//        ZoneGPS zoneGPS = zoneGpsRepository.findById(zoneGpsId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + zoneGpsId));
//
//        zoneGPS.removeAffectationsPlan();
//        zoneGpsRepository.save(zoneGPS);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
