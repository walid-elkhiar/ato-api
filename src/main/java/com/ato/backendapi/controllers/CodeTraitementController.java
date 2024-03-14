package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.CodeTraitement;
import com.ato.backendapi.repositories.CodeTraitementRepository;
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
public class CodeTraitementController {

    @Autowired
    private CodeTraitementRepository codeTraitementRepository;

    //GET
    @GetMapping("/AllCodeTraitements")
    public ResponseEntity<List<CodeTraitement>> listeCodeTraitements(){
        List<CodeTraitement> codeTraitements = new ArrayList<CodeTraitement>();

        codeTraitementRepository.findAll().forEach(codeTraitements::add);
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
    public ResponseEntity<CodeTraitement> add(@RequestBody CodeTraitement codeTraitement){
        CodeTraitement a =codeTraitementRepository.save(codeTraitement);
        return new ResponseEntity<>(a,HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/codeTraitements/{id}")
    public ResponseEntity<CodeTraitement> edit(@PathVariable("id") Long id, @RequestBody CodeTraitement codeTraitement){
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
