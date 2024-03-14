package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.PointageDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.AffectationPlanRepository;
import com.ato.backendapi.repositories.PointageRepository;
import com.ato.backendapi.repositories.UtilisateursRepository;
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
public class PointageController {
    @Autowired
    private AffectationPlanRepository affectationPlanRepository;

    @Autowired
    private PointageRepository pointageRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;


    //GET
//    @GetMapping("/AllPointages")
//        List<Pointage> pointages = new ArrayList<Pointage>();
//
//        pointageRepository.findAll().forEach(pointages::add);
//        if (pointages.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(pointages, HttpStatus.OK);
//    }

    @GetMapping("/AllPointages")
    public ResponseEntity<List<PointageDTO>> getAllPointages(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Pointage> pointages;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Pointage> pointagesPage = pointageRepository.findAll(PageRequest.of(offset, pageSize));
            pointages = pointagesPage.getContent();
        } else {
            pointages = pointageRepository.findAll();
        }

        if (pointages.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<PointageDTO> pointageDTOS = new ArrayList<>();
        for (Pointage pointage : pointages) {
            PointageDTO pointageDTO = new PointageDTO();
            pointageDTO.setIdPointage(pointage.getIdPointage());
            pointageDTO.setDatePointage(pointage.getDatePointage());
            pointageDTO.setType(pointage.getType());
            pointageDTO.setDescriptions(pointage.getDescriptions());
            pointageDTO.setDevice(pointage.getDevice());
            pointageDTO.setAdresseIp(pointage.getAdresseIp());

            // Ajoutez les informations sur l'utilisateur
            Utilisateurs utilisateurs = pointage.getUtilisateurs();
            if (utilisateurs != null) {
                pointageDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
                pointageDTO.setMatricule(utilisateurs.getMatricule());
                pointageDTO.setNom(utilisateurs.getNom());
                pointageDTO.setPrenom(utilisateurs.getPrenom());
                pointageDTO.setAdresseMail(utilisateurs.getAdresseMail());
                pointageDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
                pointageDTO.setTelephone(utilisateurs.getTelephone());
                pointageDTO.setDateEntree(utilisateurs.getDateEntree());
                pointageDTO.setActif(utilisateurs.isActif());
                pointageDTO.setPhoto(utilisateurs.getPhoto());
                pointageDTO.setPassword(utilisateurs.getPassword());
                pointageDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
                pointageDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
                pointageDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
                pointageDTO.setRole(utilisateurs.getRole());
            }

            // Ajoutez les informations sur AffectationPlan
            AffectationPlan affectationPlan = pointage.getAffectationPlan();
            if (affectationPlan != null) {
                pointageDTO.setIdAffectationPlan(affectationPlan.getIdAffectationPlan());
                pointageDTO.setTypePlan(affectationPlan.getTypePlan());
                pointageDTO.setDatePlan(affectationPlan.getDatePlan());
                pointageDTO.setDateCycle(affectationPlan.getDateCycle());
            }

            pointageDTOS.add(pointageDTO);
        }

        return new ResponseEntity<>(pointageDTOS, HttpStatus.OK);
    }

