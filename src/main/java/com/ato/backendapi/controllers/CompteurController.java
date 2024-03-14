package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Compteur;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.CompteurRepository;
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
public class CompteurController {

    @Autowired
    private CompteurRepository compteurRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllCompteurs")
    public ResponseEntity<List<Compteur>> getAllCompteurs() {
        List<Compteur> compteurs = new ArrayList<Compteur>();

        compteurRepository.findAll().forEach(compteurs::add);
        if (compteurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(compteurs, HttpStatus.OK);
    }

    //GET
//    @GetMapping("/compteurs/{userId}/AllCompteurs")
//    public ResponseEntity<List<Compteur>> getAllCompteursByProfilId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        List<Compteur> compteurs = new ArrayList<>();
//        compteurs.addAll(utilisateurs.getCompteurs());
//
//        return new ResponseEntity<>(compteurs, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/compteurs/{id}")
    public ResponseEntity<Compteur> getCompteurById(@PathVariable(value = "id") Long id) {
        Compteur compteur = compteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Compteur with id = " + id));

        return new ResponseEntity<>(compteur, HttpStatus.OK);
    }
    //POST
//    @PostMapping("/users/{userId}/compteurs")
//    public ResponseEntity<Compteur> createCompteur(@PathVariable(value = "userId") Long userId,
//                                                           @RequestBody Compteur compteurReq) {
//        Compteur compteur = utilisateursRepository.findById(userId).map(user -> {
//            user.getCompteurs().add(compteurReq);
//            return compteurRepository.save(compteurReq);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        return new ResponseEntity<>(compteur, HttpStatus.CREATED);
//    }

    @PutMapping("/compteurs/{id}")
    public ResponseEntity<Compteur> updateCompteur(@PathVariable("id") Long id, @RequestBody Compteur compteurRequest) {
        Compteur compteur = compteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CompteurId " + id + "not found"));

        compteur.setDroit(compteurRequest.getDroit());
        compteur.setPris(compteurRequest.getPris());
        compteur.setSolde(compteurRequest.getSolde());
        compteur.setDroitAnnuel(compteurRequest.getDroitAnnuel());

        return new ResponseEntity<>(compteurRepository.save(compteur), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/compteurs/{id}")
    public ResponseEntity<HttpStatus> deleteCompteur(@PathVariable("id") Long id) {
        compteurRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/compteurs")
//    public ResponseEntity<List<Compteur>> deleteAllCompteursOfUser(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        utilisateurs.removeCompteurs();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
