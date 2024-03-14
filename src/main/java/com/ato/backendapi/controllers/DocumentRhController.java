package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DocumentRhDTO;
import com.ato.backendapi.dto.UtilisateursDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
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
public class DocumentRhController {

    @Autowired
    private ListeAttestationRepository listeAttestationRepository;

    @Autowired
    private DocumentRhRepository documentRhRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;


    //GET
    @GetMapping("/AllDocumentRh")
    public ResponseEntity<List<DocumentRhDTO>> getAllDocumentRh(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<DocumentRH> documentRHS;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<DocumentRH> documentRHSPage = documentRhRepository.findAll(PageRequest.of(offset, pageSize));
            documentRHS = documentRHSPage.getContent();
        } else {
            documentRHS = documentRhRepository.findAll();
        }

        if (documentRHS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DocumentRhDTO> documentRhDTOS = new ArrayList<>();
        for (DocumentRH documentRH : documentRHS) {
            DocumentRhDTO documentRhDTO = new DocumentRhDTO();
            documentRhDTO.setIdDocument(documentRH.getIdDocument());
            documentRhDTO.setStatut(documentRH.getStatut());
            documentRhDTO.setLibelle(documentRH.getLibelle());
            documentRhDTO.setChemin(documentRH.getChemin());
            documentRhDTO.setDateSaisie(documentRH.getDateSaisie());
            documentRhDTO.setDateValidationStatut(documentRH.getDateValidationStatut());

            // Ajoutez les informations sur le demandeur
            Utilisateurs utilisateur_dem = documentRH.getDemandeur();
            if (utilisateur_dem != null) {
                UtilisateursDTO utilisateurDemandeurDTO = convertToUtilisateurDTO(utilisateur_dem);
                documentRhDTO.setDemandeur(utilisateurDemandeurDTO);
            }

            // Informations sur le saisiePar
            Utilisateurs utilisateur_saisiePar = documentRH.getSaisiePar();
            if (utilisateur_saisiePar != null) {
                UtilisateursDTO utilisateurSaisieParDTO = convertToUtilisateurDTO(utilisateur_saisiePar);
                documentRhDTO.setSaisiePar(utilisateurSaisieParDTO);
            }
            // Informations sur le validateur
            Utilisateurs utilisateur_validateur = documentRH.getValidateur();
            if (utilisateur_validateur != null) {
                UtilisateursDTO utilisateurValidateurDTO = convertToUtilisateurDTO(utilisateur_validateur);
                documentRhDTO.setValidateur(utilisateurValidateurDTO);
            }

            // Ajoutez les informations sur ListeAttestation
            ListeAttestation listeAttestation = documentRH.getListeAttestation();
            if (listeAttestation != null) {
                documentRhDTO.setIdListeAttestation(listeAttestation.getIdListeAttestation());
                documentRhDTO.setCode(listeAttestation.getCode());
                documentRhDTO.setMotif(listeAttestation.getMotif());
            }

            documentRhDTOS.add(documentRhDTO);
        }

        return new ResponseEntity<>(documentRhDTOS, HttpStatus.OK);
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
        // Profil profil = utilisateur.getProfil();
        // if (profil != null) {
        //     utilisateurDTO.setProfilId(profil.getIdProfil());
        //     utilisateurDTO.setProfilDesignation(profil.getDesignation());
        // }

        // Informations sur le département
        // Departements departements = utilisateur.getDepartements();
        // if (departements != null) {
        //     utilisateurDTO.setDepartementId(departements.getIdDepartement());
        //     utilisateurDTO.setDepartementDescription(departements.getDescription());
        // }

        // Informations sur le contrat
        // Contrats contrat = utilisateur.getContrats();
        // if (contrat != null) {
        //     utilisateurDTO.setContratId(contrat.getIdContrat());
        //     utilisateurDTO.setContratDesignation(contrat.getDesignation());
        // }

        return utilisateurDTO;
    }


    //GET
//    @GetMapping("/documentRh/{userId}/AllDocumentRh")
//    public ResponseEntity<List<DocumentRH>> getAllDocumentRhByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        List<DocumentRH> documentRHS = new ArrayList<>();
//        documentRHS.addAll(utilisateurs.getDocumentRH());
//
//        return new ResponseEntity<>(documentRHS, HttpStatus.OK);
//    }

//    @GetMapping("/documentRh/{listeAttestationId}/AllDocumentRh")
//    public ResponseEntity<List<DocumentRH>> getAllDocumentRhByListeAttestationId(@PathVariable(value = "listeAttestationId") Long listeAttestationId) {
//        ListeAttestation listeAttestation = listeAttestationRepository.findById(listeAttestationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found ListeAttestation with id = " + listeAttestationId));
//
//        List<DocumentRH> documentRHS = new ArrayList<>();
//        documentRHS.addAll(listeAttestation.getDocumentRHList());
//
//        return new ResponseEntity<>(documentRHS, HttpStatus.OK);
//    }

