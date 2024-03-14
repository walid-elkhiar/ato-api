package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Habilitation;
import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.repositories.HabilitationRepository;
import com.ato.backendapi.repositories.ProfilRepository;
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
public class HabilitationController {
    @Autowired
    private HabilitationRepository habilitationRepository;

    @Autowired
    private ProfilRepository profilRepository;

    //GET
    @GetMapping("/AllHabilitations")
    public ResponseEntity<List<Habilitation>> getAllHabilitions() {
        List<Habilitation> habilitations = new ArrayList<Habilitation>();

        habilitationRepository.findAll().forEach(habilitations::add);
        if (habilitations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(habilitations, HttpStatus.OK);
    }

    //GET
//    @GetMapping("/habilitations/{profilId}/Allhabilitations")
//    public ResponseEntity<List<Habilitation>> getAllHabilitationsByProfilId(@PathVariable(value = "profilId") Long profilId) {
//        Profil profil = profilRepository.findById(profilId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));
//
//        List<Habilitation> habilitations = new ArrayList<>();
//        habilitations.addAll(profil.getHabilitations());
//
//        return new ResponseEntity<>(habilitations, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/habilitations/{id}")
    public ResponseEntity<Habilitation> getHabilitationById(@PathVariable(value = "id") Long id) {
        Habilitation habilitation = habilitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Habilitation with id = " + id));

        return new ResponseEntity<>(habilitation, HttpStatus.OK);
    }
    //POST
//    @PostMapping("/profils/{profilId}/habilitations")
//    public ResponseEntity<Habilitation> createHabilitation(@PathVariable(value = "profilId") Long profilId,
//                                                 @RequestBody Habilitation habilitationReq) {
//        Habilitation habilitation = profilRepository.findById(profilId).map(profil -> {
//            profil.getHabilitations().add(habilitationReq);
//            return habilitationRepository.save(habilitationReq);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));
//
//        return new ResponseEntity<>(habilitation, HttpStatus.CREATED);
//    }

    @PutMapping("/habilitations/{id}")
    public ResponseEntity<Habilitation> updateHabilitation(@PathVariable("id") Long id, @RequestBody Habilitation habilitationRequest) {
        Habilitation habilitation = habilitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HabilitationId " + id + "not found"));

        habilitation.setPage(habilitationRequest.getPage());
        habilitation.setActif(habilitationRequest.getActif());
        habilitation.setActif_package(habilitationRequest.getActif_package());

        return new ResponseEntity<>(habilitationRepository.save(habilitation), HttpStatus.OK);
    }

    @DeleteMapping("/habilitations/{id}")
    public ResponseEntity<HttpStatus> deleteHabilitation(@PathVariable("id") Long id) {
        habilitationRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/profils/{profilId}/habilitations")
//    public ResponseEntity<List<Habilitation>> deleteAllHabilitationsOfProfil(@PathVariable(value = "profilId") Long profilId) {
//        Profil profil = profilRepository.findById(profilId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));
//
//        profil.removeHabilitations();
//        profilRepository.save(profil);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
