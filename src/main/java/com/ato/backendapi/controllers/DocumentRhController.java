package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<DocumentRH>> getAllDocumentRh() {
        List<DocumentRH> documentRHS = new ArrayList<DocumentRH>();

        documentRhRepository.findAll().forEach(documentRHS::add);
        if (documentRHS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(documentRHS, HttpStatus.OK);
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
    public ResponseEntity<DocumentRH> getDocumentRhById(@PathVariable(value = "id") Long id) {
        DocumentRH documentRH = documentRhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found DocumentRh with id = " + id));

        return new ResponseEntity<>(documentRH, HttpStatus.OK);
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

    @PutMapping("/documentRh/{id}")
    public ResponseEntity<DocumentRH> updateDocumentRh(@PathVariable("id") Long id, @RequestBody DocumentRH documentRHReq) {
        DocumentRH documentRH = documentRhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentRhId " + id + "not found"));

        documentRH.setDateSaisie(documentRHReq.getDateSaisie());
        documentRH.setChemin(documentRHReq.getChemin());
        documentRH.setStatut(documentRHReq.getStatut());
        documentRH.setLibelle(documentRHReq.getLibelle());
        documentRH.setDateValidationStatut(documentRHReq.getDateValidationStatut());


        return new ResponseEntity<>(documentRhRepository.save(documentRH), HttpStatus.OK);
    }

    @DeleteMapping("/documentRh/{id}")
    public ResponseEntity<HttpStatus> deleteDocumentRh(@PathVariable("id") Long id) {
        documentRhRepository.deleteById(id);

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

    @DeleteMapping("/documentRh")
    public ResponseEntity<HttpStatus> deleteAllDocumentRh() {
        documentRhRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
