package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Contrats;
import com.ato.backendapi.repositories.ContratsRepository;
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
public class ContratsController {

    @Autowired
    private ContratsRepository contratsRepository;

    //GET
    @GetMapping("/AllContrats")
    public ResponseEntity<List<Contrats>> listeContrats(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Contrats> contrats;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Contrats> pageContrats = contratsRepository.findAll(PageRequest.of(offset, pageSize));
            contrats = pageContrats.getContent();
        } else {
            contrats = contratsRepository.findAll();
        }

        if (contrats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(contrats, HttpStatus.OK);
    }


    @GetMapping("/contrats/{id}")
    public ResponseEntity<Contrats> getContratById(@PathVariable("id") Long id) {
        Contrats contrats = contratsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Contrat with id = " + id));

        return new ResponseEntity<>(contrats, HttpStatus.OK);
    }
    //POST
    @PostMapping("/contrats")
    public ResponseEntity<?> addContrats(@RequestBody Contrats contrats) {
        try {
            Contrats savedContrats = contratsRepository.save(contrats);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Contrat créé avec succès."); // Message de succès
            responseObject.put("data", savedContrats); // Les informations du contrat créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du contrat
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du contrat."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    //PUT
    @PutMapping("/contrats/{id}")
    public ResponseEntity<Contrats> editContrats(@PathVariable("id") Long id, @RequestBody Contrats contrats){
        Contrats contrats1 = contratsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Contrat with id = " + id));
        contrats1.setDesignation(contrats.getDesignation());
        //contrats1.setUtilisateurs(contrats.getUtilisateurs());

        return new ResponseEntity<>(contratsRepository.save(contrats1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/contrats/{id}")
    public ResponseEntity<HttpStatus> deleteContratById(@PathVariable("id") Long id) {
        contratsRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/contrats")
    public ResponseEntity<HttpStatus> deleteAllContrats() {
        contratsRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
