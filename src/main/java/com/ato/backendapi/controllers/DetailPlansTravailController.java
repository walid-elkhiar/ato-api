package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DetailPlansTravailDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.DetailPlansTravailRepository;
import com.ato.backendapi.repositories.PlansJournalierRepository;
import com.ato.backendapi.repositories.PlansTravailRepository;
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
public class DetailPlansTravailController {

    @Autowired
    private PlansJournalierRepository plansJournalierRepository;

    @Autowired
    private DetailPlansTravailRepository detailPlansTravailRepository;

    @Autowired
    private PlansTravailRepository plansTravailRepository;


    //GET
    @GetMapping("/AllDetailPlansTravail")
    public ResponseEntity<List<DetailPlansTravailDTO>> getAllDetailPlansTravail(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<DetailPlansTravail> detailPlansTravails;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<DetailPlansTravail> pageDetailPlansTravails = detailPlansTravailRepository.findAll(PageRequest.of(offset, pageSize));
            detailPlansTravails = pageDetailPlansTravails.getContent();
        } else {
            detailPlansTravails = detailPlansTravailRepository.findAll();
        }

        if (detailPlansTravails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DetailPlansTravailDTO> detailPlansTravailDTOS = new ArrayList<>();
        for (DetailPlansTravail detailPlansTravail : detailPlansTravails) {
            DetailPlansTravailDTO detailPlansTravailDTO = new DetailPlansTravailDTO();
            detailPlansTravailDTO.setIdDetailPlansTravail(detailPlansTravail.getIdDetailPlansTravail());
            detailPlansTravailDTO.setNomJour(detailPlansTravail.getNomJour());

            // Ajoutez les informations sur le PlansTravail
            PlansTravail plansTravail = detailPlansTravail.getPlansTravail();
            if (plansTravail != null) {
                detailPlansTravailDTO.setIdPlansTravail(plansTravail.getIdPlansTravail());
                detailPlansTravailDTO.setType(plansTravail.getType());
                detailPlansTravailDTO.setCodePlanTravail(plansTravail.getCode());
                detailPlansTravailDTO.setNbrJour(plansTravail.getNbrJour());
                detailPlansTravailDTO.setLibellePlanTravail(plansTravail.getLibelle());
            }

            // Ajoutez les informations sur le plan Journalier
            PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
            if (plansJournalier != null) {
                detailPlansTravailDTO.setIdPlansJournalier(plansJournalier.getIdPlansJournalier());
                detailPlansTravailDTO.setCodePlansJournalier(plansJournalier.getCode());
                detailPlansTravailDTO.setLibellePlansJournalier(plansJournalier.getLibelle());
            }

            detailPlansTravailDTOS.add(detailPlansTravailDTO);
        }

        return new ResponseEntity<>(detailPlansTravailDTOS, HttpStatus.OK);
    }

    //GET
//    @GetMapping("/detailPlansTravail/{plansJournalierId}/AllDetailPlansTravail")
//    public ResponseEntity<List<DetailPlansTravail>> getAllDetailPlansTravailByPlansJournalierId(@PathVariable(value = "plansJournalierId") Long plansJournalierId) {
//        PlansJournalier plansJournalier = plansJournalierRepository.findById(plansJournalierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        List<DetailPlansTravail> detailPlansTravails = new ArrayList<>();
//        detailPlansTravails.addAll(plansJournalier.getDetailPlansTravails());
//
//        return new ResponseEntity<>(detailPlansTravails, HttpStatus.OK);
//    }

//    @GetMapping("/detailPlansTravail/{plansTravailId}/AllDetailPlansTravail")
//    public ResponseEntity<List<DetailPlansTravail>> getAllDetailPlansTravailByPlansTravailId(@PathVariable(value = "plansTravailId") Long plansTravailId) {
//        PlansTravail plansTravail = plansTravailRepository.findById(plansTravailId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansTravail with id = " + plansTravailId));
//
//        List<DetailPlansTravail> detailPlansTravails = new ArrayList<>();
//        detailPlansTravails.addAll(plansTravail.getDetailPlansTravails());
//
//        return new ResponseEntity<>(detailPlansTravails, HttpStatus.OK);
//    }

    //GET
//    @GetMapping("/detailPlansTravail/{id}")
//    public ResponseEntity<DetailPlansTravail> getDetailPlansTravailById(@PathVariable(value = "id") Long id) {
//        DetailPlansTravail detailPlansTravail = detailPlansTravailRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found DetailPlansTravail with id = " + id));
//
//        return new ResponseEntity<>(detailPlansTravail, HttpStatus.OK);
//    }
    //POST
//    @PostMapping("/plansJournalier/{plansJournalierId}/detailPlansTravail")
//    public ResponseEntity<DetailPlansTravail> createDetailPlansTravailByPlansJournalier(@PathVariable(value = "plansJournalierId") Long plansJournalierId,
//                                                                                              @RequestBody DetailPlansTravail detailPlansTravail) {
//        DetailPlansTravail detailPlansTravail1 = plansJournalierRepository.findById(plansJournalierId).map(planJour -> {
//            planJour.getDetailPlansTravails().add(detailPlansTravail);
//            return detailPlansTravailRepository.save(detailPlansTravail);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        return new ResponseEntity<>(detailPlansTravail1, HttpStatus.CREATED);
//    }

//    @PostMapping("/plansTravail/{plansTravailId}/detailPlansTravail")
//    public ResponseEntity<DetailPlansTravail> createDetailPlansTravailByPlansTravail(@PathVariable(value = "plansTravailId") Long plansTravailId,
//                                                                                        @RequestBody DetailPlansTravail detailPlansTravail) {
//        DetailPlansTravail detailPlansTravail1 = plansTravailRepository.findById(plansTravailId).map(planTrav -> {
//            planTrav.getDetailPlansTravails().add(detailPlansTravail);
//            return detailPlansTravailRepository.save(detailPlansTravail);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found PlansTravail with id = " + plansTravailId));
//
//        return new ResponseEntity<>(detailPlansTravail1, HttpStatus.CREATED);
//    }

