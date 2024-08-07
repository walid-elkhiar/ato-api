package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.ParametrageDemandeDTO;
import com.ato.backendapi.dto.SousCodeTraitementDTO;
import com.ato.backendapi.dto.SousCodeTraitementRequest;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.CodeTraitementRepository;
import com.ato.backendapi.repositories.ProfilRepository;
import com.ato.backendapi.repositories.SousCodeTraitementRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @GetMapping("/AllScTraitements")
    public ResponseEntity<List<SousCodeTraitementDTO>> getAllScTraitements(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<SousCodeTraitement> sousCodeTraitements;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<SousCodeTraitement> sousCodeTraitementsPage = sousCodeTraitementRepository.findAll(PageRequest.of(offset, pageSize));
            sousCodeTraitements = sousCodeTraitementsPage.getContent();
        } else {
            sousCodeTraitements = sousCodeTraitementRepository.findAll();
        }

        if (sousCodeTraitements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<SousCodeTraitementDTO> sousCodeTraitementDTOS = new ArrayList<>();
        for (SousCodeTraitement sousCodeTraitement : sousCodeTraitements) {
            SousCodeTraitementDTO sousCodeTraitementDTO = new SousCodeTraitementDTO();
            sousCodeTraitementDTO.setIdSousCodeTraitement(sousCodeTraitement.getIdSousCodeTraitement());
            sousCodeTraitementDTO.setCode(sousCodeTraitement.getCode());
            sousCodeTraitementDTO.setAbsence(sousCodeTraitement.getAbsence());
            //sousCodeTraitementDTO.setPresence(sousCodeTraitement.getPresence());
            sousCodeTraitementDTO.setLibelle(sousCodeTraitement.getLibelle());
            sousCodeTraitementDTO.setType(sousCodeTraitement.getType());

            // Ajoutez les informations sur le Code Traitement
            CodeTraitement codeTraitement = sousCodeTraitement.getCodeTraitement();
            if (codeTraitement != null) {
                sousCodeTraitementDTO.setIdCodeTraitement(codeTraitement.getIdCodeTraitement());
                sousCodeTraitementDTO.setCodeCTraitement(codeTraitement.getCode());
                sousCodeTraitementDTO.setLibelleCTraitement(codeTraitement.getLibelle());
                sousCodeTraitementDTO.setCouleurCTraitement(codeTraitement.getCouleur());
            }

            sousCodeTraitementDTOS.add(sousCodeTraitementDTO);
        }

        return new ResponseEntity<>(sousCodeTraitementDTOS, HttpStatus.OK);
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
    public ResponseEntity<SousCodeTraitementDTO> getScTraitementById(@PathVariable("id") long id) {
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + id));

        if (sousCodeTraitement == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        SousCodeTraitementDTO sousCodeTraitementDTO = new SousCodeTraitementDTO();
        sousCodeTraitementDTO.setIdSousCodeTraitement(sousCodeTraitement.getIdSousCodeTraitement());
        sousCodeTraitementDTO.setCode(sousCodeTraitement.getCode());
        sousCodeTraitementDTO.setAbsence(sousCodeTraitement.getAbsence());
        //sousCodeTraitementDTO.setPresence(sousCodeTraitement.getPresence());
        sousCodeTraitementDTO.setLibelle(sousCodeTraitement.getLibelle());
        sousCodeTraitementDTO.setType(sousCodeTraitement.getType());


        // Ajoutez les informations sur le Code Traitement
        CodeTraitement codeTraitement = sousCodeTraitement.getCodeTraitement();
        if (codeTraitement != null) {
            sousCodeTraitementDTO.setIdCodeTraitement(codeTraitement.getIdCodeTraitement());
            sousCodeTraitementDTO.setCodeCTraitement(codeTraitement.getCode());
            sousCodeTraitementDTO.setLibelleCTraitement(codeTraitement.getLibelle());
            sousCodeTraitementDTO.setCouleurCTraitement(codeTraitement.getCouleur());

        }

        return new ResponseEntity<>(sousCodeTraitementDTO, HttpStatus.OK);
    }

    // Get SousCodeTraitement ayant le type = PRESENCE
    @GetMapping("/presence")
    public ResponseEntity<List<SousCodeTraitement>> getSousCodeTraitementByPresence() {
        List<SousCodeTraitement> result = sousCodeTraitementRepository.findByType(TypeScTraitement.PRESENCE);
        return ResponseEntity.ok(result);
    }



    // Get des SousCodeTraitement sans le type PRESENCE
    @GetMapping("/sansPresence")
    public ResponseEntity<List<SousCodeTraitementDTO>> getSousCodeTraitementsWithoutPresence() {
        List<SousCodeTraitement> sousCodeTraitementList = sousCodeTraitementRepository.findByTypeNot(TypeScTraitement.PRESENCE);

        if (sousCodeTraitementList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<SousCodeTraitementDTO> sousCodeTraitementDTOS = new ArrayList<>();
        for (SousCodeTraitement sousCodeTraitement : sousCodeTraitementList) {
            SousCodeTraitementDTO sousCodeTraitementDTO = new SousCodeTraitementDTO();
            sousCodeTraitementDTO.setIdSousCodeTraitement(sousCodeTraitement.getIdSousCodeTraitement());
            sousCodeTraitementDTO.setCode(sousCodeTraitement.getCode());
            sousCodeTraitementDTO.setAbsence(sousCodeTraitement.getAbsence());
            //sousCodeTraitementDTO.setPresence(sousCodeTraitement.getPresence());
            sousCodeTraitementDTO.setLibelle(sousCodeTraitement.getLibelle());
            sousCodeTraitementDTO.setType(sousCodeTraitement.getType());

            // Ajoutez les informations sur le Code Traitement
            CodeTraitement codeTraitement = sousCodeTraitement.getCodeTraitement();
            if (codeTraitement != null) {
                sousCodeTraitementDTO.setIdCodeTraitement(codeTraitement.getIdCodeTraitement());
                sousCodeTraitementDTO.setCodeCTraitement(codeTraitement.getCode());
                sousCodeTraitementDTO.setLibelleCTraitement(codeTraitement.getLibelle());
                sousCodeTraitementDTO.setCouleurCTraitement(codeTraitement.getCouleur());
            }

            sousCodeTraitementDTOS.add(sousCodeTraitementDTO);
        }

        return new ResponseEntity<>(sousCodeTraitementDTOS, HttpStatus.OK);
    }

    //Get Parametrage Demande
    @GetMapping("/parametrageDemandes")
    public ResponseEntity<?> getAllParametrageDemandes() {
        try {
            List<ParametrageDemandeDTO> parametrageDemandes = sousCodeTraitementRepository.findAllParametrageDemandes();
            return ResponseEntity.ok(parametrageDemandes);
        } catch (Exception e) {
            // Log the error for diagnostics
            e.printStackTrace();

            // Error response with exception details
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0);
            errorResponse.put("message", "Erreur lors de la récupération des données de la jointure.");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    //POST
    @PostMapping("/scTraitements")
    public ResponseEntity<?> addScTraitement(@RequestBody SousCodeTraitement sc,
                                                @RequestParam Long idCodeTraitement) {
        try {
            CodeTraitement codeTraitement = codeTraitementRepository.findById(idCodeTraitement)
                    .orElseThrow(() -> new ResourceNotFoundException("Code Traitement non trouvé avec l'ID : " + idCodeTraitement));
            sc.setCodeTraitement(codeTraitement);
            SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.save(sc);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Sous Code Traitement créé avec succès."); // Message de succès
            responseObject.put("data", sousCodeTraitement); // Les informations du sous code traitement créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du sous code traitement
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du sous code traitement."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
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

    //Post d'un sousCodeTraitement par Profil
    @PostMapping("/addScTraitementByProfil")
    public ResponseEntity<?> addScTraitement(@RequestBody SousCodeTraitementRequest scDTO) {
        try {
            // Rechercher le CodeTraitement par ID
            CodeTraitement codeTraitement = codeTraitementRepository.findById(scDTO.getCodeTraitementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Code Traitement non trouvé avec l'ID : " + scDTO.getCodeTraitementId()));

            // Rechercher le Profil par ID
            Profil profil = profilRepository.findById(scDTO.getProfilId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'ID : " + scDTO.getProfilId()));

            // Créer le SousCodeTraitement
            SousCodeTraitement sc = new SousCodeTraitement();
            sc.setCode(scDTO.getCode());
            sc.setLibelle(scDTO.getLibelle());
            sc.setType(scDTO.getType());
            sc.setAbsence(scDTO.getAbsence());
            sc.setCodeTraitement(codeTraitement);

            // Ajouter le Profil au SousCodeTraitement
            sc.addProfil(profil);

            // Sauvegarder le SousCodeTraitement
            SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.save(sc);

            // Réponse de succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1);
            responseObject.put("message", "Sous Code Traitement créé avec succès.");
            responseObject.put("data", sousCodeTraitement);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // Journaliser l'erreur pour le diagnostic
            e.printStackTrace();

            // Réponse d'erreur avec détails de l'exception
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0);
            errorResponse.put("message", "Erreur lors de la création du sous code traitement.");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


//    @PostMapping("/profils/{profilId}/scTraitements")
//    public ResponseEntity<SousCodeTraitement> addScTraitementByProfil(@PathVariable(value = "profilId") Long profilId,
//                                                                 @RequestBody SousCodeTraitement sousCodeTraitement) {
//        SousCodeTraitement sousCodeTraitement1 = profilRepository.findById(profilId).map(prof -> {
//            prof.getSousCodeTraitement().add(sousCodeTraitement);
//            return sousCodeTraitementRepository.save(sousCodeTraitement);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));
//
//        return new ResponseEntity<>(sousCodeTraitement1, HttpStatus.CREATED);
//    }

    @PutMapping("/scTraitements/{id}")
    public ResponseEntity<SousCodeTraitement> updateScTraitement(@PathVariable("id") long id, @RequestBody SousCodeTraitement sc,
                                                                 @RequestParam Long idCodeTraitement){
        CodeTraitement codeTraitement = codeTraitementRepository.findById(idCodeTraitement)
                .orElseThrow(() -> new ResourceNotFoundException("Code Traitement non trouvé avec l'ID : " + idCodeTraitement));
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + id));
        sousCodeTraitement.setCodeTraitement(codeTraitement);
        sousCodeTraitement.setCode(sc.getCode());
        sousCodeTraitement.setAbsence(sc.getAbsence());
        sousCodeTraitement.setType(sc.getType());
        sousCodeTraitement.setLibelle(sc.getLibelle());
        //sousCodeTraitement.setPresence(sc.getPresence());

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
