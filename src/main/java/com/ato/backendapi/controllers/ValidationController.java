package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.*;
import com.ato.backendapi.emailService.EmailService;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.DemandesRepository;
import com.ato.backendapi.repositories.UtilisateursRepository;
import com.ato.backendapi.repositories.ValidateurRepository;
import com.ato.backendapi.repositories.ValidationRepository;
import com.ato.backendapi.services.ValidationService;
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
public class ValidationController {

    @Autowired
    private DemandesRepository demandesRepository;

    @Autowired
    private ValidateurRepository validateurRepository;

    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
    @GetMapping("/AllValidations")
    public ResponseEntity<Map<String, Object>> getAllValidations(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Validation> validations;
        Page<Validation> validationPage = null;

        if (offset != null && pageSize != null) {
            Pageable pageable = PageRequest.of(offset, pageSize);
            validationPage = validationRepository.findAll(pageable);
            validations = validationPage.getContent();
        } else {
            validations = validationRepository.findAll();
        }

        if (validations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<ValidationDTO> validationDTOs = validations.stream()
                .map(this::convertToValidationDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("AllValidations", validationDTOs);

        if (validationPage != null) {
            response.put("currentPage", validationPage.getNumber());
            response.put("totalItems", validationPage.getTotalElements());
            response.put("totalPages", validationPage.getTotalPages());
        } else {
            response.put("currentPage", 0);
            response.put("totalItems", validationDTOs.size());
            response.put("totalPages", 1);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ValidationDTO convertToValidationDTO(Validation validation) {
        ValidationDTO validationDTO = new ValidationDTO();

        // Remplir les champs de ValidationDTO à partir de l'objet Validation
        validationDTO.setIdValidation(validation.getIdValidation());
        validationDTO.setDateValidation(validation.getDateValidation());

        // Remplir les informations sur le Validateur
        Validateur validateur = validation.getValidateur();
        if (validateur != null) {
            validationDTO.setIdValidateur(validateur.getIdValidateur());
            validationDTO.setNiveau(validateur.getNiveau());

            // Convertir les informations sur l'utilisateur associé au Validateur
            Utilisateurs utilisateur = validateur.getUtilisateurs();
            if (utilisateur != null) {
                UtilisateursDTO utilisateurDTO = convertToUtilisateurDTO(utilisateur);
                validationDTO.setUtilisateur(utilisateurDTO);
            }

            // Informations sur le validateur associé
            Utilisateurs validateurAssocie = validateur.getValidateur();
            if (validateurAssocie != null) {
                UtilisateursDTO validateurAssocieDTO = convertToUtilisateurDTO(validateurAssocie);
                validationDTO.setValidateur(validateurAssocieDTO);
            }
        }

        // Remplir les informations sur la Demande
        Demandes demande = validation.getDemandes();
        if (demande != null) {
            validationDTO.setIdDemande(demande.getIdDemande());
            validationDTO.setDateSaisie(demande.getDateSaisie());
            validationDTO.setCodeMotif(demande.getCodeMotif());
            validationDTO.setDateDebutMission(demande.getDateDebutMission());
            validationDTO.setDateFinMission(demande.getDateFinMission());
            validationDTO.setDuree(demande.getDuree());
            validationDTO.setObjet(demande.getObjet());
            validationDTO.setDateReprise(demande.getDateReprise());
            validationDTO.setPlage(demande.getPlage());
            validationDTO.setEtatValidation(demande.getEtatValidation());

            // Convertir les informations sur l'utilisateur associé à la Demande
            Utilisateurs utilisateurDemande = demande.getUtilisateurs();
            if (utilisateurDemande != null) {
                UtilisateursDTO utilisateurDemandeDTO = convertToUtilisateurDTO(utilisateurDemande);
                validationDTO.setUtilisateur_demande(utilisateurDemandeDTO);
            }

            // Convertir les informations sur l'utilisateur qui a saisi la Demande
            Utilisateurs utilisateurSaisiDemande = demande.getUtilisateurSaisiDemande();
            if (utilisateurSaisiDemande != null) {
                UtilisateursDTO utilisateurSaisiDemandeDTO = convertToUtilisateurDTO(utilisateurSaisiDemande);
                validationDTO.setUtilisateur_saisi_demande(utilisateurSaisiDemandeDTO);
            }
        }
        return validationDTO;
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


    //GET
//    @GetMapping("/validations/{demandeId}/AllValidations")
//    public ResponseEntity<List<Validation>> getAllValidationsByDemandeId(@PathVariable(value = "demandeId") Long demandeId) {
//        Demandes demandes = demandesRepository.findById(demandeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Demande with id = " + demandeId));
//
//        List<Validation> validations = new ArrayList<>();
//        validations.addAll(demandes.getValidations());
//
//        return new ResponseEntity<>(validations, HttpStatus.OK);
//    }

//    @GetMapping("/validations/{validateurId}/AllValidations")
//    public ResponseEntity<List<Validation>> getAllValidationsByValidateurId(@PathVariable(value = "validateurId") Long validateurId) {
//        Validateur validateur = validateurRepository.findById(validateurId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + validateurId));
//
//        List<Validation> validations = new ArrayList<>();
//        validations.addAll(validateur.getValidations());
//
//        return new ResponseEntity<>(validations, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/validations/{id}")
    public ResponseEntity<ValidationDTO> getValidationById(@PathVariable(value = "id") Long id) {
        Optional<Validation> validationOptional = validationRepository.findById(id);

        if (validationOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Validation validation = validationOptional.get();
        ValidationDTO validationDTO = convertToValidationDTO(validation);

        return new ResponseEntity<>(validationDTO, HttpStatus.OK);
    }

    //Consultation demandes par validateurId


    @GetMapping("/by-validateur")
    public ResponseEntity<List<ValidationDTO>> getDemandesByValidateur(@RequestParam Long validateurId) {
        List<ValidationDTO> demandes = validationService.getDemandesByValidateur(validateurId);
        return ResponseEntity.ok(demandes);
    }

    @GetMapping("/grouped-by-validateur")
    public ResponseEntity<Map<ValidateurDTO, List<DemandesDTO>>> getDemandesGroupedByValidateur() {
        Map<ValidateurDTO, List<DemandesDTO>> result = validationService.getDemandesGroupedByValidateur();
        return ResponseEntity.ok(result);
    }

    //POST
    @PostMapping("/validations")
    public ResponseEntity<?> addValidation(@RequestBody Validation validation,
                                 @RequestParam Long idDemande,
                                 @RequestParam Long idValidateur) {
        try {
            Demandes demandes = demandesRepository.findById(idDemande)
                    .orElseThrow(() -> new ResourceNotFoundException("Demandes non trouvé avec l'ID : " + idDemande));
            Validateur validateur = validateurRepository.findById(idValidateur)
                    .orElseThrow(() -> new ResourceNotFoundException("Validateur non trouvé avec l'ID : " + idValidateur));

            validation.setDemandes(demandes);
            validation.setValidateur(validateur);

            Validation v = validationRepository.save(validation);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Validation créée avec succès."); // Message de succès
            responseObject.put("data", v); // Les informations de la validation créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de la validation
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de la validation."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

//    @PostMapping("/demandes/{demandeId}/validations")
//    public ResponseEntity<Validation> createValidationByDemandeId(@PathVariable(value = "demandeId") Long demandeId,
//                                                   @RequestBody Validation validation) {
//        Validation validation1 = demandesRepository.findById(demandeId).map(dem -> {
//            dem.getValidations().add(validation);
//            return validationRepository.save(validation);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Demande with id = " + demandeId));
//
//        return new ResponseEntity<>(validation1, HttpStatus.CREATED);
//    }

//    @PostMapping("/validateurs/{validateurId}/validations")
//    public ResponseEntity<Validation> createValidationByValidateurId(@PathVariable(value = "validateurId") Long validateurId,
//                                                       @RequestBody Validation validation) {
//        Validation validation1 = validateurRepository.findById(validateurId).map(validt -> {
//            validt.getValidations().add(validation);
//            return validationRepository.save(validation);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + validateurId));
//
//        return new ResponseEntity<>(validation1, HttpStatus.CREATED);
//    }

    @PutMapping("/validations/{id}")
    public ResponseEntity<Validation> updateValidation(@PathVariable("id") Long id, @RequestBody Validation validationReq,
                                                       @RequestParam Long idDemande,
                                                       @RequestParam Long idValidateur){
        Validation validation = validationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ValidationId " + id + "not found"));
        Demandes demandes = demandesRepository.findById(idDemande)
                .orElseThrow(() -> new ResourceNotFoundException("Demandes non trouvé avec l'ID : " + idDemande));
        Validateur validateur = validateurRepository.findById(idValidateur)
                .orElseThrow(() -> new ResourceNotFoundException("Validateur non trouvé avec l'ID : " + idValidateur));

        validation.setDemandes(demandes);
        validation.setValidateur(validateur);
        validation.setDateValidation(validationReq.getDateValidation());

        return new ResponseEntity<>(validationRepository.save(validation), HttpStatus.OK);
    }

    @PutMapping("/valider/{idDemande}/{idUtilisateur}")
    public ResponseEntity<?> validerDemande(@PathVariable Long idDemande, @PathVariable Long idUtilisateur) {
        // Récupérer la demande
        Optional<Demandes> optionalDemande = demandesRepository.findById(idDemande);
        if (optionalDemande.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée");
        }
        Demandes demande = optionalDemande.get();

        // Récupérer le validateur
        Optional<Validateur> optionalValidateur = validateurRepository.findByUtilisateurs_IdUtilisateur(idUtilisateur);
        if (optionalValidateur.isEmpty()) {
            // Si l'utilisateur n'est pas un validateur, valider comme RH
            demande.setEtatValidation("RH Validé");
            demandesRepository.save(demande);
            return ResponseEntity.ok("Demande validée par RH");
        }

        // Si un validateur est trouvé
        Validateur validateur = optionalValidateur.get();

        // Mise à jour du niveau de validation, maximum 5
        int niveau = validateur.getNiveau();
        if (niveau < 5) {
            validateur.setNiveau(niveau + 1);  // Incrémenter le niveau
            demande.setEtatValidation("Niveau " + validateur.getNiveau() + " Validé");
        } else {
            demande.setEtatValidation("Finalement Validé");  // Si le niveau est 5, validation finale
        }

        // Sauvegarder les changements dans la base de données
        validateurRepository.save(validateur);
        demandesRepository.save(demande);

        return ResponseEntity.ok("Demande validée avec succès au niveau " + validateur.getNiveau());
    }

    @PutMapping("/invalider/{idDemande}/{idUtilisateur}")
    public ResponseEntity<?> invaliderDemande(@PathVariable Long idDemande, @PathVariable Long idUtilisateur) {
        // Récupérer la demande
        Optional<Demandes> optionalDemande = demandesRepository.findById(idDemande);
        if (optionalDemande.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande non trouvée");
        }
        Demandes demande = optionalDemande.get();

        // Récupérer l'utilisateur pour envoyer un email
        Optional<Utilisateurs> optionalUtilisateur = utilisateursRepository.findById(idUtilisateur);
        if (optionalUtilisateur.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }
        Utilisateurs utilisateur = optionalUtilisateur.get();

        // Récupérer le validateur
        Optional<Validateur> optionalValidateur = validateurRepository.findByUtilisateurs_IdUtilisateur(idUtilisateur);
        if (optionalValidateur.isEmpty()) {
            // Si l'utilisateur n'est pas un validateur, invalider comme RH
            demande.setEtatValidation("Refusé par RH");
        } else {
            // Invalidation de la demande par un validateur
            demande.setEtatValidation("Refusé");
        }

        // Sauvegarder l'état modifié de la demande
        demandesRepository.save(demande);

        // Envoi d'un email de notification
        String message = "Bonjour " + utilisateur.getPrenom() + ",\n\nVotre demande avec ID: " + idDemande + " a été refusée.\n\nCordialement.";
        emailService.sendNotificationEmail(utilisateur.getAdresseMail(), "Refus de votre demande", message);

        return ResponseEntity.ok("Demande refusée et notification envoyée à l'utilisateur.");
    }




    @DeleteMapping("/validation/{id}")
    public ResponseEntity<HttpStatus> deleteValidation(@PathVariable("id") Long id) {
        validationRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/validation")
    public ResponseEntity<HttpStatus> deleteAllValidations() {
        validationRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/demandes/{demandeId}/validations")
//    public ResponseEntity<List<Validation>> deleteAllValidationsOfDemande(@PathVariable(value = "demandeId") Long demandeId) {
//        Demandes demandes = demandesRepository.findById(demandeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Demande with id = " + demandeId));
//
//        demandes.removeValidations();
//        demandesRepository.save(demandes);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/validateur/{validateurId}/validations")
//    public ResponseEntity<List<Validation>> deleteAllValidationsOfValidateur(@PathVariable(value = "validateurId") Long validateurId) {
//        Validateur validateur = validateurRepository.findById(validateurId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + validateurId));
//
//        validateur.removeValidations();
//        validateurRepository.save(validateur);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