    @GetMapping("/detailPlansTravail/{id}")
    public ResponseEntity<DetailPlansTravailDTO> getDetailPlansTravailById(@PathVariable(value = "id") Long id){
    DetailPlansTravail detailPlansTravail = detailPlansTravailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found DetailPlansJournalier with id = " + id));
        if (detailPlansTravail == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        DetailPlansTravailDTO detailPlansTravailDTO = new DetailPlansTravailDTO();
        detailPlansTravailDTO.setIdDetailPlansTravail(detailPlansTravail.getIdDetailPlansTravail());
        detailPlansTravailDTO.setNomJour(detailPlansTravail.getNomJour());

        // Ajoutez les informations sur le PlansTravail
        PlansTravail plansTravail = detailPlansTravail.getPlansTravail();
        if (plansTravail != null) {
            detailPlansTravailDTO.setIdPlansTravail(plansTravail.getIdPlansTravail());
            detailPlansTravailDTO.setType(plansTravail.getType());
            detailPlansTravailDTO.setCodePlanTravail(plansTravail.getCode());
            detailPlansTravailDTO.setNbrJour(plansTravail.getNbrJour());
            detailPlansTravailDTO.setLibellePlanTravail(plansTravail.getLibelle());

        }

        // Ajoutez les informations sur le plan Journalier
        PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
        if (plansJournalier != null) {
            detailPlansTravailDTO.setIdPlansJournalier(plansJournalier.getIdPlansJournalier());
            detailPlansTravailDTO.setCodePlansJournalier(plansJournalier.getCode());
            detailPlansTravailDTO.setLibellePlansJournalier(plansJournalier.getLibelle());
        }

        return new ResponseEntity<>(detailPlansTravailDTO, HttpStatus.OK);
    }

    //POST
    @PostMapping("/detailPlansTravail")
    public ResponseEntity<?> addDetailPlansTravail(@RequestBody DetailPlansTravail detailPlansTravail,
                                 @RequestParam Long idPlanJournalier,
                                 @RequestParam Long idPlanTravail) {
        try {
            PlansJournalier plansJournalier = plansJournalierRepository.findById(idPlanJournalier)
                    .orElseThrow(() -> new ResourceNotFoundException("PlanJournalier non trouvé avec l'ID : " + idPlanJournalier));
            PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
                    .orElseThrow(() -> new ResourceNotFoundException("PlanTravail non trouvé avec l'ID : " + idPlanTravail));
            detailPlansTravail.setPlansJournalier(plansJournalier);
            detailPlansTravail.setPlansTravail(plansTravail);

            DetailPlansTravail savedDetailPlansTravail = detailPlansTravailRepository.save(detailPlansTravail);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Détail de plan de travail créé avec succès."); // Message de succès
            responseObject.put("data", savedDetailPlansTravail); // Les informations du détail de plan de travail créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du détail de plan de travail
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du détail de plan de travail."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    //PUT
    @PutMapping("/detailPlansTravail/{id}")
    public ResponseEntity<DetailPlansTravail> updateDetailPlansTravail(@PathVariable("id") Long id,
                                                                       @RequestBody DetailPlansTravail detailPlansTravailReq,
                                                                       @RequestParam Long idPlanJournalier,
                                                                       @RequestParam Long idPlanTravail){
        DetailPlansTravail detailPlansTravail  = detailPlansTravailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetailPlansJournalierId " + id + "not found"));

        PlansJournalier plansJournalier = plansJournalierRepository.findById(idPlanJournalier)
                .orElseThrow(() -> new ResourceNotFoundException("PlanJournalier non trouvé avec l'ID : " + idPlanJournalier));
        PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
                .orElseThrow(() -> new ResourceNotFoundException("PlansTravail non trouvé avec l'ID : " + idPlanTravail));
        detailPlansTravail.setPlansJournalier(plansJournalier);
        detailPlansTravail.setPlansTravail(plansTravail);

        detailPlansTravail.setNomJour(detailPlansTravailReq.getNomJour());

        return new ResponseEntity<>(detailPlansTravailRepository.save(detailPlansTravail), HttpStatus.OK);
    }


    //DELETE
    @DeleteMapping("/detailPlansTravail/{id}")
    public ResponseEntity<HttpStatus> deleteDetailPlansTravail(@PathVariable("id") Long id) {
        detailPlansTravailRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/detailPlansTravail")
    public ResponseEntity<HttpStatus> deleteAllDetailPlansTravail() {
        detailPlansTravailRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/plansJournalier/{plansJournalierId}/detailPlansTravail")
//    public ResponseEntity<List<DetailPlansTravail>> deleteAllDetailPlansTravailOfPlanJournalier(@PathVariable(value = "plansJournalierId") Long plansJournalierId) {
//        PlansJournalier plansJournalier = plansJournalierRepository.findById(plansJournalierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        plansJournalier.removeDetailPlansTravail();
//        plansJournalierRepository.save(plansJournalier);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/plansTravail/{plansTravailId}/detailPlansTravail")
//    public ResponseEntity<List<DetailPlansTravail>> deleteAllDetailPlansTravailOfPlanTravail(@PathVariable(value = "plansTravailId") Long plansTravailId) {
//        PlansTravail plansTravail = plansTravailRepository.findById(plansTravailId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansTravail with id = " + plansTravailId));
//
//        plansTravail.removeDetailPlansTravails();
//        plansTravailRepository.save(plansTravail);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }



}
