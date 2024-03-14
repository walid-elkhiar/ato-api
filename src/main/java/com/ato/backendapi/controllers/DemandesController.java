package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Demandes;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.DemandesRepository;
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
public class DemandesController {

    @Autowired
    private DemandesRepository demandesRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllDemandes")
    public ResponseEntity<List<Demandes>> listeDemandes(){
        List<Demandes> demandes = new ArrayList<Demandes>();

        demandesRepository.findAll().forEach(demandes::add);
        if (demandes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(demandes, HttpStatus.OK);
    }

//    @GetMapping("/demandes/{userId}/AllDemandes")
//    public ResponseEntity<List<Demandes>> getAllDemandesByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<Demandes> demandes = new ArrayList<>();
//        demandes.addAll(utilisateurs.getDemandes());
//
//        return new ResponseEntity<>(demandes, HttpStatus.OK);
//    }

    @GetMapping("/demandes/{id}")
    public ResponseEntity<Demandes> getDemandeById(@PathVariable("id") Long id) {
        Demandes demandes = demandesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Demande with id = " + id));

        return new ResponseEntity<>(demandes, HttpStatus.OK);
    }

    //POST
    @PostMapping("/demandes")
    public ResponseEntity<Demandes> add(@RequestBody Demandes demandes){
        Demandes d =demandesRepository.save(demandes);
        return new ResponseEntity<>(d,HttpStatus.CREATED);
    }

//    @PostMapping("/users/{userId}/demandes")
//    public ResponseEntity<Demandes> createDemande(@PathVariable(value = "userId") Long userId,
//                                                  @RequestBody Demandes demandes) {
//        Demandes demandes1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getDemandes().add(demandes);
//            return demandesRepository.save(demandes);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(demandes1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/demandes/{id}")
    public ResponseEntity<Demandes> edit(@PathVariable("id") Long id, @RequestBody Demandes demandes){
        Demandes demandes1 = demandesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Demande with id = " + id));
        demandes1.setDuree(demandes.getDuree());
        demandes1.setCodeMotif(demandes.getCodeMotif());
        demandes1.setPlage(demandes.getPlage());
        demandes1.setObjet(demandes.getObjet());
        demandes1.setDateReprise(demandes.getDateReprise());
        //demandes1.setValidations(demandes.getValidations());
        demandes1.setDateSaisie(demandes.getDateSaisie());
        demandes1.setDateDebutMission(demandes.getDateDebutMission());
        demandes1.setDateFinMission(demandes.getDateFinMission());
        demandes1.setEtatValidation(demandes.getEtatValidation());

        return new ResponseEntity<>(demandesRepository.save(demandes1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/demandes/{id}")
    public ResponseEntity<HttpStatus> deleteDemandeById(@PathVariable("id") Long id) {
        demandesRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/demandes")
    public ResponseEntity<HttpStatus> deleteAllDemandes() {
        demandesRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/demandes")
//    public ResponseEntity<List<Demandes>> deleteAllDemandesOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeDemandes();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
