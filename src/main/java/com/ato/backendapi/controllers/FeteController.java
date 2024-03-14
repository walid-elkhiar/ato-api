package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Fete;
import com.ato.backendapi.repositories.FeteRepository;
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
public class FeteController {

    @Autowired
    private FeteRepository feteRepository;


    //GET
    @GetMapping("/AllFetes")
    public ResponseEntity<List<Fete>> getAllFetes(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Fete> fetes;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Fete> fetesPage = feteRepository.findAll(PageRequest.of(offset, pageSize));
            fetes = fetesPage.getContent();
        } else {
            fetes = feteRepository.findAll();
        }

        if (fetes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(fetes, HttpStatus.OK);
    }



    @GetMapping("/fetes/{id}")
    public ResponseEntity<Fete> getFeteById(@PathVariable("id") long id) {
        Fete fete = feteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Fete with id = " + id));

        return new ResponseEntity<>(fete, HttpStatus.OK);
    }

    //POST
    @PostMapping("/fetes")
    public ResponseEntity<?> addFete(@RequestBody Fete fete) {
        try {
            Fete savedFete = feteRepository.save(fete);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Fête créée avec succès."); // Message de succès
            responseObject.put("data", savedFete); // Les informations de la fête créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de la fête
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de la fête."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    //PUT
    @PutMapping("/fetes/{id}")
    public ResponseEntity<Fete> updateFete(@PathVariable("id") long id, @RequestBody Fete fete) {
        Fete _f = feteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Fete with id = " + id));

        _f.setDateDebut(fete.getDateDebut());
        _f.setDuree(fete.getDuree());
        _f.setEvenement(fete.getEvenement());
        _f.setDateFin(fete.getDateFin());


        return new ResponseEntity<>(feteRepository.save(_f), HttpStatus.OK);
    }


    //DELETE
    @DeleteMapping("/fetes/{id}")
    public ResponseEntity<HttpStatus> deleteFeteById(@PathVariable("id") long id) {

        feteRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/fetes")
    public ResponseEntity<HttpStatus> deleteAllFetes() {
        feteRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
