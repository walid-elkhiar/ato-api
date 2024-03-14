package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.entities.SousCodeTraitement;
import com.ato.backendapi.repositories.ProfilRepository;
import com.ato.backendapi.repositories.SousCodeTraitementRepository;
import com.ato.backendapi.services.ProfilService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class ProfilController {

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private ProfilService profilService;

    @Autowired
    private SousCodeTraitementRepository sousCodeTraitementRepository;

    //GET
    @GetMapping("/AllProfils")
    public ResponseEntity<List<Profil>> listeProfils(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Profil> profils;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Profil> profilsPage = profilRepository.findAll(PageRequest.of(offset, pageSize));
            profils = profilsPage.getContent();
        } else {
            profils = profilRepository.findAll();
        }

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
    public ResponseEntity<?> addProfil(@RequestBody Profil profil) {
        try {
            // Vérifier si un profil avec la même désignation existe déjà
            Optional<Profil> existingProfil = profilRepository.findByDesignation(profil.getDesignation());

            if (existingProfil.isPresent()) {
                // Retourner un message d'erreur si la désignation est déjà utilisée
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("success", 0);
                errorResponse.put("message", "A profile with this designation already exists.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse.toString());
            }

            // Si aucun doublon, sauvegarder le profil
            Profil savedProfil = profilRepository.save(profil);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Profile created successfully."); // Message de succès
            responseObject.put("data", savedProfil); // Les informations du profil créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());

        } catch (Exception e) {
            // En cas d'erreur lors de la création du profil
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Error creating profile."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }



    @PostMapping("/scTraitements/{scTraitementsId}/profils")
    public ResponseEntity<Profil> addProfilViaScTraitement(@PathVariable(value = "scTraitementsId") Long scTraitementsId, @RequestBody Profil pRequest) {
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
    public ResponseEntity<Profil> editProfil(@PathVariable("id") Long id, @RequestBody Profil profil){
        if (id == 1 || id == 2 || id == 3) {
            throw new IllegalArgumentException("Modification pour ce profil n'est pas possible");
        }

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
        if (id == 1 || id == 2 || id == 3) {
            throw new IllegalArgumentException("Suppression de ce profil n'est pas authorisé");
        }
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