    //GET
    @GetMapping("/documentRh/{id}")
    public ResponseEntity<DocumentRhDTO> getDocumentRhById(@PathVariable(value = "id") Long id) {
        DocumentRH documentRH = documentRhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found DocumentRh with id = " + id));

        if (documentRH == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        DocumentRhDTO documentRhDTO = new DocumentRhDTO();
        documentRhDTO.setIdDocument(documentRH.getIdDocument());
        documentRhDTO.setDateSaisie(documentRH.getDateSaisie());
        documentRhDTO.setStatut(documentRH.getStatut());
        documentRhDTO.setLibelle(documentRH.getLibelle());
        documentRhDTO.setChemin(documentRH.getChemin());
        documentRhDTO.setDateValidationStatut(documentRH.getDateValidationStatut());

        // Ajoutez les informations sur le demandeur
        Utilisateurs utilisateur_dem = documentRH.getDemandeur();
        if (utilisateur_dem != null) {
            UtilisateursDTO utilisateurDemandeurDTO = convertToUtilisateurDTO(utilisateur_dem);
            documentRhDTO.setDemandeur(utilisateurDemandeurDTO);
        }

        // Informations sur le saisiePar
        Utilisateurs utilisateur_saisiePar = documentRH.getSaisiePar();
        if (utilisateur_saisiePar != null) {
            UtilisateursDTO utilisateurSaisieParDTO = convertToUtilisateurDTO(utilisateur_saisiePar);
            documentRhDTO.setSaisiePar(utilisateurSaisieParDTO);
        }
        // Informations sur le validateur
        Utilisateurs utilisateur_validateur = documentRH.getValidateur();
        if (utilisateur_validateur != null) {
            UtilisateursDTO utilisateurValidateurDTO = convertToUtilisateurDTO(utilisateur_validateur);
            documentRhDTO.setValidateur(utilisateurValidateurDTO);
        }

        // Ajoutez les informations sur ListeAttestation
        ListeAttestation listeAttestation = documentRH.getListeAttestation();
        if (listeAttestation != null) {
            documentRhDTO.setIdListeAttestation(listeAttestation.getIdListeAttestation());
            documentRhDTO.setCode(listeAttestation.getCode());
            documentRhDTO.setMotif(listeAttestation.getMotif());
        }

        return new ResponseEntity<>(documentRhDTO, HttpStatus.OK);
    }

    //POST
//    @PostMapping("/listeAttestation/{listeAttestationId}/documentRh")
//    public ResponseEntity<DocumentRH> createDocumentRhByListeAttestation(@PathVariable(value = "listeAttestationId") Long listeAttestationId,
//                                                   @RequestBody DocumentRH documentRH) {
//        DocumentRH documentRH1 = listeAttestationRepository.findById(listeAttestationId).map(listAtt -> {
//            listAtt.getDocumentRHList().add(documentRH);
//            return documentRhRepository.save(documentRH);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found ListeAttestation with id = " + listeAttestationId));
//
//        return new ResponseEntity<>(documentRH1, HttpStatus.CREATED);
//    }

//    @PostMapping("/users/{userId}/documentRh")
//    public ResponseEntity<DocumentRH> createDocumentRhByUser(@PathVariable(value = "userId") Long userId,
//                                                         @RequestBody DocumentRH documentRH) {
//        DocumentRH documentRH1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getDocumentRH().add(documentRH);
//            return documentRhRepository.save(documentRH);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        return new ResponseEntity<>(documentRH1, HttpStatus.CREATED);
//    }

    //POST
    @PostMapping("/documentRh")
    public ResponseEntity<?> addDocumentRH(@RequestBody DocumentRH documentRH,
                                           @RequestParam Long idDemandeur,
                                           @RequestParam Long idSaisiePar,
                                           @RequestParam(required = false) Long idValidateur,
                                           @RequestParam Long idListeAttestation) {
        try {
            // Rechercher les utilisateurs (demandeur et saisie par)
            Utilisateurs utilisateurs_demandeur = utilisateursRepository.findById(idDemandeur)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs Demandeur non trouvé avec l'ID : " + idDemandeur));
            Utilisateurs utilisateurs_saisiePar = utilisateursRepository.findById(idSaisiePar)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs SaisiePar non trouvé avec l'ID : " + idSaisiePar));

            // Rechercher le validateur uniquement si l'ID est fourni
            Utilisateurs utilisateurs_validateur = null;
            if (idValidateur != null) {
                utilisateurs_validateur = utilisateursRepository.findById(idValidateur)
                        .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs Validateur non trouvé avec l'ID : " + idValidateur));
                documentRH.setValidateur(utilisateurs_validateur); // Si un validateur est fourni, on l'ajoute
            }

            // Rechercher la liste d'attestation
            ListeAttestation listeAttestation = listeAttestationRepository.findById(idListeAttestation)
                    .orElseThrow(() -> new ResourceNotFoundException("ListeAttestation non trouvée avec l'ID : " + idListeAttestation));

            // Assigner les valeurs au document RH
            documentRH.setDemandeur(utilisateurs_demandeur);
            documentRH.setSaisiePar(utilisateurs_saisiePar);
            documentRH.setListeAttestation(listeAttestation);

            // Sauvegarder le document RH
            DocumentRH savedDocumentRH = documentRhRepository.save(documentRH);

            // Réponse de succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1);
            responseObject.put("message", "Document RH créé avec succès.");
            responseObject.put("data", savedDocumentRH);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // Réponse en cas d'erreur
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0);
            errorResponse.put("message", "Erreur lors de la création du document RH.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    @PutMapping("/documentRh/{id}")
    public ResponseEntity<DocumentRH> updateDocumentRh(@PathVariable("id") Long id,
                                                       @RequestBody DocumentRH documentRHReq,
                                                       @RequestParam Long idDemandeur,
                                                       @RequestParam Long idSaisiePar,
                                                       @RequestParam(required = false) Long idValidateur,
                                                       @RequestParam Long idListeAttestation) {
        // Trouver le document RH à mettre à jour
        DocumentRH documentRH = documentRhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentRhId " + id + " not found"));

        // Rechercher les utilisateurs (demandeur et saisie par)
        Utilisateurs utilisateurs_demandeur = utilisateursRepository.findById(idDemandeur)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idDemandeur));
        Utilisateurs utilisateurs_saisiePar = utilisateursRepository.findById(idSaisiePar)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idSaisiePar));

