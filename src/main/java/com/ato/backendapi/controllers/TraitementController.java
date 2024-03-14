package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Traitement;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.TraitementRepository;
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
public class TraitementController {

    @Autowired
    private TraitementRepository traitementRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllTraitements")
    public ResponseEntity<List<Traitement>> listeTraitements(){
        List<Traitement> traitements = new ArrayList<Traitement>();

        traitementRepository.findAll().forEach(traitements::add);
        if (traitements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(traitements, HttpStatus.OK);
    }

//    @GetMapping("/traitements/{userId}/AllTraitements")
//    public ResponseEntity<List<Traitement>> getAllTraitementsByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<Traitement> traitements = new ArrayList<>();
//        traitements.addAll(utilisateurs.getTraitements());
//
//        return new ResponseEntity<>(traitements, HttpStatus.OK);
//    }

    @GetMapping("/traitements/{id}")
    public ResponseEntity<Traitement> getTraitementById(@PathVariable("id") Long id) {
        Traitement traitement = traitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Traitement with id = " + id));

        return new ResponseEntity<>(traitement, HttpStatus.OK);
    }

    //POST
    @PostMapping("/traitements")
    public ResponseEntity<Traitement> add(@RequestBody Traitement traitement){
        Traitement t = traitementRepository.save(traitement);
        return new ResponseEntity<>(t,HttpStatus.CREATED);
    }

//    @PostMapping("/users/{userId}/traitements")
//    public ResponseEntity<Traitement> createTraitement(@PathVariable(value = "userId") Long userId,
//                                                       @RequestBody Traitement traitement) {
//        Traitement traitement1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getTraitements().add(traitement);
//            return traitementRepository.save(traitement);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(traitement1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/traitements/{id}")
    public ResponseEntity<Traitement> edit(@PathVariable("id") Long id, @RequestBody Traitement traitement){
        Traitement traitement1 = traitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Traitement with id = " + id));
        traitement1.set_25(traitement.get_25());
        traitement1.set_50(traitement.get_50());
        traitement1.set_100(traitement.get_100());
        traitement1.setAbsence(traitement.getAbsence());
        traitement1.setAbsenceJustifiee(traitement.getAbsenceJustifiee());
        traitement1.setDateTraitement(traitement.getDateTraitement());
        traitement1.setDej(traitement.getDej());
        traitement1.setDiff(traitement.getDiff());
        traitement1.setIndifinie(traitement.getIndifinie());
        traitement1.setJourneePointage(traitement.getJourneePointage());
        traitement1.setJustificationAbsence(traitement.getJustificationAbsence());
        traitement1.setJrn(traitement.getJrn());
        traitement1.setObjectif(traitement.getObjectif());
        traitement1.setPresence(traitement.getPresence());
        traitement1.setPointage(traitement.getPointage());
        traitement1.setRetard(traitement.getRetard());
        traitement1.setTolerance(traitement.getTolerance());
        traitement1.setTrn(traitement.getTrn());
        traitement1.setTypeTraitement(traitement.getTypeTraitement());


        return new ResponseEntity<>(traitementRepository.save(traitement1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/traitements/{id}")
    public ResponseEntity<HttpStatus> deleteTraitementById(@PathVariable("id") Long id) {
        traitementRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/traitements")
    public ResponseEntity<HttpStatus> deleteAllTraitements() {
        traitementRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/traitements")
//    public ResponseEntity<List<Traitement>> deleteAllTraitementOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeTraitements();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
