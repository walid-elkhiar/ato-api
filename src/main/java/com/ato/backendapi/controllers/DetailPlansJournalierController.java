package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DetailPlansJournalierDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class DetailPlansJournalierController {

    @Autowired
    private PlansJournalierRepository plansJournalierRepository;

    @Autowired
    private DetailPlansJournalierRepository detailPlansJournalierRepository;

    @Autowired
    private SousCodeTraitementRepository sousCodeTraitementRepository;
    @Autowired
    private AffectationPlanRepository affectationPlanRepository;


    //GET
    @GetMapping("/AllDetailPlansJournalier")
    public ResponseEntity<List<DetailPlansJournalierDTO>> getAllDetailPlansJournalier(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<DetailPlansJournalier> detailPlansJournaliers;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<DetailPlansJournalier> pageDetailPlansJournaliers = detailPlansJournalierRepository.findAll(PageRequest.of(offset, pageSize));
            detailPlansJournaliers = pageDetailPlansJournaliers.getContent();
        } else {
            detailPlansJournaliers = detailPlansJournalierRepository.findAll();
        }

        if (detailPlansJournaliers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DetailPlansJournalierDTO> detailPlansJournalierDTOS = new ArrayList<>();
        for (DetailPlansJournalier detailPlansJournalier : detailPlansJournaliers) {
            DetailPlansJournalierDTO detailPlansJournalierDTO = new DetailPlansJournalierDTO();
            detailPlansJournalierDTO.setIdDetailPlansJournalier(detailPlansJournalier.getIdDetailPlansJournalier());
            detailPlansJournalierDTO.setHeureDebut(detailPlansJournalier.getHeureDebut());
            detailPlansJournalierDTO.setHeureFin(detailPlansJournalier.getHeureFin());
            detailPlansJournalierDTO.setObjectif(detailPlansJournalier.getObjectif());
            detailPlansJournalierDTO.setTFin(detailPlansJournalier.getTFin());
            detailPlansJournalierDTO.setTDebut(detailPlansJournalier.getTDebut());
            detailPlansJournalierDTO.setFlexible(detailPlansJournalier.isFlexible());

            // Ajoutez les informations sur le sous code traitement
            SousCodeTraitement sousCodeTraitement = detailPlansJournalier.getSousCodeTraitement();
            if (sousCodeTraitement != null) {
                detailPlansJournalierDTO.setIdSousCodeTraitement(sousCodeTraitement.getIdSousCodeTraitement());
                detailPlansJournalierDTO.setCode(sousCodeTraitement.getCode());
                detailPlansJournalierDTO.setAbsence(sousCodeTraitement.getAbsence());
                //detailPlansJournalierDTO.setPre(sousCodeTraitement.getPresence());
                detailPlansJournalierDTO.setLibellePlanJournalier(sousCodeTraitement.getLibelle());
                detailPlansJournalierDTO.setType(sousCodeTraitement.getType());

                // Ajoutez les informations sur le code traitement
                CodeTraitement codeTraitement = sousCodeTraitement.getCodeTraitement();
                if (codeTraitement != null) {
                    detailPlansJournalierDTO.setIdCodeTraitement(codeTraitement.getIdCodeTraitement());
                    detailPlansJournalierDTO.setCodeCTraitement(codeTraitement.getCode());
                    detailPlansJournalierDTO.setLibelleCTraitement(codeTraitement.getLibelle());
                    detailPlansJournalierDTO.setCouleurCTraitement(codeTraitement.getCouleur());
                }
            }

            // Ajoutez les informations sur le plan Journalier
            PlansJournalier plansJournalier = detailPlansJournalier.getPlansJournalier();
            if (plansJournalier != null) {
                detailPlansJournalierDTO.setIdPlansJournalier(plansJournalier.getIdPlansJournalier());
                detailPlansJournalierDTO.setCodePlanJournalier(plansJournalier.getCode());
                detailPlansJournalierDTO.setLibellePlanJournalier(plansJournalier.getLibelle());
            }

//            // Ajoutez les informations sur l'Affectation Plan
//            AffectationPlan affectationPlan = detailPlansJournalier.getAffectationPlan();
//            if (affectationPlan != null) {
//                detailPlansJournalierDTO.setIdAffectationPlan(affectationPlan.getIdAffectationPlan());
//                detailPlansJournalierDTO.setDatePlan(affectationPlan.getDatePlan());
//                detailPlansJournalierDTO.setDateCycle(affectationPlan.getDateCycle());
//                detailPlansJournalierDTO.setTypePlan(affectationPlan.getTypePlan());
//            }
            detailPlansJournalierDTOS.add(detailPlansJournalierDTO);
        }

        return new ResponseEntity<>(detailPlansJournalierDTOS, HttpStatus.OK);
    }



    //GET
//    @GetMapping("/detailPlansJournalier/{plansJournalierId}/AllDetailPlansJournalier")
//    public ResponseEntity<List<DetailPlansJournalier>> getAllDetailPlansJournalierByPlansJournalierId(@PathVariable(value = "plansJournalierId") Long plansJournalierId) {
//        PlansJournalier plansJournalier = plansJournalierRepository.findById(plansJournalierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        List<DetailPlansJournalier> detailPlansJournaliers = new ArrayList<>();
//        detailPlansJournaliers.addAll(plansJournalier.getDetailPlansJournaliers());
//
//        return new ResponseEntity<>(detailPlansJournaliers, HttpStatus.OK);
//    }

//    @GetMapping("/detailPlansJournalier/{sousCodeTraitementId}/AllDetailPlansJournalier")
//    public ResponseEntity<List<DetailPlansJournalier>> getAllDetailPlansJournalierBySousCodeTraitementId(@PathVariable(value = "sousCodeTraitementId") Long sousCodeTraitementId) {
//        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(sousCodeTraitementId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + sousCodeTraitementId));
//
//        List<DetailPlansJournalier> detailPlansJournaliers = new ArrayList<>();
//        detailPlansJournaliers.addAll(sousCodeTraitement.getDetailPlansJournaliers());
//
//        return new ResponseEntity<>(detailPlansJournaliers, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/detailPlansJournalier/{id}")
    public ResponseEntity<DetailPlansJournalierDTO> getDetailPlanJournalierById(@PathVariable(value = "id") Long id) {
        DetailPlansJournalier detailPlansJournalier = detailPlansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found DetailPlansJournalier with id = " + id));
        if (detailPlansJournalier == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        DetailPlansJournalierDTO detailPlansJournalierDTO = new DetailPlansJournalierDTO();
        detailPlansJournalierDTO.setIdDetailPlansJournalier(detailPlansJournalier.getIdDetailPlansJournalier());
        detailPlansJournalierDTO.setHeureDebut(detailPlansJournalier.getHeureDebut());
        detailPlansJournalierDTO.setHeureFin(detailPlansJournalier.getHeureFin());
        detailPlansJournalierDTO.setObjectif(detailPlansJournalier.getObjectif());
        detailPlansJournalierDTO.setTFin(detailPlansJournalier.getTFin());
        detailPlansJournalierDTO.setTDebut(detailPlansJournalier.getTDebut());
        detailPlansJournalierDTO.setFlexible(detailPlansJournalier.isFlexible());


        // Ajoutez les informations sur le sous code traitement
        SousCodeTraitement sousCodeTraitement = detailPlansJournalier.getSousCodeTraitement();
        if (sousCodeTraitement != null) {
            detailPlansJournalierDTO.setIdSousCodeTraitement(sousCodeTraitement.getIdSousCodeTraitement());
            detailPlansJournalierDTO.setCode(sousCodeTraitement.getCode());
            detailPlansJournalierDTO.setAbsence(sousCodeTraitement.getAbsence());
            //detailPlansJournalierDTO.setPresence(sousCodeTraitement.getPresence());
            detailPlansJournalierDTO.setLibellePlanJournalier(sousCodeTraitement.getLibelle());
            detailPlansJournalierDTO.setType(sousCodeTraitement.getType());
        }

        // Ajoutez les informations sur le plan Journalier
        PlansJournalier plansJournalier = detailPlansJournalier.getPlansJournalier();
        if (plansJournalier != null) {
            detailPlansJournalierDTO.setIdPlansJournalier(plansJournalier.getIdPlansJournalier());
            detailPlansJournalierDTO.setCode(plansJournalier.getCode());
            detailPlansJournalierDTO.setLibellePlanJournalier(plansJournalier.getLibelle());
        }
        // Ajoutez les informations sur l'Affectation Plan
//        AffectationPlan affectationPlan = detailPlansJournalier.getAffectationPlan();
//        if (plansJournalier != null) {
//            detailPlansJournalierDTO.setIdAffectationPlan(affectationPlan.getIdAffectationPlan());
//            detailPlansJournalierDTO.setDatePlan(affectationPlan.getDatePlan());
//            detailPlansJournalierDTO.setDateCycle(affectationPlan.getDateCycle());
//            detailPlansJournalierDTO.setTypePlan(affectationPlan.getTypePlan());
//
//        }

        return new ResponseEntity<>(detailPlansJournalierDTO, HttpStatus.OK);
    }
    //POST
//    @PostMapping("/plansJournalier/{plansJournalierId}/detailPlansJournalier")
//    public ResponseEntity<DetailPlansJournalier> createDetailPlansJournalierByPlansJournalier(@PathVariable(value = "plansJournalierId") Long plansJournalierId,
//                                                   @RequestBody DetailPlansJournalier detailPlansJournalier) {
//        DetailPlansJournalier detailPlansJournalier1 = plansJournalierRepository.findById(plansJournalierId).map(planJour -> {
//            planJour.getDetailPlansJournaliers().add(detailPlansJournalier);
//            return detailPlansJournalierRepository.save(detailPlansJournalier);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        return new ResponseEntity<>(detailPlansJournalier1, HttpStatus.CREATED);
//    }

//    @PostMapping("/sousCodeTraitement/{sousCodeTraitementId}/detailPlansJournalier")
//    public ResponseEntity<DetailPlansJournalier> createDetailPlansJournalierBySousCodeTraitement(@PathVariable(value = "sousCodeTraitementId") Long sousCodeTraitementId,
//                                                                                              @RequestBody DetailPlansJournalier detailPlansJournalier) {
//        DetailPlansJournalier detailPlansJournalier1 = sousCodeTraitementRepository.findById(sousCodeTraitementId).map(sousCodeTrai -> {
//            sousCodeTrai.getDetailPlansJournaliers().add(detailPlansJournalier);
//            return detailPlansJournalierRepository.save(detailPlansJournalier);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + sousCodeTraitementId));
//
//        return new ResponseEntity<>(detailPlansJournalier1, HttpStatus.CREATED);
//    }

    //POST
//    @PostMapping("/detailPlansJournalier")
//    public ResponseEntity<DetailPlansJournalier> addDetailPlansJournalier(@RequestBody DetailPlansJournalier detailPlansJournalier,
//                                                                          @RequestParam Long idPlanJournalier,
//                                                                          @RequestParam Long idSousCodeTraitement) {
//        try {
//            PlansJournalier plansJournalier = plansJournalierRepository.findById(idPlanJournalier)
//                    .orElseThrow(() -> new ResourceNotFoundException("PlanJournalier non trouvé avec l'ID : " + idPlanJournalier));
//            SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(idSousCodeTraitement)
//                    .orElseThrow(() -> new ResourceNotFoundException("SousCodeTraitement non trouvé avec l'ID : " + idSousCodeTraitement));
//
//            detailPlansJournalier.setPlansJournalier(plansJournalier);
//            detailPlansJournalier.setSousCodeTraitement(sousCodeTraitement);
//
//            // Ajout des journaux pour vérifier les valeurs
//            System.out.println("tDebut: " + detailPlansJournalier.getTDebut());
//            System.out.println("tFin: " + detailPlansJournalier.getTFin());
//
//            DetailPlansJournalier savedDetailPlansJournalier = detailPlansJournalierRepository.save(detailPlansJournalier);
//            return new ResponseEntity<>(savedDetailPlansJournalier, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PostMapping("/detailPlansJournalier")
    public ResponseEntity<String> addDetailPlansJournalier(@RequestBody DetailPlansJournalier detailPlansJournalier,
                                                           @RequestParam Long idPlanJournalier,
                                                           @RequestParam Long idSousCodeTraitement) {
        try {
            // Récupérer les entités PlansJournalier et SousCodeTraitement
            PlansJournalier plansJournalier = plansJournalierRepository.findById(idPlanJournalier)
                    .orElseThrow(() -> new ResourceNotFoundException("PlanJournalier non trouvé avec l'ID : " + idPlanJournalier));
            SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(idSousCodeTraitement)
                    .orElseThrow(() -> new ResourceNotFoundException("SousCodeTraitement non trouvé avec l'ID : " + idSousCodeTraitement));

            // Vérifier le chevauchement des heures
            List<DetailPlansJournalier> existingDetails = detailPlansJournalierRepository.findByPlansJournalierAndSousCodeTraitement(
                    plansJournalier, sousCodeTraitement);

            LocalTime newHeureDebut = detailPlansJournalier.getHeureDebut();
            LocalTime newHeureFin = detailPlansJournalier.getHeureFin();

            for (DetailPlansJournalier existingDetail : existingDetails) {
                LocalTime existingHeureDebut = existingDetail.getHeureDebut();
                LocalTime existingHeureFin = existingDetail.getHeureFin();

                // Vérifier si l'intervalle se chevauche avec l'un des détails existants
                if ((newHeureDebut.isBefore(existingHeureFin) && newHeureFin.isAfter(existingHeureDebut)) ||
                        newHeureDebut.equals(existingHeureDebut) || newHeureFin.equals(existingHeureFin)) {
                    return new ResponseEntity<>("Erreur : L'intervalle horaire est déjà sauvegardé dans un autre détail plan journalier.", HttpStatus.BAD_REQUEST);
                }
            }
            // Si aucun chevauchement n'a été trouvé, associer les entités et sauvegarder
            detailPlansJournalier.setPlansJournalier(plansJournalier);
            detailPlansJournalier.setSousCodeTraitement(sousCodeTraitement);

            detailPlansJournalierRepository.save(detailPlansJournalier);
            return new ResponseEntity<>("Détail plan journalier ajouté avec succès.", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Erreur interne du serveur : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //PUT
    @PutMapping("/detailPlansJournalier/{id}")
    public ResponseEntity<DetailPlansJournalier> updateDetailPlansJournalier(@PathVariable("id") Long id,
                                                                             @RequestBody DetailPlansJournalier detailPlansJournalierReq,
                                                                             @RequestParam Long idPlanJournalier,
                                                                             @RequestParam Long idSousCodeTraitement){
        DetailPlansJournalier detailPlansJournalier = detailPlansJournalierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetailPlansJournalierId " + id + "not found"));

        PlansJournalier plansJournalier = plansJournalierRepository.findById(idPlanJournalier)
                .orElseThrow(() -> new ResourceNotFoundException("PlanJournalier non trouvé avec l'ID : " + idPlanJournalier));
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(idSousCodeTraitement)
                .orElseThrow(() -> new ResourceNotFoundException("SousCodeTraitement non trouvé avec l'ID : " + idSousCodeTraitement));

        detailPlansJournalier.setPlansJournalier(plansJournalier);
        detailPlansJournalier.setSousCodeTraitement(sousCodeTraitement);

        detailPlansJournalier.setFlexible(detailPlansJournalierReq.isFlexible());
        detailPlansJournalier.setObjectif(detailPlansJournalierReq.getObjectif());
        detailPlansJournalier.setHeureDebut(detailPlansJournalierReq.getHeureDebut());
        detailPlansJournalier.setTDebut(detailPlansJournalierReq.getTDebut());
        detailPlansJournalier.setTFin(detailPlansJournalierReq.getTFin());
        detailPlansJournalier.setHeureFin(detailPlansJournalierReq.getHeureFin());

        return new ResponseEntity<>(detailPlansJournalierRepository.save(detailPlansJournalier), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/detailPlansJournalier/{id}")
    public ResponseEntity<HttpStatus> deleteDetailPlansJournalier(@PathVariable("id") Long id) {
        detailPlansJournalierRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/detailPlansJournalier")
    public ResponseEntity<HttpStatus> deleteAllDetailPlansJournalier() {
        detailPlansJournalierRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/plansJournalier/{plansJournalierId}/detailPlansJournalier")
//    public ResponseEntity<List<DetailPlansJournalier>> deleteAllDetailPlansJournalierOfPlanJournalier(@PathVariable(value = "plansJournalierId") Long plansJournalierId) {
//        PlansJournalier plansJournalier = plansJournalierRepository.findById(plansJournalierId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlansJournalier with id = " + plansJournalierId));
//
//        plansJournalier.removeDetailPlansJournalier();
//        plansJournalierRepository.save(plansJournalier);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/sousCodeTraitement/{sousCodeTraitementId}/detailPlansJournalier")
//    public ResponseEntity<List<DetailPlansJournalier>> deleteAllDetailPlansJournalierOfSousCodeTraitement(@PathVariable(value = "sousCodeTraitementId") Long sousCodeTraitementId) {
//        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(sousCodeTraitementId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found SousCodeTraitement with id = " + sousCodeTraitementId));
//
//        sousCodeTraitement.removeDetailPlansJournaliers();
//        sousCodeTraitementRepository.save(sousCodeTraitement);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }



}
