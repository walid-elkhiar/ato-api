package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DetailPlansJournalierDTO;
import com.ato.backendapi.dto.DetailPlansTravailDTO;
import com.ato.backendapi.dto.PlansJournalierDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.PlansJournalierRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class PlansJournalierController {

    @Autowired
    private PlansJournalierRepository plansJournalierRepository;


    //GET
    @GetMapping("/AllPlansJournalier")
    public ResponseEntity<List<PlansJournalierDTO>> listePlansJournalier(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<PlansJournalier> plansJournaliers;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<PlansJournalier> plansJournalierPage = plansJournalierRepository.findAll(PageRequest.of(offset, pageSize));
            plansJournaliers = plansJournalierPage.getContent();
        } else {
            plansJournaliers = plansJournalierRepository.findAll();
        }

        if (plansJournaliers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Convertir les entités en DTOs
        List<PlansJournalierDTO> plansJournalierDTOs = plansJournaliers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(plansJournalierDTOs, HttpStatus.OK);
    }

    private PlansJournalierDTO convertToDTO(PlansJournalier plansJournalier) {
        PlansJournalierDTO dto = new PlansJournalierDTO();
        dto.setIdPlansJournalier(plansJournalier.getIdPlansJournalier());
        dto.setCode(plansJournalier.getCode());
        dto.setLibelle(plansJournalier.getLibelle());

        List<DetailPlansJournalierDTO> detailPlansJournalierDTOs = plansJournalier.getDetailPlansJournaliers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dto.setDetailPlansJournaliers(detailPlansJournalierDTOs);

        List<DetailPlansTravailDTO> detailPlansTravailDTOs = plansJournalier.getDetailPlansTravail().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dto.setDetailPlansTravail(detailPlansTravailDTOs);

        return dto;
    }

    private DetailPlansJournalierDTO convertToDTO(DetailPlansJournalier detailPlansJournalier) {
        DetailPlansJournalierDTO dto = new DetailPlansJournalierDTO();
        dto.setIdDetailPlansJournalier(detailPlansJournalier.getIdDetailPlansJournalier());
        dto.setHeureDebut(detailPlansJournalier.getHeureDebut());
        dto.setHeureFin(detailPlansJournalier.getHeureFin());
        dto.setTDebut(detailPlansJournalier.getTDebut());
        dto.setTFin(detailPlansJournalier.getTFin());
        dto.setFlexible(detailPlansJournalier.isFlexible());
        dto.setObjectif(detailPlansJournalier.getObjectif());

        SousCodeTraitement sousCodeTraitement = detailPlansJournalier.getSousCodeTraitement();
        dto.setIdSousCodeTraitement(sousCodeTraitement.getIdSousCodeTraitement());
        dto.setCode(sousCodeTraitement.getCode());
        dto.setLibelle(sousCodeTraitement.getLibelle());
        dto.setType(sousCodeTraitement.getType());
        dto.setAbsence(sousCodeTraitement.getAbsence());

        CodeTraitement codeTraitement = sousCodeTraitement.getCodeTraitement();
        dto.setIdCodeTraitement(codeTraitement.getIdCodeTraitement());
        dto.setCodeCTraitement(codeTraitement.getCode());
        dto.setLibelleCTraitement(codeTraitement.getLibelle());
        dto.setCouleurCTraitement(codeTraitement.getCouleur());

        PlansJournalier plansJournalier = detailPlansJournalier.getPlansJournalier();
        dto.setIdPlansJournalier(plansJournalier.getIdPlansJournalier());
        dto.setCodePlanJournalier(plansJournalier.getCode());
        dto.setLibellePlanJournalier(plansJournalier.getLibelle());

        return dto;
    }

    private DetailPlansTravailDTO convertToDTO(DetailPlansTravail detailPlansTravail) {
        DetailPlansTravailDTO dto = new DetailPlansTravailDTO();
        dto.setIdDetailPlansTravail(detailPlansTravail.getIdDetailPlansTravail());
        dto.setNomJour(detailPlansTravail.getNomJour());

        PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
        dto.setIdPlansJournalier(plansJournalier.getIdPlansJournalier());
        dto.setCodePlansJournalier(plansJournalier.getCode());
        dto.setLibellePlansJournalier(plansJournalier.getLibelle());

        PlansTravail plansTravail = detailPlansTravail.getPlansTravail();
        dto.setIdPlansTravail(plansTravail.getIdPlansTravail());
        dto.setCodePlanTravail(plansTravail.getCode());
        dto.setLibellePlanTravail(plansTravail.getLibelle());
        dto.setType(plansTravail.getType());
        dto.setNbrJour(plansTravail.getNbrJour());

        return dto;
    }

    @GetMapping("/plansJournalier/{id}/detailPlansJournalier")
    public ResponseEntity<List<DetailPlansJournalierDTO>> getDetailPlansJournalierByPlanId(@PathVariable(value = "id") Long id) {
        PlansJournalier plansJournalier = plansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlanJournalier non trouvé avec l'ID : " + id));

        List<DetailPlansJournalier> detailPlansJournaliers = plansJournalier.getDetailPlansJournaliers();
        if (detailPlansJournaliers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Convertir en DTO
        List<DetailPlansJournalierDTO> detailPlansJournalierDTOs = detailPlansJournaliers.stream().map(this::convertToDTO).collect(Collectors.toList());

        return new ResponseEntity<>(detailPlansJournalierDTOs, HttpStatus.OK);
    }


    @GetMapping("/plansJournalier/{id}")
    public ResponseEntity<PlansJournalier> getPlansJournalierById(@PathVariable("id") Long id) {
        PlansJournalier plansJournalier = plansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + id));

        return new ResponseEntity<>(plansJournalier, HttpStatus.OK);
    }
    //POST
    @PostMapping("/plansJournalier")
    public ResponseEntity<?> addPlansJournalier(@RequestBody PlansJournalier plansJournalier) {
        try {
            // Sauvegarder l'objet PlansJournalier
            PlansJournalier savedPlansJournalier = plansJournalierRepository.save(plansJournalier);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Le plan de travail a été créé avec succès."); // Message de succès

            // Créer l'objet PlansJournalier dans une structure
            JSONObject plansJournalierData = new JSONObject();
            plansJournalierData.put("idPlansJournalier", savedPlansJournalier.getIdPlansJournalier());
            plansJournalierData.put("code", savedPlansJournalier.getCode());
            plansJournalierData.put("libelle", savedPlansJournalier.getLibelle());

            // Créer un objet final pour "data"
            JSONObject dataObject = new JSONObject();
            dataObject.put("PlansJournalier", plansJournalierData);

            // Ajouter "data" à la réponse
            responseObject.put("data", dataObject);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du plan journalier
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du plan journalier."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }




    //PUT
    @PutMapping("/plansJournalier/{id}")
    public ResponseEntity<PlansJournalier> editPlansJournalier(@PathVariable("id") Long id, @RequestBody PlansJournalier plansJournalier){
        PlansJournalier plansJournalier1 = plansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found PlanJournalier with id = " + id));
        plansJournalier1.setCode(plansJournalier.getCode());
        plansJournalier1.setLibelle(plansJournalier.getLibelle());
        //plansJournalier1.setDetailPlansJournaliers(plansJournalier.getDetailPlansJournaliers());
        //plansJournalier1.setDetailPlansTravails(plansJournalier.getDetailPlansTravails());

        return new ResponseEntity<>(plansJournalierRepository.save(plansJournalier1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/plansJournalier/{id}")
    public ResponseEntity<HttpStatus> deletePlansJournalierById(@PathVariable("id") Long id) {
        plansJournalierRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/plansJournalier")
    public ResponseEntity<HttpStatus> deleteAllPlansJournalier() {
        plansJournalierRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
