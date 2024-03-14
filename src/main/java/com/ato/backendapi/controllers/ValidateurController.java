package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.UtilisateursDTO;
import com.ato.backendapi.dto.ValidateurDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.UtilisateursRepository;
import com.ato.backendapi.repositories.ValidateurRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class ValidateurController {
    @Autowired
    private ValidateurRepository validateurRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllValidateurs")
    public ResponseEntity<Map<String, Object>> listeValidateurs(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Validateur> validateurs;
        if (offset != null && pageSize != null) {
            Pageable pageable = PageRequest.of(offset, pageSize);
            Page<Validateur> validateursPage = validateurRepository.findAll(pageable);
            validateurs = validateursPage.getContent();
        } else {
            validateurs = validateurRepository.findAll();
        }

        if (validateurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<Map<String, Object>> validateurList = new ArrayList<>();
        for (Validateur validateur : validateurs) {
            Map<String, Object> validateurMap = new HashMap<>();
            validateurMap.put("idValidateur", validateur.getIdValidateur());
            validateurMap.put("niveau", validateur.getNiveau());

            Utilisateurs validateurAssocie = validateur.getValidateur();
            if (validateurAssocie != null) {
                Map<String, Object> validateurAssocieMap = new HashMap<>();
                validateurAssocieMap.put("id", validateurAssocie.getIdUtilisateur());
                validateurAssocieMap.put("nom", validateurAssocie.getNom());
                validateurAssocieMap.put("prenom", validateurAssocie.getPrenom());
                validateurAssocieMap.put("matricule", validateurAssocie.getMatricule());
                validateurMap.put("Validateur", validateurAssocieMap);
            }

            Utilisateurs utilisateur = validateur.getUtilisateurs();
            if (utilisateur != null) {
                Map<String, Object> utilisateurMap = new HashMap<>();
                utilisateurMap.put("idUtilisateur", utilisateur.getIdUtilisateur());
                utilisateurMap.put("nom", utilisateur.getNom());
                utilisateurMap.put("prenom", utilisateur.getPrenom());
                utilisateurMap.put("matricule", utilisateur.getMatricule());
                // Créez une liste d'utilisateurs pour chaque validateur
                List<Map<String, Object>> utilisateurList = new ArrayList<>();
                utilisateurList.add(utilisateurMap);
                validateurMap.put("Utilisateurs", utilisateurList);
            }

            validateurList.add(validateurMap);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("AllValidateurs", validateurList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/AllValidateursMobile")
    public ResponseEntity<Map<String, Object>> allValidateurs(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Validateur> validateurs;
        Page<Validateur> validateursPage = null;

        if (offset != null && pageSize != null) {
            Pageable pageable = PageRequest.of(offset, pageSize);
            validateursPage = validateurRepository.findAll(pageable);
            validateurs = validateursPage.getContent();
        } else {
            validateurs = validateurRepository.findAll();
        }

        if (validateurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<ValidateurDTO> validateurDTOS = new ArrayList<>();
        for (Validateur validateur : validateurs) {
            ValidateurDTO validateurDTO = new ValidateurDTO();
            validateurDTO.setIdValidateur(validateur.getIdValidateur());
            validateurDTO.setNiveau(validateur.getNiveau());

            // Informations sur l'utilisateur associé
            Utilisateurs utilisateur = validateur.getUtilisateurs();
            if (utilisateur != null) {
                UtilisateursDTO utilisateurDTO = convertToUtilisateurDTO(utilisateur);
                validateurDTO.setUtilisateurs(utilisateurDTO);
            }

            // Informations sur le validateur associé
            Utilisateurs validateurAssocie = validateur.getValidateur();
            if (validateurAssocie != null) {
                UtilisateursDTO validateurAssocieDTO = convertToUtilisateurDTO(validateurAssocie);
                validateurDTO.setValidateur(validateurAssocieDTO);
            }

            validateurDTOS.add(validateurDTO);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("AllValidateurs", validateurDTOS);

        if (validateursPage != null) {
            response.put("currentPage", validateursPage.getNumber());
            response.put("totalItems", validateursPage.getTotalElements());
            response.put("totalPages", validateursPage.getTotalPages());
        } else {
            response.put("currentPage", 0);
            response.put("totalItems", validateurDTOS.size());
            response.put("totalPages", 1);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{validateurId}/utilisateurs")
    public ResponseEntity<List<UtilisateursDTO>> getUsersByValidator(
            @PathVariable Long validateurId,
            @RequestParam int niveau) {
        List<Utilisateurs> utilisateurs = utilisateursRepository.getUsersByValidator(validateurId, niveau);
        if (utilisateurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<UtilisateursDTO> utilisateursDTOs = new ArrayList<>();
        for (Utilisateurs utilisateur : utilisateurs) {
            UtilisateursDTO utilisateurDTO = convertToUtilisateurDTO(utilisateur);
            utilisateursDTOs.add(utilisateurDTO);
        }

        return new ResponseEntity<>(utilisateursDTOs, HttpStatus.OK);
    }




//    @GetMapping("/validateurs/{userId}/AllValidateurs")
//    public ResponseEntity<List<Validateur>> getAllValidateursByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<Validateur> validateurs = new ArrayList<>();
//        validateurs.addAll(utilisateurs.getValidateurs());
//
//        return new ResponseEntity<>(validateurs, HttpStatus.OK);
//    }

    @GetMapping("/validateurs/{id}")
    public ResponseEntity<ValidateurDTO> getValidateurById(@PathVariable("id") Long id) {
        Optional<Validateur> validateurOptional = validateurRepository.findById(id);

        if (validateurOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Validateur validateur = validateurOptional.get();
        ValidateurDTO validateurDTO = convertToValidateurDTO(validateur);

        return new ResponseEntity<>(validateurDTO, HttpStatus.OK);
    }

    private ValidateurDTO convertToValidateurDTO(Validateur validateur) {
        ValidateurDTO validateurDTO = new ValidateurDTO();
        validateurDTO.setIdValidateur(validateur.getIdValidateur());
        validateurDTO.setNiveau(validateur.getNiveau());

        // Convertir les informations sur l'utilisateur associé
        Utilisateurs utilisateur = validateur.getUtilisateurs();
        if (utilisateur != null) {
            UtilisateursDTO utilisateurDTO = new UtilisateursDTO();
            utilisateurDTO.setIdUtilisateur(utilisateur.getIdUtilisateur());
            utilisateurDTO.setMatricule(utilisateur.getMatricule());
            utilisateurDTO.setNom(utilisateur.getNom());
            utilisateurDTO.setPrenom(utilisateur.getPrenom());
            utilisateurDTO.setAdresseMail(utilisateur.getAdresseMail());
            utilisateurDTO.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
            utilisateurDTO.setTelephone(utilisateur.getTelephone());
            utilisateurDTO.setDateEntree(utilisateur.getDateEntree());
            utilisateurDTO.setActif(utilisateur.isActif());
            utilisateurDTO.setPhoto(utilisateur.getPhoto());
            utilisateurDTO.setPassword(utilisateur.getPassword());
            utilisateurDTO.setAdresseIpTel(utilisateur.getAdresseIpTel());
            utilisateurDTO.setDatePassModifie(utilisateur.getDatePassModifie());
            utilisateurDTO.setDateFinContrat(utilisateur.getDateFinContrat());
            utilisateurDTO.setRole(utilisateur.getRole());
            // Remplir les informations sur le profil de l'utilisateurDTO
            Profil profil = utilisateur.getProfil();
            if (profil != null) {
                utilisateurDTO.setProfilId(profil.getIdProfil());
                utilisateurDTO.setProfilDesignation(profil.getDesignation());
            }
            // Informations sur le département
            Departements departements = utilisateur.getDepartements();
            if (departements != null) {
                utilisateurDTO.setDepartementId(departements.getIdDepartement());
                utilisateurDTO.setDepartementDescription(departements.getDescription());

                // Informations sur la direction
                Direction direction = departements.getDirection();
                if (direction != null) {
                    utilisateurDTO.setDirectionId(direction.getIdDirection());
                    utilisateurDTO.setDirectionDescription(direction.getDescription());
                }
            }
            // Remplir les informations sur le contrat de l'utilisateurDTO
            Contrats contrat = utilisateur.getContrats();
            if (contrat != null) {
                utilisateurDTO.setContratId(contrat.getIdContrat());
                utilisateurDTO.setContratDesignation(contrat.getDesignation());
           }
            validateurDTO.setUtilisateurs(utilisateurDTO);
        }

        // Convertir les informations sur le validateur associé
        Utilisateurs validateurAssocie = validateur.getValidateur();
        if (validateurAssocie != null) {
            UtilisateursDTO validateurAssocieDTO = new UtilisateursDTO();
            validateurAssocieDTO.setIdUtilisateur(validateurAssocie.getIdUtilisateur());
            validateurAssocieDTO.setMatricule(validateurAssocie.getMatricule());
            validateurAssocieDTO.setNom(validateurAssocie.getNom());
            validateurAssocieDTO.setPrenom(validateurAssocie.getPrenom());
            validateurAssocieDTO.setAdresseMail(validateurAssocie.getAdresseMail());
            validateurAssocieDTO.setPosteUtilisateur(validateurAssocie.getPosteUtilisateur());
            validateurAssocieDTO.setTelephone(validateurAssocie.getTelephone());
            validateurAssocieDTO.setDateEntree(validateurAssocie.getDateEntree());
            validateurAssocieDTO.setActif(validateurAssocie.isActif());
            validateurAssocieDTO.setPhoto(validateurAssocie.getPhoto());
            validateurAssocieDTO.setPassword(validateurAssocie.getPassword());
            validateurAssocieDTO.setAdresseIpTel(validateurAssocie.getAdresseIpTel());
            validateurAssocieDTO.setDatePassModifie(validateurAssocie.getDatePassModifie());
            validateurAssocieDTO.setDateFinContrat(validateurAssocie.getDateFinContrat());
            validateurAssocieDTO.setRole(validateurAssocie.getRole());
            // Remplir les autres détails du validateurDTO ici...
            validateurDTO.setValidateur(validateurAssocieDTO);
        }

        return validateurDTO;
    }

    private UtilisateursDTO convertToUtilisateurDTO(Utilisateurs utilisateur) {
        UtilisateursDTO utilisateurDTO = new UtilisateursDTO();
        utilisateurDTO.setIdUtilisateur(utilisateur.getIdUtilisateur());
        utilisateurDTO.setMatricule(utilisateur.getMatricule());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setAdresseMail(utilisateur.getAdresseMail());
        utilisateurDTO.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
        utilisateurDTO.setTelephone(utilisateur.getTelephone());
        utilisateurDTO.setDateEntree(utilisateur.getDateEntree());
        utilisateurDTO.setActif(utilisateur.isActif());
        utilisateurDTO.setPhoto(utilisateur.getPhoto());
        utilisateurDTO.setPassword(utilisateur.getPassword());
        utilisateurDTO.setAdresseIpTel(utilisateur.getAdresseIpTel());
        utilisateurDTO.setDatePassModifie(utilisateur.getDatePassModifie());
        utilisateurDTO.setDateFinContrat(utilisateur.getDateFinContrat());
        utilisateurDTO.setRole(utilisateur.getRole());

        // Informations sur le profil
        Profil profil = utilisateur.getProfil();
        if (profil != null) {
            utilisateurDTO.setProfilId(profil.getIdProfil());
            utilisateurDTO.setProfilDesignation(profil.getDesignation());
        }

        // Informations sur le département
        Departements departements = utilisateur.getDepartements();
        if (departements != null) {
            utilisateurDTO.setDepartementId(departements.getIdDepartement());
            utilisateurDTO.setDepartementDescription(departements.getDescription());

            // Informations sur la direction
            Direction direction = departements.getDirection();
            if (direction != null) {
                utilisateurDTO.setDirectionId(direction.getIdDirection());
                utilisateurDTO.setDirectionDescription(direction.getDescription());
            }
        }

        // Informations sur le contrat
        Contrats contrat = utilisateur.getContrats();
        if (contrat != null) {
            utilisateurDTO.setContratId(contrat.getIdContrat());
            utilisateurDTO.setContratDesignation(contrat.getDesignation());
        }

        return utilisateurDTO;
    }


    //Get des validateurs by niveau
    @GetMapping("/by-niveau")
    public ResponseEntity<List<ValidateurDTO>> getValidateursByNiveau(@RequestParam int niveau) {
        List<Validateur> validateurs = validateurRepository.findByNiveau(niveau);
        List<ValidateurDTO> dtoList = validateurs.stream()
                .map(this::convertToValidateurDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }


    //Hadi hta hiya khadama
//    @PostMapping("/validateurs")
//    public ResponseEntity<Map<String, Object>> addValidateur(
//            @RequestBody Validateur validateur,
//            @RequestParam List<Long> idUsers,
//            @RequestParam Long idValidateur) {
//
//        Map<String, Object> response = new HashMap<>();
//        List<Map<String, Object>> validateursDataList = new ArrayList<>();
//        Utilisateurs user_validateur = utilisateursRepository.findById(idValidateur)
//                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idValidateur));
//
//        try {
//            // Parcours des utilisateurs à ajouter au validateur
//            for (Long idUser : idUsers) {
//                Utilisateurs utilisateur = utilisateursRepository.findById(idUser)
//                        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
//
//                // Créer un nouvel objet Validateur
//                Validateur nouveauValidateur = new Validateur();
//                nouveauValidateur.setNiveau(validateur.getNiveau());
//                nouveauValidateur.setUtilisateurs(utilisateur); // Associe l'utilisateur
//                nouveauValidateur.setValidateur(user_validateur);
//
//                // Sauvegarder le validateur
//                validateurRepository.save(nouveauValidateur);
//
//                // Créer un objet Map pour contenir les détails du validateur créé
//                Map<String, Object> validateurData = new HashMap<>();
//                validateurData.put("idValidateur", nouveauValidateur.getIdValidateur());
//                validateurData.put("niveau", nouveauValidateur.getNiveau());
//                validateurData.put("idUtilisateur", utilisateur.getIdUtilisateur());
//                validateurData.put("idValidateurParent", idValidateur);
//
//                // Ajouter les informations du validateur créé à la liste
//                validateursDataList.add(validateurData);
//            }
//
//            // Construire la réponse JSON avec "success", "message", et "data"
//            response.put("success", "1");
//            response.put("message", "Validateurs créés avec succès.");
//            response.put("data", validateursDataList);
//
//            // Renvoyer la réponse avec statut CREATED
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//        } catch (Exception e) {
//            // Gérer les exceptions et renvoyer une réponse avec un message d'erreur
//            response.put("success", "0");
//            response.put("message", "Erreur lors de la création des validateurs.");
//            response.put("data", Collections.emptyList());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }

    //POST
    @PostMapping("/validateurs")
    public ResponseEntity<?> addValidateur(@RequestBody Validateur validateur,
                                           @RequestParam("idUsers") List<Long> idUsers,
                                           @RequestParam("idValidateur") Long idValidateur) {
        try {
            List<Validateur> validateursList = new ArrayList<>();

            // Récupérer le validateur (user_validateur)
            Utilisateurs user_validateur = utilisateursRepository.findById(idValidateur)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idValidateur));

            for (Long idUser : idUsers) {
                // Récupérer l'utilisateur (utilisateurs)
                Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                        .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));

                // Vérifier si une entrée pour cet utilisateur, validateur et niveau existe déjà
                Optional<Validateur> existingValidateur = validateurRepository.findByUtilisateursAndValidateurAndNiveau(utilisateurs, user_validateur, validateur.getNiveau());

                if (existingValidateur.isPresent()) {
                    // Si le validateur existe déjà, on passe cette itération
                    continue;
                }

                // Créer un nouveau validateur
                Validateur nouveauValidateur = new Validateur();
                nouveauValidateur.setUtilisateurs(utilisateurs);
                nouveauValidateur.setValidateur(user_validateur);
                nouveauValidateur.setNiveau(validateur.getNiveau());

                // Sauvegarder le nouveau validateur
                Validateur v = validateurRepository.save(nouveauValidateur);
                validateursList.add(v);
            }

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Validateur créés avec succès."); // Message de succès
            responseObject.put("data", validateursList); // Les informations des validateurs créés
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());

        } catch (Exception e) {
            // En cas d'erreur lors de la création des validateurs
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création des validateurs."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }




//    @PostMapping("/users/{userId}/validateurs")
//    public ResponseEntity<Validateur> createValidateur(@PathVariable(value = "userId") Long userId,
//                                                         @RequestBody Validateur validateur) {
//        Validateur validateur1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getValidateurs().add(validateur);
//            return validateurRepository.save(validateur);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(validateur1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/validateurs/{id}")
    public ResponseEntity<Validateur> editValidateur(@PathVariable("id") Long id, @RequestBody Validateur validateur,
                                           @RequestParam Long idUser,
                                           @RequestParam Long idValidateur){
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        Utilisateurs user_validateur = utilisateursRepository.findById(idValidateur)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idValidateur));
        Validateur validateur1 = validateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + id));
        validateur1.setUtilisateurs(utilisateurs);
        validateur1.setValidateur(user_validateur);
        validateur1.setNiveau(validateur.getNiveau());

        return new ResponseEntity<>(validateurRepository.save(validateur1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/validateurs/{id}")
    public ResponseEntity<HttpStatus> deleteValidateurById(@PathVariable("id") Long id) {
        validateurRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/validateurs")
    public ResponseEntity<HttpStatus> deleteAllValidateurs() {
        validateurRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/validateurs")
//    public ResponseEntity<List<Validateur>> deleteAllValidateursOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeValidateurs();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