    //GET
//    @GetMapping("/pointages/{affectationId}/AllPointages")
//    public ResponseEntity<List<Pointage>> getAllPointagesByAffectationId(@PathVariable(value = "affectationId") Long affectationId) {
//        AffectationPlan affectationPlan = affectationPlanRepository.findById(affectationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + affectationId));
//
//        List<Pointage> pointages = new ArrayList<>();
//        pointages.addAll(affectationPlan.getPointages());
//
//        return new ResponseEntity<>(pointages, HttpStatus.OK);
//    }

//    @GetMapping("/pointages/{userId}/AllPointages")
//    public ResponseEntity<List<Pointage>> getAllPointagesByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        List<Pointage> pointages = new ArrayList<>();
//        pointages.addAll(utilisateurs.getPointages());
//
//        return new ResponseEntity<>(pointages, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/pointages/{id}")
    public ResponseEntity<PointageDTO> getPointageById(@PathVariable(value = "id") Long id) {
        Pointage pointage = pointageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Pointage with id = " + id));

        if (pointage == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        PointageDTO pointageDTO = new PointageDTO();
        pointageDTO.setIdPointage(pointage.getIdPointage());
        pointageDTO.setDatePointage(pointage.getDatePointage());
        pointageDTO.setType(pointage.getType());
        pointageDTO.setDescriptions(pointage.getDescriptions());
        pointageDTO.setDevice(pointage.getDevice());
        pointageDTO.setAdresseIp(pointage.getAdresseIp());


        // Ajoutez les informations sur l'utilisateur
        Utilisateurs utilisateurs = pointage.getUtilisateurs();
        if (utilisateurs != null) {
            pointageDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
            pointageDTO.setMatricule(utilisateurs.getMatricule());
            pointageDTO.setNom(utilisateurs.getNom());
            pointageDTO.setPrenom(utilisateurs.getPrenom());
            pointageDTO.setAdresseMail(utilisateurs.getAdresseMail());
            pointageDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
            pointageDTO.setTelephone(utilisateurs.getTelephone());
            pointageDTO.setDateEntree(utilisateurs.getDateEntree());
            pointageDTO.setActif(utilisateurs.isActif());
            pointageDTO.setPhoto(utilisateurs.getPhoto());
            pointageDTO.setPassword(utilisateurs.getPassword());
            pointageDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
            pointageDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
            pointageDTO.setDateEntree(utilisateurs.getDateEntree());
            pointageDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
            pointageDTO.setRole(utilisateurs.getRole());

        }

        // Ajoutez les informations sur AffectationPlan
        AffectationPlan affectationPlan = pointage.getAffectationPlan();
        if (affectationPlan != null) {
            pointageDTO.setIdAffectationPlan(affectationPlan.getIdAffectationPlan());
            pointageDTO.setDatePlan(affectationPlan.getDatePlan());
            pointageDTO.setTypePlan(affectationPlan.getTypePlan());
            pointageDTO.setDateCycle(affectationPlan.getDateCycle());
        }



        return new ResponseEntity<>(pointageDTO, HttpStatus.OK);
    }


//    @PostMapping("/affectations/{affectationId}/pointages")
//    public ResponseEntity<Pointage> createPointage(@PathVariable(value = "affectationId") Long affectationId,
//                                                           @RequestBody Pointage pointage) {
//        Pointage pointage1 = affectationPlanRepository.findById(affectationId).map(aff -> {
//            aff.getPointages().add(pointage);
//            return pointageRepository.save(pointage);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + affectationId));
//
//        return new ResponseEntity<>(pointage1, HttpStatus.CREATED);
//    }

//    @PostMapping("/users/{userId}/pointages")
//    public ResponseEntity<Pointage> createPointageByUser(@PathVariable(value = "userId") Long userId,
//                                                   @RequestBody Pointage pointage) {
//        Pointage pointage1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getPointages().add(pointage);
//            return pointageRepository.save(pointage);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        return new ResponseEntity<>(pointage1, HttpStatus.CREATED);
//    }


    //POST
    @PostMapping("/pointages")
    public ResponseEntity<?> addPointage(@RequestBody Pointage pointage,
                                 @RequestParam Long idUser,
                                 @RequestParam Long idAffectation) {
        try {
            Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
            AffectationPlan affectationPlan = affectationPlanRepository.findById(idAffectation)
                    .orElseThrow(() -> new ResourceNotFoundException("AffectationPlan non trouvé avec l'ID : " + idAffectation));

            pointage.setUtilisateurs(utilisateurs);
            pointage.setAffectationPlan(affectationPlan);

            Pointage savedPointage = pointageRepository.save(pointage);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Pointage créé avec succès."); // Message de succès
            responseObject.put("data", savedPointage); // Les informations du pointage créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du pointage
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du pointage."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    @PutMapping("/pointages/{id}")
    public ResponseEntity<Pointage> updatePointage(@PathVariable("id") Long id,
                                                   @RequestBody Pointage pointageReq,
                                                   @RequestParam Long idUser,
                                                   @RequestParam Long idAffectation) {
        Pointage pointage = pointageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PointageId " + id + "not found"));
        AffectationPlan affectationPlan = affectationPlanRepository.findById(idAffectation)
                .orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + idAffectation));
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        pointage.setUtilisateurs(utilisateurs);
        pointage.setAffectationPlan(affectationPlan);
        pointage.setDatePointage(pointageReq.getDatePointage());
        pointage.setType(pointageReq.getType());
        pointage.setDescriptions(pointageReq.getDescriptions());
        pointage.setDevice(pointageReq.getDevice());
        pointage.setAdresseIp(pointageReq.getAdresseIp());


        return new ResponseEntity<>(pointageRepository.save(pointage), HttpStatus.OK);
    }

    @DeleteMapping("/pointages/{id}")
    public ResponseEntity<HttpStatus> deletePointage(@PathVariable("id") Long id) {
        pointageRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/pointages")
    public ResponseEntity<HttpStatus> deleteAllPointages() {
        pointageRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/affectations/{affectationId}/pointages")
//    public ResponseEntity<List<Pointage>> deleteAllPointagesOfAffectation(@PathVariable(value = "affectationId") Long affectationId) {
//        AffectationPlan affectationPlan = affectationPlanRepository.findById(affectationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + affectationId));
//
//        affectationPlan.removePointages();
//        affectationPlanRepository.save(affectationPlan);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/users/{userId}/pointages")
//    public ResponseEntity<List<Pointage>> deleteAllPointagesOfUser(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        utilisateurs.removePointages();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }


}
