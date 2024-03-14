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
import java.util.stream.Collectors;

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

//    @GetMapping("/scTraitements/{profilId}/AllScTraitements")
//    public ResponseEntity<List<SousCodeTraitement>> getAllScTraitementsByProfilId(@PathVariable(value = "profilId") Long profilId) {
//        Profil profil = profilRepository.findById(profilId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));
//
//        List<SousCodeTraitement> sousCodeTraitements = new ArrayList<>();
//        sousCodeTraitements.addAll(profil.getSousCodeTraitement());
//
//        return new ResponseEntity<>(sousCodeTraitements, HttpStatus.OK);
//    }


    @GetMapping("/scTraitements/{profilId}/AllScTraitements")
    public ResponseEntity<List<SousCodeTraitementDTO>> getAllScTraitementsByProfilId(@PathVariable(value = "profilId") Long profilId) {
        // Vérifie si le profil existe dans la base de données
        Profil profil = profilRepository.findById(profilId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));

        // Récupérer tous les sous-codes de traitement (même ceux non liés à ce profil)
        List<SousCodeTraitement> allSousCodeTraitements = sousCodeTraitementRepository.findAll();

        // Mapper chaque SousCodeTraitement vers SousCodeTraitementDTO
        List<SousCodeTraitementDTO> sousCodeTraitementDTOs = allSousCodeTraitements.stream()
                .map(sousCodeTraitement -> {
                    SousCodeTraitementDTO dto = new SousCodeTraitementDTO();
                    dto.setIdSousCodeTraitement(sousCodeTraitement.getIdSousCodeTraitement());
                    dto.setCode(sousCodeTraitement.getCode());
                    dto.setLibelle(sousCodeTraitement.getLibelle());
                    dto.setType(sousCodeTraitement.getType());
                    dto.setAbsence(sousCodeTraitement.getAbsence());

                    // Informations liées à CodeTraitement
                    CodeTraitement codeTraitement = sousCodeTraitement.getCodeTraitement();
                    dto.setIdCodeTraitement(codeTraitement.getIdCodeTraitement());
                    dto.setCodeCTraitement(codeTraitement.getCode());
                    dto.setLibelleCTraitement(codeTraitement.getLibelle());
                    dto.setCouleurCTraitement(codeTraitement.getCouleur());

                    // Vérifier si le profil est lié à ce SousCodeTraitement
                    boolean isChecked = sousCodeTraitement.getProfil().stream()
                            .anyMatch(p -> p.getIdProfil().equals(profilId));

                    dto.setChecked(isChecked);  // Assigner true si le profil est lié, sinon false

                    return dto;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(sousCodeTraitementDTOs, HttpStatus.OK);
    }

    @PutMapping("/{profilId}/{sousCodeTraitementId}/updateCheck")
    public ResponseEntity<String> updateSousCodeTraitementCheck(
            @PathVariable(value = "profilId") Long profilId,
            @PathVariable(value = "sousCodeTraitementId") Long sousCodeTraitementId,
            @RequestParam(value = "isChecked") boolean isChecked) {

        // Récupérer le profil à partir de l'ID
        Profil profil = profilRepository.findById(profilId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));

        // Récupérer le sous-code de traitement à partir de l'ID
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(sousCodeTraitementId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + sousCodeTraitementId));

        if (isChecked) {
            // Si isChecked est true, ajouter le lien entre profil et sousCodeTraitement
            if (!sousCodeTraitement.getProfil().contains(profil)) {
                sousCodeTraitement.addProfil(profil);  // Ajoute la relation ManyToMany
                sousCodeTraitementRepository.save(sousCodeTraitement);
            }
        } else {
            // Si isChecked est false, supprimer le lien entre profil et sousCodeTraitement
            if (sousCodeTraitement.getProfil().contains(profil)) {
                sousCodeTraitement.removeProfil(profilId);  // Supprime la relation ManyToMany
                sousCodeTraitementRepository.save(sousCodeTraitement);
            }
        }

        return new ResponseEntity<>("SousCodeTraitement check updated successfully", HttpStatus.OK);
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
    public ResponseEntity<List<SousCodeTraitementDTO>> getSousCodeTraitementByPresence() {
        List<SousCodeTraitement> result = sousCodeTraitementRepository.findByType(TypeScTraitement.PRESENCE);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<SousCodeTraitementDTO> sousCodeTraitementDTOS = new ArrayList<>();
        for (SousCodeTraitement sousCodeTraitement : result) {
            SousCodeTraitementDTO sousCodeTraitementDTO = new SousCodeTraitementDTO();
            sousCodeTraitementDTO.setIdSousCodeTraitement(sousCodeTraitement.getIdSousCodeTraitement());
            sousCodeTraitementDTO.setCode(sousCodeTraitement.getCode());
            sousCodeTraitementDTO.setAbsence(sousCodeTraitement.getAbsence());
            sousCodeTraitementDTO.setPresence(sousCodeTraitement.getPresence());
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
            sousCodeTraitementDTO.setPresence(sousCodeTraitement.getPresence());
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
    public ResponseEntity<?> addScTraitement(@RequestBody SousCodeTraitement sc) {
        try {
            // Associer un CodeTraitement par défaut
            Long defaultCodeTraitementId = 1L;  // Remplacez par l'ID que vous souhaitez utiliser comme valeur par défaut
            CodeTraitement codeTraitement = codeTraitementRepository.findById(defaultCodeTraitementId)
                    .orElseThrow(() -> new ResourceNotFoundException("Code Traitement par défaut non trouvé avec l'ID : " + defaultCodeTraitementId));

            sc.setCodeTraitement(codeTraitement);  // Associer le CodeTraitement par défaut

            // Sauvegarder le SousCodeTraitement
            SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.save(sc);

            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1);
            responseObject.put("message", "Sous Code Traitement créé avec succès.");
            responseObject.put("data", sousCodeTraitement);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0);
            errorResponse.put("message", "Erreur lors de la création du sous code traitement.");

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
    public ResponseEntity<?> updateScTraitement(@PathVariable("id") long id, @RequestBody SousCodeTraitement sc) {
        try {
            // ID ou code du CodeTraitement par défaut
            final Long defaultCodeTraitementId = 1L; // Remplacez par l'ID par défaut souhaité

            // Récupérer le CodeTraitement par défaut
            CodeTraitement codeTraitement = codeTraitementRepository.findById(defaultCodeTraitementId)
                    .orElseThrow(() -> new ResourceNotFoundException("Code Traitement par défaut non trouvé avec l'ID : " + defaultCodeTraitementId));

            // Récupérer le SousCodeTraitement à mettre à jour
            SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + id));

            // Mettre à jour les champs du SousCodeTraitement
            sousCodeTraitement.setCodeTraitement(codeTraitement); // Associer le CodeTraitement par défaut
            sousCodeTraitement.setCode(sc.getCode());
            sousCodeTraitement.setAbsence(sc.getAbsence());
            sousCodeTraitement.setType(sc.getType());
            sousCodeTraitement.setLibelle(sc.getLibelle());

            // Sauvegarder les modifications
            SousCodeTraitement updatedSousCodeTraitement = sousCodeTraitementRepository.save(sousCodeTraitement);

            // Retourner l'objet mis à jour avec un statut HTTP OK
            return ResponseEntity.ok(updatedSousCodeTraitement);
        } catch (Exception e) {
            // En cas d'erreur, retourner une réponse avec un statut d'erreur serveur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du SousCodeTraitement.");
        }
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