        // Rechercher le validateur uniquement si l'ID est fourni
        if (idValidateur != null) {
            Utilisateurs utilisateurs_validateur = utilisateursRepository.findById(idValidateur)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idValidateur));
            documentRH.setValidateur(utilisateurs_validateur); // Assigner le validateur s'il est présent
        }

        // Rechercher la liste d'attestation
        ListeAttestation listeAttestation = listeAttestationRepository.findById(idListeAttestation)
                .orElseThrow(() -> new ResourceNotFoundException("ListeAttestation non trouvé avec l'ID : " + idListeAttestation));

        // Assigner les valeurs au document RH
        documentRH.setDemandeur(utilisateurs_demandeur);
        documentRH.setSaisiePar(utilisateurs_saisiePar);
        documentRH.setListeAttestation(listeAttestation);

        // Mettre à jour les autres champs du document RH
        documentRH.setDateSaisie(documentRHReq.getDateSaisie());
        documentRH.setChemin(documentRHReq.getChemin());
        documentRH.setStatut(documentRHReq.getStatut());
        documentRH.setLibelle(documentRHReq.getLibelle());
        documentRH.setDateValidationStatut(documentRHReq.getDateValidationStatut());

        // Sauvegarder le document mis à jour
        return new ResponseEntity<>(documentRhRepository.save(documentRH), HttpStatus.OK);
    }


    @DeleteMapping("/documentRh/{id}")
    public ResponseEntity<HttpStatus> deleteDocumentRhById(@PathVariable("id") Long id) {
        documentRhRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/documentRh")
    public ResponseEntity<HttpStatus> deleteAllDocumentRh() {
        documentRhRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
//    @DeleteMapping("/listeAttestation/{listeAttestationId}/documentRh")
//    public ResponseEntity<List<DocumentRH>> deleteAllDocumentRhOfListeAttestation(@PathVariable(value = "listeAttestationId") Long listeAttestationId) {
//        ListeAttestation listeAttestation = listeAttestationRepository.findById(listeAttestationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found ListeAttestation with id = " + listeAttestationId));
//
//        listeAttestation.removeDocumentRH();
//        listeAttestationRepository.save(listeAttestation);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/users/{userId}/documentRh")
//    public ResponseEntity<List<DocumentRH>> deleteAllDocumentRhOfUser(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
//
//        utilisateurs.removeDocumentRH();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }



}
