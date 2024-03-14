package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.CodeTraitement;
import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.entities.SousCodeTraitement;
import com.ato.backendapi.repositories.CodeTraitementRepository;
import com.ato.backendapi.repositories.ProfilRepository;
import com.ato.backendapi.repositories.SousCodeTraitementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4444")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class SousCodeTraitementController {

    @Autowired
    private SousCodeTraitementRepository sousCodeTraitementRepository;
    @Autowired
    private CodeTraitementRepository codeTraitementRepository;
    @Autowired
    private ProfilRepository profilRepository;

    //GET
    @GetMapping("/scTraitements")
    public ResponseEntity<List<SousCodeTraitement>> getAllScTraitements() {
        List<SousCodeTraitement> sousCodeTraitementList = new ArrayList<>();

        sousCodeTraitementRepository.findAll().forEach(sousCodeTraitementList::add);

        if (sousCodeTraitementList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(sousCodeTraitementList, HttpStatus.OK);
    }

//    @GetMapping("/scTraitements/{codeTraitementId}/AllScTraitements")
//    public ResponseEntity<List<SousCodeTraitement>> getAllScTraitementsByCodeTraitementId(@PathVariable(value = "codeTraitementId") Long codeTraitementId) {
//        CodeTraitement codeTraitement = codeTraitementRepository.findById(codeTraitementId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found CodeTraitement with id = " + codeTraitementId));
//
//        List<SousCodeTraitement> sousCodeTraitements = new ArrayList<>();
//        sousCodeTraitements.addAll(codeTraitement.getSousCodeTraitements());
//
//        return new ResponseEntity<>(sousCodeTraitements, HttpStatus.OK);
//    }

    @GetMapping("/scTraitements/{profilId}/AllScTraitements")
    public ResponseEntity<List<SousCodeTraitement>> getAllScTraitementsByProfilId(@PathVariable(value = "profilId") Long profilId) {
        Profil profil = profilRepository.findById(profilId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));

        List<SousCodeTraitement> sousCodeTraitements = new ArrayList<>();
        sousCodeTraitements.addAll(profil.getSousCodeTraitement());

        return new ResponseEntity<>(sousCodeTraitements, HttpStatus.OK);
    }

    @GetMapping("/scTraitements/{id}")
    public ResponseEntity<SousCodeTraitement> getScTraitementById(@PathVariable("id") long id) {
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + id));

        return new ResponseEntity<>(sousCodeTraitement, HttpStatus.OK);
    }

    //POST
    @PostMapping("/scTraitements")
    public ResponseEntity<SousCodeTraitement> createScTraitement(@RequestBody SousCodeTraitement sc) {
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.save(sc);
        return new ResponseEntity<>(sousCodeTraitement, HttpStatus.CREATED);
    }

//    @PostMapping("/codeTraitements/{codeTraitementId}/scTraitements")
//    public ResponseEntity<SousCodeTraitement> createScTraitementByCodeTraitement(@PathVariable(value = "codeTraitementId") Long codeTraitementId,
//                                                             @RequestBody SousCodeTraitement sousCodeTraitement) {
//        SousCodeTraitement sousCodeTraitement1 = codeTraitementRepository.findById(codeTraitementId).map(cT -> {
//            cT.getSousCodeTraitements().add(sousCodeTraitement);
//            return sousCodeTraitementRepository.save(sousCodeTraitement);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found CodeTraitement with id = " + codeTraitementId));
//
//        return new ResponseEntity<>(sousCodeTraitement1, HttpStatus.CREATED);
//    }

    @PostMapping("/profils/{profilId}/scTraitements")
    public ResponseEntity<SousCodeTraitement> createScTraitementByProfl(@PathVariable(value = "profilId") Long profilId,
                                                                 @RequestBody SousCodeTraitement sousCodeTraitement) {
        SousCodeTraitement sousCodeTraitement1 = profilRepository.findById(profilId).map(prof -> {
            prof.getSousCodeTraitement().add(sousCodeTraitement);
            return sousCodeTraitementRepository.save(sousCodeTraitement);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));

        return new ResponseEntity<>(sousCodeTraitement1, HttpStatus.CREATED);
    }

    @PutMapping("/scTraitements/{id}")
    public ResponseEntity<SousCodeTraitement> updateScTraitement(@PathVariable("id") long id, @RequestBody SousCodeTraitement sc) {
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + id));

        sousCodeTraitement.setCode(sc.getCode());
        sousCodeTraitement.setAbsence(sc.getAbsence());
        sousCodeTraitement.setType(sc.getType());
        sousCodeTraitement.setLibelle(sc.getLibelle());
        sousCodeTraitement.setPresence(sc.getPresence());
        //sousCodeTraitement.setDetailPlansJournaliers(sc.getDetailPlansJournaliers());

        return new ResponseEntity<>(sousCodeTraitementRepository.save(sousCodeTraitement), HttpStatus.OK);
    }

    //DELETE

    @DeleteMapping("/scTraitements/{id}")
    public ResponseEntity<HttpStatus> deleteScTraitement(@PathVariable("id") long id) {
        sousCodeTraitementRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/scTraitements")
    public ResponseEntity<HttpStatus> deleteAllScTraitements() {
        sousCodeTraitementRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
//    @DeleteMapping("/codeTraitements/{codeTraitementId}/scTraitements")
//    public ResponseEntity<List<SousCodeTraitement>> deleteAllScTraitementOfCodeTraitement(@PathVariable(value = "codeTraitementId") Long codeTraitementId) {
//        CodeTraitement codeTraitement = codeTraitementRepository.findById(codeTraitementId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found CodeTraitement with id = " + codeTraitementId));
//
//        codeTraitement.removeSousCodeTraitement();
//        codeTraitementRepository.save(codeTraitement);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @DeleteMapping("/profils/{profilId}/scTraitements")
    public ResponseEntity<List<SousCodeTraitement>> deleteAllScTraitementOfProfil(@PathVariable(value = "profilId") Long profilId) {
        Profil profil = profilRepository.findById(profilId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));

        profil.removeSousCodeTraitements();
        profilRepository.save(profil);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
