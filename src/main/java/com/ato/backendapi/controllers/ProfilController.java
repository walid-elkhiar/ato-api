package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.entities.SousCodeTraitement;
import com.ato.backendapi.repositories.ProfilRepository;
import com.ato.backendapi.repositories.SousCodeTraitementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class ProfilController {

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private SousCodeTraitementRepository sousCodeTraitementRepository;

    //GET
    @GetMapping("/AllProfils")
    public ResponseEntity<List<Profil>> listeProfils(){
        List<Profil> profils = new ArrayList<Profil>();
        profilRepository.findAll().forEach(profils::add);
        if (profils.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(profils, HttpStatus.OK);
    }

    @GetMapping("/scTraitements/{scTraitementsId}/profils")
    public ResponseEntity<List<Profil>> getAllProfilsByScTraitementsId(@PathVariable(value = "scTraitementsId") Long scTraitementsId) {
        if (!sousCodeTraitementRepository.existsById(scTraitementsId)) {
            throw new ResourceNotFoundException("Not found SousCodeTraitement with id = " + scTraitementsId);
        }

        List<Profil> profils = profilRepository.findProfilsBySousCodeTraitement_IdSousCodeTraitement(scTraitementsId);
        return new ResponseEntity<>(profils, HttpStatus.OK);
    }
    @GetMapping("/profils/{id}")
    public ResponseEntity<Profil> getProfilById(@PathVariable("id") Long id) {
        Profil profil = profilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

        return new ResponseEntity<>(profil, HttpStatus.OK);
    }

    @GetMapping("/profils/{profilId}/scTraitements")
    public ResponseEntity<List<SousCodeTraitement>> getAllScTraitementsByProfilId(@PathVariable(value = "profilId") Long profilId) {
        if (!profilRepository.existsById(profilId)) {
            throw new ResourceNotFoundException("Not found Profil  with id = " + profilId);
        }

        List<SousCodeTraitement> sousCodeTraitements = sousCodeTraitementRepository.findSousCodeTraitementsByProfil_IdProfil(profilId);
        return new ResponseEntity<>(sousCodeTraitements, HttpStatus.OK);
    }
    //POST
    @PostMapping("/profils")
    public ResponseEntity<Profil> add(@RequestBody Profil profil){
        Profil p =profilRepository.save(profil);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @PostMapping("/scTraitements/{scTraitementsId}/profils")
    public ResponseEntity<Profil> addProfil(@PathVariable(value = "scTraitementsId") Long scTraitementsId, @RequestBody Profil pRequest) {
        Profil profil = sousCodeTraitementRepository.findById(scTraitementsId).map(sc -> {
            Long profId = pRequest.getIdProfil();

            // tag is existed
            if (profId != 0L) {
                Profil _p = profilRepository.findById(profId)
                        .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profId));
                sc.addProfil(_p);
                sousCodeTraitementRepository.save(sc);
                return _p;
            }

            // add and create new Profil
            sc.addProfil(pRequest);
            return profilRepository.save(pRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + scTraitementsId));

        return new ResponseEntity<>(profil, HttpStatus.CREATED);
    }
    //PUT
    @PutMapping("/profils/{id}")
    public ResponseEntity<Profil> edit(@PathVariable("id") Long id, @RequestBody Profil profil){
        Profil profil1 = profilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + id));
        profil1.setDesignation(profil.getDesignation());
        //profil1.setHabilitations(profil.getHabilitations());
        //profil1.setUtilisateurs(profil.getUtilisateurs());
        profil1.setSousCodeTraitement(profil.getSousCodeTraitement());

        return new ResponseEntity<>(profilRepository.save(profil1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/profils/{id}")
    public ResponseEntity<HttpStatus> deleteProfilById(@PathVariable("id") Long id) {
        profilRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/scTraitements/{scTraitementsId}/profils/{profilId}")
    public ResponseEntity<HttpStatus> deleteProfilFromScTraitement(@PathVariable(value = "scTraitementsId") Long scTraitementsId, @PathVariable(value = "profilId") Long profilId) {
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(scTraitementsId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + scTraitementsId));

        sousCodeTraitement.removeProfil(profilId);
        sousCodeTraitementRepository.save(sousCodeTraitement);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/profils")
    public ResponseEntity<HttpStatus> deleteAllProfils() {
        profilRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
