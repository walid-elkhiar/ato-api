package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DetailPlansTravailDTO;
import com.ato.backendapi.entities.DetailPlansTravail;
import com.ato.backendapi.entities.PlansTravail;
import com.ato.backendapi.repositories.PlansTravailRepository;
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
public class PlansTravailController {

    @Autowired
    private PlansTravailRepository plansTravailRepository;


    //GET
    @GetMapping("/AllPlansTravail")
    public ResponseEntity<List<PlansTravail>> listePlansTravail(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<PlansTravail> plansTravails;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<PlansTravail> plansTravailPage = plansTravailRepository.findAll(PageRequest.of(offset, pageSize));
            plansTravails = plansTravailPage.getContent();
        } else {
            plansTravails = plansTravailRepository.findAll();
        }

        if (plansTravails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(plansTravails, HttpStatus.OK);
    }




    @GetMapping("/plansTravail/{id}")
    public ResponseEntity<PlansTravail> getPlansTravailById(@PathVariable("id") Long id) {
        PlansTravail plansTravail = plansTravailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansTravail with id = " + id));

        return new ResponseEntity<>(plansTravail, HttpStatus.OK);
    }

    @GetMapping("/plansTravail/{id}/detailPlansTravail")
    public ResponseEntity<List<DetailPlansTravailDTO>> getDetailPlansTravailByPlanId(@PathVariable(value = "id") Long id) {
        PlansTravail plansTravail = plansTravailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlansTravail non trouvé avec l'ID : " + id));

        List<DetailPlansTravail> detailPlansTravailList = plansTravail.getDetailPlansTravail();
        if (detailPlansTravailList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Convertir en DTO
        List<DetailPlansTravailDTO> detailPlansTravailDTOs = detailPlansTravailList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(detailPlansTravailDTOs, HttpStatus.OK);
    }

    private DetailPlansTravailDTO convertToDTO(DetailPlansTravail detailPlansTravail) {
        DetailPlansTravailDTO detailPlansTravailDTO = new DetailPlansTravailDTO();
        detailPlansTravailDTO.setIdDetailPlansTravail(detailPlansTravail.getIdDetailPlansTravail());
        detailPlansTravailDTO.setNomJour(detailPlansTravail.getNomJour());

        PlansTravail plansTravail = detailPlansTravail.getPlansTravail();
        if (plansTravail != null) {
            detailPlansTravailDTO.setIdPlansTravail(plansTravail.getIdPlansTravail());
            detailPlansTravailDTO.setCodePlanTravail(plansTravail.getCode());
            detailPlansTravailDTO.setLibellePlanTravail(plansTravail.getLibelle());
            detailPlansTravailDTO.setType(plansTravail.getType());
            detailPlansTravailDTO.setNbrJour(plansTravail.getNbrJour());
        }

        return detailPlansTravailDTO;
    }

    //POST
    @PostMapping("/plansTravail")
    public ResponseEntity<?> addPlansTravail(@RequestBody PlansTravail plansTravail) {
        try {
            PlansTravail savedPlansTravail = plansTravailRepository.save(plansTravail);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Plan de travail créé avec succès."); // Message de succès
            responseObject.put("data", savedPlansTravail); // Les informations du plan de travail créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du plan de travail
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du plan de travail."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }



    //PUT
    @PutMapping("/plansTravail/{id}")
    public ResponseEntity<PlansTravail> editPlansTravail(@PathVariable("id") Long id, @RequestBody PlansTravail plansTravail){
        PlansTravail plansTravail1 = plansTravailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found PlanTravail with id = " + id));
        //plansTravail1.setAffectationPlans(plansTravail.getAffectationPlans());
        plansTravail1.setCode(plansTravail.getCode());
        //plansTravail1.setDetailPlansTravails(plansTravail.getDetailPlansTravails());
        plansTravail1.setLibelle(plansTravail.getLibelle());
        plansTravail1.setNbrJour(plansTravail.getNbrJour());
        plansTravail1.setNbrJour(plansTravail.getNbrJour());

        return new ResponseEntity<>(plansTravailRepository.save(plansTravail1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/plansTravail/{id}")
    public ResponseEntity<HttpStatus> deletePlansTravailById(@PathVariable("id") Long id) {
        plansTravailRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/plansTravail")
    public ResponseEntity<HttpStatus> deleteAllPlansTravail() {
        plansTravailRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
