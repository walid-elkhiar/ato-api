package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Departements;
import com.ato.backendapi.entities.Direction;
import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.DepartementsRepository;
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
public class DepartementsController {
    @Autowired
    private DepartementsRepository departementsRepository;
    @Autowired
    private DirectionRepository directionRepository;

    //GET
    @GetMapping("/AllDepartements")
    public ResponseEntity<List<Departements>> listeDepartements(){
        List<Departements> departements= new ArrayList<Departements>();

        departementsRepository.findAll().forEach(departements::add);
        if (departements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(departements, HttpStatus.OK);
    }

    //GET
//    @GetMapping("/departements/{directionId}/AllDepartements")
//    public ResponseEntity<List<Departements>> getAllDepartementsByDirectionId(@PathVariable(value = "directionId") Long directionId) {
//        Direction direction = directionRepository.findById(directionId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Direction with id = " + directionId));
//
//        List<Departements> departements = new ArrayList<>();
//        departements.addAll(direction.getDepartements());
//
//        return new ResponseEntity<>(departements, HttpStatus.OK);
//    }

    @GetMapping("/departements/{id}")
    public ResponseEntity<Departements> getDepartementById(@PathVariable("id") Long id) {
        Departements departements = departementsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Department with id = " + id));

        return new ResponseEntity<>(departements, HttpStatus.OK);
    }
    //POST
    @PostMapping("/departements")
    public ResponseEntity<Departements> add(@RequestBody Departements departements,@RequestParam Long idDirection){
        Direction direction = directionRepository.findById(idDirection)
                .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvé avec l'ID : " + idDirection));

        departements.setDirection(direction);
        Departements d = departementsRepository.save(departements);
        return new ResponseEntity<>(d, HttpStatus.CREATED);
    }

//    @PostMapping("/directions/{directionId}/departements")
//    public ResponseEntity<Departements> createDepartementByDirectionId(@PathVariable(value = "directionId") Long directionId,
//                                                   @RequestBody Departements departements) {
//        Departements departements1 = directionRepository.findById(directionId).map(dir -> {
//            dir.getDepartements().add(departements);
//            return departementsRepository.save(departements);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Direction with id = " + directionId));
//
//        return new ResponseEntity<>(departements1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/departements/{id}")
    public ResponseEntity<Departements> edit(@PathVariable("id") Long id,
                                             @RequestBody Departements departements,@RequestParam Long idDirection){
        Direction direction = directionRepository.findById(idDirection)
                .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvé avec l'ID : " + idDirection));
        Departements departements1 = departementsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Department with id = " + id));
        departements1.setDirection(direction);
        departements1.setDescription(departements.getDescription());
//        departements1.setUtilisateurs(departements.getUtilisateurs());

        return new ResponseEntity<>(departementsRepository.save(departements1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/departements/{id}")
    public ResponseEntity<HttpStatus> deleteDepartementById(@PathVariable("id") Long id) {
        departementsRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/departements")
    public ResponseEntity<HttpStatus> deleteAllDepartements() {
        departementsRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/directions/{directionId}/departements")
//    public ResponseEntity<List<Departements>> deleteAllDepartementsOfDirection(@PathVariable(value = "directionId") Long directionId) {
//        Direction direction = directionRepository.findById(directionId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Direction with id = " + directionId));
//
//        direction.removeDepartements();
//        directionRepository.save(direction);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
