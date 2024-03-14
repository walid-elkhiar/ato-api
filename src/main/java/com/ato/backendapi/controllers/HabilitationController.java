package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.HabilitationDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.HabilitationRepository;
import com.ato.backendapi.repositories.ProfilRepository;
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
public class HabilitationController {
    @Autowired
    private HabilitationRepository habilitationRepository;

    @Autowired
    private ProfilRepository profilRepository;

    //GET
    @GetMapping("/AllHabilitations")
    public ResponseEntity<List<HabilitationDTO>> getAllHabilitations(
            @RequestParam(required = false) Long idProfil,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Habilitation> habilitations;

        if (idProfil != null) {
            habilitations = habilitationRepository.findByProfil_IdProfilAndActifTrue(idProfil);
        } else {
            if (offset != null && pageSize != null) {
                Page<Habilitation> habilitationsPage = habilitationRepository.findAll(PageRequest.of(offset, pageSize));
                habilitations = habilitationsPage.getContent();
            } else {
                habilitations = habilitationRepository.findAll();
            }
        }

        if (habilitations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<HabilitationDTO> habilitationDTOS = habilitations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(habilitationDTOS, HttpStatus.OK);
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
    public ResponseEntity<HabilitationDTO> getHabilitationById(@PathVariable(value = "id") Long id) {
        Habilitation habilitation = habilitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Habilitation with id = " + id));

        if (habilitation == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        HabilitationDTO habilitationDTO = new HabilitationDTO();
        habilitationDTO.setIdHabilitaion(habilitation.getIdHabilitaion());
        habilitationDTO.setActif(habilitation.isActif());
        habilitationDTO.setPage(habilitation.getPage());
        habilitationDTO.setActif_package(habilitation.isActif_package());
        habilitationDTO.setNiveau(habilitation.getNiveau());

        // Ajoutez les informations sur le Profil
        Profil profil = habilitation.getProfil();
        if (profil != null) {
            habilitationDTO.setIdProfil(profil.getIdProfil());
            habilitationDTO.setDesignation(profil.getDesignation());
        }

        return new ResponseEntity<>(habilitationDTO, HttpStatus.OK);
    }
    @GetMapping("/habilitations/profil/{profilId}")
    public ResponseEntity<List<Habilitation>> getHabilitationsByProfilId(@PathVariable Long profilId) {
        List<Habilitation> habilitations = habilitationRepository.findByProfil_IdProfil(profilId);
        if (habilitations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(habilitations, HttpStatus.OK);
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


    //POST
    @PostMapping("/habilitations")
    public ResponseEntity<?> addHabilitation(@RequestBody Habilitation habilitation,
                                 @RequestParam Long idProfil) {
        try {
            Profil profil = profilRepository.findById(idProfil)
                    .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'ID : " + idProfil));
            habilitation.setProfil(profil);
            Habilitation savedHabilitation = habilitationRepository.save(habilitation);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Habilitation créée avec succès."); // Message de succès
            responseObject.put("data", savedHabilitation); // Les informations de l'habilitation créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de l'habilitation
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de l'habilitation."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    @PutMapping("/habilitations/{id}")
    public ResponseEntity<Habilitation> updateHabilitation(@PathVariable("id") Long id,
                                                           @RequestBody Habilitation habilitationRequest) {
        // Rechercher l'habilitation par ID
        Habilitation habilitation = habilitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HabilitationId " + id + " not found"));

        // Vérifier si le profil dans habilitationRequest n'est pas nul
        if (habilitationRequest.getProfil() == null || habilitationRequest.getProfil().getIdProfil() == null) {
            throw new IllegalArgumentException("Le profil ou l'ID du profil ne peut pas être nul");
        }

        // Rechercher le profil par ID
        Long idProfil = habilitationRequest.getProfil().getIdProfil();
        Profil profil = profilRepository.findById(idProfil)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'ID : " + idProfil));

        // Mettre à jour les champs de l'habilitation
        habilitation.setProfil(profil);
        habilitation.setPage(habilitationRequest.getPage());
        habilitation.setActif(habilitationRequest.isActif());
        habilitation.setActif_package(habilitationRequest.isActif_package());
        habilitation.setNiveau(habilitationRequest.getNiveau());

        // Sauvegarder l'habilitation mise à jour et retourner la réponse
        return new ResponseEntity<>(habilitationRepository.save(habilitation), HttpStatus.OK);
    }
    @PutMapping("/updateHabilitation/{id}")
    public ResponseEntity<Habilitation> updateHabilitation(@PathVariable("id") Long id,
                                                           @RequestParam Long idProfil,
                                                           @RequestBody Habilitation habilitationRequest) {
        // Rechercher l'habilitation par ID
        Habilitation habilitation = habilitationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HabilitationId " + id + " not found"));

        // Rechercher le profil par ID
        Profil profil = profilRepository.findById(idProfil)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'ID : " + idProfil));

        // Mettre à jour les champs de l'habilitation
        habilitation.setProfil(profil);
        habilitation.setPage(habilitationRequest.getPage());
        habilitation.setActif(habilitationRequest.isActif());
        habilitation.setActif_package(habilitationRequest.isActif_package());
        habilitation.setNiveau(habilitationRequest.getNiveau());

        // Sauvegarder l'habilitation mise à jour et retourner la réponse
        return new ResponseEntity<>(habilitationRepository.save(habilitation), HttpStatus.OK);
    }

    @GetMapping("/active/{idProfil}")
    public ResponseEntity<List<HabilitationDTO>> getActiveHabilitationsByProfil(@PathVariable Long idProfil) {
        List<Habilitation> habilitations = habilitationRepository.findByProfil_IdProfilAndActifTrue(idProfil);
        if (habilitations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<HabilitationDTO> habilitationDTOs = habilitations.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(habilitationDTOs, HttpStatus.OK);
    }

    public HabilitationDTO toDto(Habilitation habilitation) {
        HabilitationDTO dto = new HabilitationDTO();
        dto.setIdHabilitaion(habilitation.getIdHabilitaion());
        dto.setPage(habilitation.getPage());
        dto.setActif_package(habilitation.isActif_package());
        dto.setActif(habilitation.isActif());
        dto.setNiveau(habilitation.getNiveau());


        if (habilitation.getProfil() != null) {
            dto.setIdProfil(habilitation.getProfil().getIdProfil());
            dto.setDesignation(habilitation.getProfil().getDesignation());
        }

        return dto;
    }
    //DELETE
    @DeleteMapping("/habilitations/{id}")
    public ResponseEntity<HttpStatus> deleteHabilitation(@PathVariable("id") Long id) {
        habilitationRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/habilitations")
    public ResponseEntity<HttpStatus> deleteAllHabilitation() {
        habilitationRepository.deleteAll();

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
