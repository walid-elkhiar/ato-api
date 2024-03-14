package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Direction;
import com.ato.backendapi.repositories.DirectionRepository;
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
public class DirectionController {

    @Autowired
    private DirectionRepository directionRepository;

    //GET
    @GetMapping("/AllDirections")
    public ResponseEntity<List<Direction>> listeDirections(){
        List<Direction> directions = new ArrayList<Direction>();

        directionRepository.findAll().forEach(directions::add);
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
    public ResponseEntity<Direction> add(@RequestBody Direction direction){
        Direction d =directionRepository.save(direction);
        return new ResponseEntity<>(d,HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/directions/{id}")
    public ResponseEntity<Direction> edit(@PathVariable("id") Long id, @RequestBody Direction direction){
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
