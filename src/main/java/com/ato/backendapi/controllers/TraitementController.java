package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.TraitementDTO;
import com.ato.backendapi.entities.Traitement;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.TraitementRepository;
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
public class TraitementController {

    @Autowired
    private TraitementRepository traitementRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllTraitements")
    public ResponseEntity<List<TraitementDTO>> listeTraitements(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Traitement> traitements;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Traitement> traitementsPage = traitementRepository.findAll(PageRequest.of(offset, pageSize));
            traitements = traitementsPage.getContent();
        } else {
            traitements = traitementRepository.findAll();
        }

        if (traitements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<TraitementDTO> traitementDTOS = new ArrayList<>();
        for (Traitement traitement : traitements) {
            TraitementDTO traitementDTO = new TraitementDTO();
            traitementDTO.setIdTraitement(traitement.getIdTraitement());
            traitementDTO.set_25(traitement.get_25());
            traitementDTO.set_50(traitement.get_50());
            traitementDTO.set_100(traitement.get_100());
            traitementDTO.setAbsence(traitement.getAbsence());
            traitementDTO.setAbsenceJustifiee(traitement.getAbsenceJustifiee());
            traitementDTO.setDateTraitement(traitement.getDateTraitement());
            traitementDTO.setDej(traitement.getDej());
            traitementDTO.setDiff(traitement.getDiff());
            traitementDTO.setIndifinie(traitement.getIndifinie());
            traitementDTO.setJourneePointage(traitement.getJourneePointage());
            traitementDTO.setJustificationAbsence(traitement.getJustificationAbsence());
            traitementDTO.setJrn(traitement.getJrn());
            traitementDTO.setObjectif(traitement.getObjectif());
            traitementDTO.setPresence(traitement.getPresence());
            traitementDTO.setPointage(traitement.getPointage());
            traitementDTO.setRetard(traitement.getRetard());
            traitementDTO.setTolerance(traitement.getTolerance());
            traitementDTO.setTrn(traitement.getTrn());
            traitementDTO.setTypeTraitement(traitement.getTypeTraitement());

            // Ajoutez les informations sur l'utilisateur
            Utilisateurs utilisateurs = traitement.getUtilisateurs();
            if (utilisateurs != null) {
                traitementDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
                traitementDTO.setMatricule(utilisateurs.getMatricule());
                traitementDTO.setNom(utilisateurs.getNom());
                traitementDTO.setPrenom(utilisateurs.getPrenom());
                traitementDTO.setAdresseMail(utilisateurs.getAdresseMail());
                traitementDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
                traitementDTO.setTelephone(utilisateurs.getTelephone());
                traitementDTO.setDateEntree(utilisateurs.getDateEntree());
                traitementDTO.setActif(utilisateurs.isActif());
                traitementDTO.setPhoto(utilisateurs.getPhoto());
                traitementDTO.setPassword(utilisateurs.getPassword());
                traitementDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
                traitementDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
                traitementDTO.setDateEntree(utilisateurs.getDateEntree());
                traitementDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
                traitementDTO.setRole(utilisateurs.getRole());
            }

            traitementDTOS.add(traitementDTO);
        }

        return new ResponseEntity<>(traitementDTOS, HttpStatus.OK);
    }


//    @GetMapping("/traitements/{userId}/AllTraitements")
//    public ResponseEntity<List<Traitement>> getAllTraitementsByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<Traitement> traitements = new ArrayList<>();
//        traitements.addAll(utilisateurs.getTraitements());
//
//        return new ResponseEntity<>(traitements, HttpStatus.OK);
//    }

