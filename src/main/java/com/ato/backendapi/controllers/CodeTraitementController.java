package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.CodeTraitement;
import com.ato.backendapi.repositories.CodeTraitementRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class CodeTraitementController {

    @Autowired
    private CodeTraitementRepository codeTraitementRepository;

    //GET
    @GetMapping("/AllCodeTraitements")
    public ResponseEntity<List<CodeTraitement>> listeCodeTraitements(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<CodeTraitement> codeTraitements;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<CodeTraitement> pageCodeTraitements = codeTraitementRepository.findAll(PageRequest.of(offset, pageSize));
            codeTraitements = pageCodeTraitements.getContent();
        } else {
            codeTraitements = codeTraitementRepository.findAll();
        }

        if (codeTraitements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(codeTraitements, HttpStatus.OK);
    }


    @GetMapping("/codeTraitements/{id}")
    public ResponseEntity<CodeTraitement> getCodeTraitementById(@PathVariable("id") Long id) {
        CodeTraitement codeTraitement = codeTraitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found CodeTraitement with id = " + id));

        return new ResponseEntity<>(codeTraitement, HttpStatus.OK);
    }
    //POST
    @PostMapping("/codeTraitements")
    public ResponseEntity<?> addCodeTraitement(@RequestBody CodeTraitement codeTraitement) {
        try {
            CodeTraitement savedCodeTraitement = codeTraitementRepository.save(codeTraitement);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Code de traitement créé avec succès."); // Message de succès
            responseObject.put("data", savedCodeTraitement); // Les informations du code de traitement créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du code de traitement
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du code de traitement."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }

    //PUT
    @PutMapping("/codeTraitements/{id}")
    public ResponseEntity<CodeTraitement> editCodeTraitement(@PathVariable("id") Long id, @RequestBody CodeTraitement codeTraitement){
        CodeTraitement codeTraitement1 = codeTraitementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found CodeTraitement with id = " + id));
        codeTraitement1.setCode(codeTraitement.getCode());
        codeTraitement1.setCouleur(codeTraitement.getCouleur());
        codeTraitement1.setLibelle(codeTraitement.getLibelle());
        //codeTraitement1.setSousCodeTraitements(codeTraitement.getSousCodeTraitements());

        return new ResponseEntity<>(codeTraitementRepository.save(codeTraitement1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/codeTraitements/{id}")
    public ResponseEntity<HttpStatus> deleteCodeTraitementById(@PathVariable("id") Long id) {
        codeTraitementRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/codeTraitements")
    public ResponseEntity<HttpStatus> deleteAllCodeTraitements() {
        codeTraitementRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
