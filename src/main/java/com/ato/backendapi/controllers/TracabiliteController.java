package com.ato.backendapi.controllers;


import com.ato.backendapi.dto.TracabiliteDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.TracabiliteRepository;
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
public class TracabiliteController {

    @Autowired
    private TracabiliteRepository tracabiliteRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllTracabilites")
    public ResponseEntity<List<TracabiliteDTO>> listeTracabilites(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Tracabilite> tracabilites;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Tracabilite> tracabilitesPage = tracabiliteRepository.findAll(PageRequest.of(offset, pageSize));
            tracabilites = tracabilitesPage.getContent();
        } else {
            tracabilites = tracabiliteRepository.findAll();
        }

        if (tracabilites.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<TracabiliteDTO> tracabiliteDTOS = new ArrayList<>();
        for (Tracabilite tracabilite : tracabilites) {
            TracabiliteDTO tracabiliteDTO = new TracabiliteDTO();
            tracabiliteDTO.setIdTracabilite(tracabilite.getIdTracabilite());
            tracabiliteDTO.setOperation(tracabilite.getOperation());
            tracabiliteDTO.setConcerne(tracabilite.getConcerne());
            tracabiliteDTO.setDateOperation(tracabilite.getDateOperation());
            tracabiliteDTO.setAdreesePc(tracabilite.getAdreesePc());

            // Ajoutez les informations sur l'utilisateur
            Utilisateurs utilisateurs = tracabilite.getUtilisateurs();
            if (utilisateurs != null) {
                tracabiliteDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
                tracabiliteDTO.setMatricule(utilisateurs.getMatricule());
                tracabiliteDTO.setNom(utilisateurs.getNom());
                tracabiliteDTO.setPrenom(utilisateurs.getPrenom());
                tracabiliteDTO.setAdresseMail(utilisateurs.getAdresseMail());
                tracabiliteDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
                tracabiliteDTO.setTelephone(utilisateurs.getTelephone());
                tracabiliteDTO.setDateEntree(utilisateurs.getDateEntree());
                tracabiliteDTO.setActif(utilisateurs.isActif());
                tracabiliteDTO.setPhoto(utilisateurs.getPhoto());
                tracabiliteDTO.setPassword(utilisateurs.getPassword());
                tracabiliteDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
                tracabiliteDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
                tracabiliteDTO.setDateEntree(utilisateurs.getDateEntree());
                tracabiliteDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
                tracabiliteDTO.setRole(utilisateurs.getRole());
            }

            tracabiliteDTOS.add(tracabiliteDTO);
        }

        return new ResponseEntity<>(tracabiliteDTOS, HttpStatus.OK);
    }


//    @GetMapping("/tracabilites/{userId}/AllTracabilites")
//    public ResponseEntity<List<Tracabilite>> getAllTracabilitesByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<Tracabilite> tracabilites = new ArrayList<>();
//        tracabilites.addAll(utilisateurs.getTracabilites());
//
//        return new ResponseEntity<>(tracabilites, HttpStatus.OK);
//    }

    @GetMapping("/tracabilites/{id}")
    public ResponseEntity<TracabiliteDTO> getTracabiliteById(@PathVariable("id") Long id) {
        Tracabilite tracabilite = tracabiliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tracabilite with id = " + id));

        if (tracabilite == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        TracabiliteDTO tracabiliteDTO = new TracabiliteDTO();
        tracabiliteDTO.setIdTracabilite(tracabilite.getIdTracabilite());
        tracabiliteDTO.setOperation(tracabilite.getOperation());
        tracabiliteDTO.setConcerne(tracabilite.getConcerne());
        tracabiliteDTO.setDateOperation(tracabilite.getDateOperation());
        tracabiliteDTO.setAdreesePc(tracabilite.getAdreesePc());


        // Ajoutez les informations sur l'utilisateur
        Utilisateurs utilisateurs = tracabilite.getUtilisateurs();
        if (utilisateurs != null) {
            tracabiliteDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
            tracabiliteDTO.setMatricule(utilisateurs.getMatricule());
            tracabiliteDTO.setNom(utilisateurs.getNom());
            tracabiliteDTO.setPrenom(utilisateurs.getPrenom());
            tracabiliteDTO.setAdresseMail(utilisateurs.getAdresseMail());
            tracabiliteDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
            tracabiliteDTO.setTelephone(utilisateurs.getTelephone());
            tracabiliteDTO.setDateEntree(utilisateurs.getDateEntree());
            tracabiliteDTO.setActif(utilisateurs.isActif());
            tracabiliteDTO.setPhoto(utilisateurs.getPhoto());
            tracabiliteDTO.setPassword(utilisateurs.getPassword());
            tracabiliteDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
            tracabiliteDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
            tracabiliteDTO.setDateEntree(utilisateurs.getDateEntree());
            tracabiliteDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
            tracabiliteDTO.setRole(utilisateurs.getRole());

        }

        return new ResponseEntity<>(tracabiliteDTO, HttpStatus.OK);
    }

    //POST
    @PostMapping("/tracabilites")
    public ResponseEntity<?> add(@RequestBody Tracabilite tracabilite,
                                 @RequestParam Long idUser) {
        try {
            Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
            tracabilite.setUtilisateurs(utilisateurs);
            Tracabilite t = tracabiliteRepository.save(tracabilite);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Tracabilite créé avec succès."); // Message de succès
            responseObject.put("data", t); // Les informations de la tracabilite créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de la tracabilite
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de la tracabilite."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


//    @PostMapping("/users/{userId}/tracabilites")
//    public ResponseEntity<Tracabilite> createTracabilite(@PathVariable(value = "userId") Long userId,
//                                                  @RequestBody Tracabilite tracabilite) {
//        Tracabilite tracabilite1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getTracabilites().add(tracabilite);
//            return tracabiliteRepository.save(tracabilite);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(tracabilite1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/tracabilites/{id}")
    public ResponseEntity<Tracabilite> edit(@PathVariable("id") Long id, @RequestBody Tracabilite tracabilite,
                                            @RequestParam Long idUser){
        Tracabilite tracabilite1 = tracabiliteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tracabilite with id = " + id));
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        tracabilite1.setUtilisateurs(utilisateurs);
        tracabilite1.setAdreesePc(tracabilite.getAdreesePc());
        tracabilite1.setConcerne(tracabilite.getConcerne());
        tracabilite1.setOperation(tracabilite.getOperation());
        tracabilite1.setDateOperation(tracabilite.getDateOperation());
        return new ResponseEntity<>(tracabiliteRepository.save(tracabilite1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/tracabilites/{id}")
    public ResponseEntity<HttpStatus> deleteTracabiliteById(@PathVariable("id") Long id) {
        tracabiliteRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tracabilites")
    public ResponseEntity<HttpStatus> deleteAllTracabilites() {
        tracabiliteRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/tracabilites")
//    public ResponseEntity<List<Tracabilite>> deleteAllTracabilitesOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeTracabilites();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
