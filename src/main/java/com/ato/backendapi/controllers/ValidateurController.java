package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.entities.Validateur;
import com.ato.backendapi.repositories.UtilisateursRepository;
import com.ato.backendapi.repositories.ValidateurRepository;
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
public class ValidateurController {
    @Autowired
    private ValidateurRepository validateurRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllValidateurs")
    public ResponseEntity<List<Validateur>> listeValidateurs(){
        List<Validateur> validateurs = new ArrayList<Validateur>();

        validateurRepository.findAll().forEach(validateurs::add);
        if (validateurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(validateurs, HttpStatus.OK);
    }

//    @GetMapping("/validateurs/{userId}/AllValidateurs")
//    public ResponseEntity<List<Validateur>> getAllValidateursByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<Validateur> validateurs = new ArrayList<>();
//        validateurs.addAll(utilisateurs.getValidateurs());
//
//        return new ResponseEntity<>(validateurs, HttpStatus.OK);
//    }

    @GetMapping("/validateurs/{id}")
    public ResponseEntity<Validateur> getValidateurById(@PathVariable("id") Long id) {
        Validateur validateur = validateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + id));

        return new ResponseEntity<>(validateur, HttpStatus.OK);
    }

    //POST
    @PostMapping("/validateurs")
    public ResponseEntity<Validateur> add(@RequestBody Validateur validateur){
        Validateur v = validateurRepository.save(validateur);
        return new ResponseEntity<>(v,HttpStatus.CREATED);
    }

//    @PostMapping("/users/{userId}/validateurs")
//    public ResponseEntity<Validateur> createValidateur(@PathVariable(value = "userId") Long userId,
//                                                         @RequestBody Validateur validateur) {
//        Validateur validateur1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getValidateurs().add(validateur);
//            return validateurRepository.save(validateur);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(validateur1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/validateurs/{id}")
    public ResponseEntity<Validateur> edit(@PathVariable("id") Long id, @RequestBody Validateur validateur){
        Validateur validateur1 = validateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + id));
        validateur1.setNiveau(validateur.getNiveau());
        //validateur1.setValidations(validateur.getValidations());

        return new ResponseEntity<>(validateurRepository.save(validateur1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/validateurs/{id}")
    public ResponseEntity<HttpStatus> deleteValidateurById(@PathVariable("id") Long id) {
        validateurRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/validateurs")
    public ResponseEntity<HttpStatus> deleteAllValidateurs() {
        validateurRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/validateurs")
//    public ResponseEntity<List<Validateur>> deleteAllValidateursOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeValidateurs();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
