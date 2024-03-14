package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Tracabilite;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.TracabiliteRepository;
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
public class TracabiliteController {


    @Autowired
    private TracabiliteRepository tracabiliteRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllTracabilites")
    public ResponseEntity<List<Tracabilite>> listeTracabilites(){
        List<Tracabilite> tracabilites = new ArrayList<Tracabilite>();

        tracabiliteRepository.findAll().forEach(tracabilites::add);
        if (tracabilites.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tracabilites, HttpStatus.OK);
    }

//    @GetMapping("/tracabilites/{userId}/AllTracabilites")
//    public ResponseEntity<List<Tracabilite>> getAllTracabilitesByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<Tracabilite> tracabilites = new ArrayList<>();
//        tracabilites.addAll(utilisateurs.getTracabilites());
//
//        return new ResponseEntity<>(tracabilites, HttpStatus.OK);
//    }

    @GetMapping("/tracabilites/{id}")
    public ResponseEntity<Tracabilite> getTracabiliteById(@PathVariable("id") Long id) {
        Tracabilite tracabilite = tracabiliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tracabilite with id = " + id));

        return new ResponseEntity<>(tracabilite, HttpStatus.OK);
    }

    //POST
    @PostMapping("/tracabilites")
    public ResponseEntity<Tracabilite> add(@RequestBody Tracabilite tracabilite){
        Tracabilite t = tracabiliteRepository.save(tracabilite);
        return new ResponseEntity<>(t,HttpStatus.CREATED);
    }

//    @PostMapping("/users/{userId}/tracabilites")
//    public ResponseEntity<Tracabilite> createTracabilite(@PathVariable(value = "userId") Long userId,
//                                                  @RequestBody Tracabilite tracabilite) {
//        Tracabilite tracabilite1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getTracabilites().add(tracabilite);
//            return tracabiliteRepository.save(tracabilite);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(tracabilite1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/tracabilites/{id}")
    public ResponseEntity<Tracabilite> edit(@PathVariable("id") Long id, @RequestBody Tracabilite tracabilite){
        Tracabilite tracabilite1 = tracabiliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tracabilite with id = " + id));
        tracabilite1.setAdreesePc(tracabilite.getAdreesePc());
        tracabilite1.setConcerne(tracabilite.getConcerne());
        tracabilite1.setOperation(tracabilite.getOperation());
        tracabilite1.setDateOperation(tracabilite.getDateOperation());
        return new ResponseEntity<>(tracabiliteRepository.save(tracabilite1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/tracabilites/{id}")
    public ResponseEntity<HttpStatus> deleteTracabiliteById(@PathVariable("id") Long id) {
        tracabiliteRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tracabilites")
    public ResponseEntity<HttpStatus> deleteAllTracabilites() {
        tracabiliteRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/tracabilites")
//    public ResponseEntity<List<Tracabilite>> deleteAllTracabilitesOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeTracabilites();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