    @GetMapping("/traitements/{id}")
    public ResponseEntity<TraitementDTO> getTraitementById(@PathVariable("id") Long id) {
        Traitement traitement = traitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Traitement with id = " + id));

        if (traitement == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        TraitementDTO traitementDTO = new TraitementDTO();
        traitementDTO.set_25(traitement.get_25());
        traitementDTO.set_50(traitement.get_50());
        traitementDTO.set_100(traitement.get_100());
        traitementDTO.setAbsence(traitement.getAbsence());
        traitementDTO.setAbsenceJustifiee(traitement.getAbsenceJustifiee());
        traitementDTO.setDateTraitement(traitement.getDateTraitement());
        traitementDTO.setDej(traitement.getDej());
        traitementDTO.setDiff(traitement.getDiff());
        traitementDTO.setIndifinie(traitement.getIndifinie());
        traitementDTO.setJourneePointage(traitement.getJourneePointage());
        traitementDTO.setJustificationAbsence(traitement.getJustificationAbsence());
        traitementDTO.setJrn(traitement.getJrn());
        traitementDTO.setObjectif(traitement.getObjectif());
        traitementDTO.setPresence(traitement.getPresence());
        traitementDTO.setPointage(traitement.getPointage());
        traitementDTO.setRetard(traitement.getRetard());
        traitementDTO.setTolerance(traitement.getTolerance());
        traitementDTO.setTrn(traitement.getTrn());
        traitementDTO.setTypeTraitement(traitement.getTypeTraitement());


        // Ajoutez les informations sur l'utilisateur
        Utilisateurs utilisateurs = traitement.getUtilisateurs();
        if (utilisateurs != null) {
            traitementDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
            traitementDTO.setMatricule(utilisateurs.getMatricule());
            traitementDTO.setNom(utilisateurs.getNom());
            traitementDTO.setPrenom(utilisateurs.getPrenom());
            traitementDTO.setAdresseMail(utilisateurs.getAdresseMail());
            traitementDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
            traitementDTO.setTelephone(utilisateurs.getTelephone());
            traitementDTO.setDateEntree(utilisateurs.getDateEntree());
            traitementDTO.setActif(utilisateurs.isActif());
            traitementDTO.setPhoto(utilisateurs.getPhoto());
            traitementDTO.setPassword(utilisateurs.getPassword());
            traitementDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
            traitementDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
            traitementDTO.setDateEntree(utilisateurs.getDateEntree());
            traitementDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
            traitementDTO.setRole(utilisateurs.getRole());

        }

        return new ResponseEntity<>(traitementDTO, HttpStatus.OK);
    }

    //POST
    @PostMapping("/traitements")
    public ResponseEntity<?> addTraitement(@RequestBody Traitement traitement,
                                 @RequestParam Long idUser) {
        try {
            Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
            traitement.setUtilisateurs(utilisateurs);
            Traitement t = traitementRepository.save(traitement);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Traitement créé avec succès."); // Message de succès
            responseObject.put("data", t); // Les informations du traitement créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du traitement
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du traitement."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


//    @PostMapping("/users/{userId}/traitements")
//    public ResponseEntity<Traitement> createTraitement(@PathVariable(value = "userId") Long userId,
//                                                       @RequestBody Traitement traitement) {
//        Traitement traitement1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getTraitements().add(traitement);
//            return traitementRepository.save(traitement);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(traitement1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/traitements/{id}")
    public ResponseEntity<Traitement> editTraitement(@PathVariable("id") Long id, @RequestBody Traitement traitement,
                                           @RequestParam Long idUser){
        Traitement traitement1 = traitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Traitement with id = " + id));
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        traitement1.setUtilisateurs(utilisateurs);
        traitement1.set_25(traitement.get_25());
        traitement1.set_50(traitement.get_50());
        traitement1.set_100(traitement.get_100());
        traitement1.setAbsence(traitement.getAbsence());
        traitement1.setAbsenceJustifiee(traitement.getAbsenceJustifiee());
        traitement1.setDateTraitement(traitement.getDateTraitement());
        traitement1.setDej(traitement.getDej());
        traitement1.setDiff(traitement.getDiff());
        traitement1.setIndifinie(traitement.getIndifinie());
        traitement1.setJourneePointage(traitement.getJourneePointage());
        traitement1.setJustificationAbsence(traitement.getJustificationAbsence());
        traitement1.setJrn(traitement.getJrn());
        traitement1.setObjectif(traitement.getObjectif());
        traitement1.setPresence(traitement.getPresence());
        traitement1.setPointage(traitement.getPointage());
        traitement1.setRetard(traitement.getRetard());
        traitement1.setTolerance(traitement.getTolerance());
        traitement1.setTrn(traitement.getTrn());
        traitement1.setTypeTraitement(traitement.getTypeTraitement());


        return new ResponseEntity<>(traitementRepository.save(traitement1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/traitements/{id}")
    public ResponseEntity<HttpStatus> deleteTraitementById(@PathVariable("id") Long id) {
        traitementRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/traitements")
    public ResponseEntity<HttpStatus> deleteAllTraitements() {
        traitementRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/traitements")
//    public ResponseEntity<List<Traitement>> deleteAllTraitementOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeTraitements();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
