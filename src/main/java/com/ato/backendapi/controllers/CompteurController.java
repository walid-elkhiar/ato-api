package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.CompteurDTO;
import com.ato.backendapi.entities.Compteur;
import com.ato.backendapi.entities.Departements;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.CompteurRepository;
import com.ato.backendapi.repositories.UtilisateursRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class CompteurController {

    @Autowired
    private CompteurRepository compteurRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllCompteurs")
    public ResponseEntity<List<CompteurDTO>> getCompteursFiltrés(
            @RequestParam(required = false) Long directionId,
            @RequestParam(required = false) Long departementId,
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String nomPrenom) {

        try {
            List<Compteur> compteurs;

            if (directionId == null && departementId == null && matricule == null && nomPrenom == null) {
                // Si aucun filtre n'est passé, récupérer tous les compteurs
                compteurs = compteurRepository.findAll();
            } else {
                // Si un ou plusieurs filtres sont passés, appliquer les filtres
                compteurs = compteurRepository.findCompteursFiltre(directionId, departementId, matricule, nomPrenom);
            }

            List<CompteurDTO> compteurDTOs = compteurs.stream().map(compteur -> {
                CompteurDTO compteurDTO = new CompteurDTO();
                compteurDTO.setIdCompteur(compteur.getIdCompteur());
                compteurDTO.setDroitAnnuel(compteur.getDroitAnnuel());
                compteurDTO.setDroit(compteur.getDroit());
                compteurDTO.setPris(compteur.getPris());
                compteurDTO.setSolde(compteur.getSolde());

                // Informations de l'utilisateur
                Utilisateurs utilisateur = compteur.getUtilisateurs();
                compteurDTO.setIdUtilisateur(utilisateur.getIdUtilisateur());
                compteurDTO.setMatricule(utilisateur.getMatricule());
                compteurDTO.setNom(utilisateur.getNom());
                compteurDTO.setPrenom(utilisateur.getPrenom());
                compteurDTO.setAdresseMail(utilisateur.getAdresseMail());
                compteurDTO.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
                compteurDTO.setTelephone(utilisateur.getTelephone());
                compteurDTO.setDateEntree(utilisateur.getDateEntree());
                compteurDTO.setActif(utilisateur.isActif());
                compteurDTO.setPhoto(utilisateur.getPhoto());
                compteurDTO.setPassword(utilisateur.getPassword());
                compteurDTO.setAdresseIpTel(utilisateur.getAdresseIpTel());
                compteurDTO.setDatePassModifie(utilisateur.getDatePassModifie());
                compteurDTO.setDateFinContrat(utilisateur.getDateFinContrat());
                compteurDTO.setRole(utilisateur.getRole());

                //Informations de Departement
                Departements departements = compteur.getUtilisateurs().getDepartements();
                compteurDTO.setDepartementId(departements.getIdDepartement());
                compteurDTO.setDepartementDescription(departements.getDescription());

                return compteurDTO;
            }).collect(Collectors.toList());

            return new ResponseEntity<>(compteurDTOs, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET
//    @GetMapping("/compteurs/{userId}/AllCompteurs")
//    public ResponseEntity<List<Compteur>> getAllCompteursByProfilId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        List<Compteur> compteurs = new ArrayList<>();
//        compteurs.addAll(utilisateurs.getCompteurs());
//
//        return new ResponseEntity<>(compteurs, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/compteurs/{id}")
    public ResponseEntity<CompteurDTO> getCompteurById(@PathVariable(value = "id") Long id) {
        Compteur compteur = compteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Compteur with id = " + id));

        if (compteur == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        CompteurDTO compteurDTO = new CompteurDTO();
        compteurDTO.setIdCompteur(compteur.getIdCompteur());
        compteurDTO.setSolde(compteur.getSolde());
        compteurDTO.setPris(compteur.getPris());
        compteurDTO.setDroit(compteur.getDroit());
        compteurDTO.setDroitAnnuel(compteur.getDroitAnnuel());


        // Ajoutez les informations sur l'utilisateur
        Utilisateurs utilisateurs = compteur.getUtilisateurs();
        if (utilisateurs != null) {
            compteurDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
            compteurDTO.setMatricule(utilisateurs.getMatricule());
            compteurDTO.setNom(utilisateurs.getNom());
            compteurDTO.setPrenom(utilisateurs.getPrenom());
            compteurDTO.setAdresseMail(utilisateurs.getAdresseMail());
            compteurDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
            compteurDTO.setTelephone(utilisateurs.getTelephone());
            compteurDTO.setDateEntree(utilisateurs.getDateEntree());
            compteurDTO.setActif(utilisateurs.isActif());
            compteurDTO.setPhoto(utilisateurs.getPhoto());
            compteurDTO.setPassword(utilisateurs.getPassword());
            compteurDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
            compteurDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
            compteurDTO.setDateEntree(utilisateurs.getDateEntree());
            compteurDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
            compteurDTO.setRole(utilisateurs.getRole());

        }

        return new ResponseEntity<>(compteurDTO, HttpStatus.OK);
    }

    // Get Compteurs par Utilisateur
    @GetMapping("/{idUtilisateur}/compteurs")
    public ResponseEntity<List<CompteurDTO>> getCompteursByUtilisateurId(@PathVariable Long idUtilisateur) {
        try {
            List<Compteur> compteurs = compteurRepository.findByUtilisateurs_IdUtilisateur(idUtilisateur);

            if (compteurs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<CompteurDTO> compteurDTOs = compteurs.stream().map(compteur -> {
                CompteurDTO compteurDTO = new CompteurDTO();
                compteurDTO.setIdCompteur(compteur.getIdCompteur());
                compteurDTO.setDroitAnnuel(compteur.getDroitAnnuel());
                compteurDTO.setDroit(compteur.getDroit());
                compteurDTO.setPris(compteur.getPris());
                compteurDTO.setSolde(compteur.getSolde());

                // Informations de l'utilisateur
                Utilisateurs utilisateur = compteur.getUtilisateurs();
                compteurDTO.setIdUtilisateur(utilisateur.getIdUtilisateur());
                compteurDTO.setMatricule(utilisateur.getMatricule());
                compteurDTO.setNom(utilisateur.getNom());
                compteurDTO.setPrenom(utilisateur.getPrenom());
                compteurDTO.setAdresseMail(utilisateur.getAdresseMail());
                compteurDTO.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
                compteurDTO.setTelephone(utilisateur.getTelephone());
                compteurDTO.setDateEntree(utilisateur.getDateEntree());
                compteurDTO.setActif(utilisateur.isActif());
                compteurDTO.setPhoto(utilisateur.getPhoto());
                compteurDTO.setPassword(utilisateur.getPassword());
                compteurDTO.setAdresseIpTel(utilisateur.getAdresseIpTel());
                compteurDTO.setDatePassModifie(utilisateur.getDatePassModifie());
                compteurDTO.setDateFinContrat(utilisateur.getDateFinContrat());
                compteurDTO.setRole(utilisateur.getRole());

                return compteurDTO;
            }).collect(Collectors.toList());

            return new ResponseEntity<>(compteurDTOs, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/users/{userId}/compteurs")
//    public ResponseEntity<Compteur> createCompteur(@PathVariable(value = "userId") Long userId,
//                                                           @RequestBody Compteur compteurReq) {
//        Compteur compteur = utilisateursRepository.findById(userId).map(user -> {
//            user.getCompteurs().add(compteurReq);
//            return compteurRepository.save(compteurReq);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        return new ResponseEntity<>(compteur, HttpStatus.CREATED);
//    }

    //POST
    @PostMapping("/compteurs")
    public ResponseEntity<?> addCompteur(@RequestBody Compteur compteur,
                                 @RequestParam Long idUser) {
        try {
            Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));

            // Initialiser les champs à zéro
            compteur.setDroitAnnuel(0);
            compteur.setDroit(0);
            compteur.setPris(0);
            compteur.setSolde(0);

            compteur.setUtilisateurs(utilisateurs);

            Compteur savedCompteur = compteurRepository.save(compteur);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Compteur créé avec succès."); // Message de succès
            responseObject.put("data", savedCompteur); // Les informations du compteur créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du compteur
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du compteur."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    @PutMapping("/compteurs/{id}")
    public ResponseEntity<Compteur> updateCompteur(@PathVariable("id") Long id,
                                                   @RequestBody Compteur compteurRequest,
                                                   @RequestParam Long idUser) {
        Compteur compteur = compteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CompteurId " + id + "not found"));
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        compteur.setUtilisateurs(utilisateurs);
        compteur.setDroit(compteurRequest.getDroit());
        compteur.setPris(compteurRequest.getPris());
        compteur.setSolde(compteurRequest.getSolde());
        compteur.setDroitAnnuel(compteurRequest.getDroitAnnuel());

        return new ResponseEntity<>(compteurRepository.save(compteur), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/compteurs/{id}")
    public ResponseEntity<HttpStatus> deleteCompteur(@PathVariable("id") Long id) {
        compteurRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/compteurs")
    public ResponseEntity<HttpStatus> deleteAllCompteurs() {
        compteurRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/compteurs")
//    public ResponseEntity<List<Compteur>> deleteAllCompteursOfUser(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        utilisateurs.removeCompteurs();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
