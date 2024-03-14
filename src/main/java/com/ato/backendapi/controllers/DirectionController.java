package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Direction;
import com.ato.backendapi.repositories.DirectionRepository;
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
public class DirectionController {

    @Autowired
    private DirectionRepository directionRepository;

    //GET
    @GetMapping("/AllDirections")
    public ResponseEntity<List<Direction>> listeDirections(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Direction> directions;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Direction> directionsPage = directionRepository.findAll(PageRequest.of(offset, pageSize));
            directions = directionsPage.getContent();
        } else {
            directions = directionRepository.findAll();
        }

        if (directions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(directions, HttpStatus.OK);
    }


    @GetMapping("/directions/{id}")
    public ResponseEntity<Direction> getDirectionById(@PathVariable("id") Long id) {
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Direction with id = " + id));

        return new ResponseEntity<>(direction, HttpStatus.OK);
    }
    //POST
    @PostMapping("/directions")
    public ResponseEntity<?> addDirection(@RequestBody Direction direction) {
        try {
            Direction savedDirection = directionRepository.save(direction);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Direction créée avec succès."); // Message de succès
            responseObject.put("data", savedDirection); // Les informations de la direction créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de la direction
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de la direction."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    //PUT
    @PutMapping("/directions/{id}")
    public ResponseEntity<Direction> editDirection(@PathVariable("id") Long id, @RequestBody Direction direction){
        Direction direction1 = directionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Direction with id = " + id));
        //direction1.setDepartements(direction.getDepartements());
        direction1.setDescription(direction.getDescription());

        return new ResponseEntity<>(directionRepository.save(direction1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/directions/{id}")
    public ResponseEntity<HttpStatus> deleteDirectionById(@PathVariable("id") Long id) {
        directionRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/directions")
    public ResponseEntity<HttpStatus> deleteAllDirections() {
        directionRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
