package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DepartementDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.DepartementsRepository;
import com.ato.backendapi.repositories.DirectionRepository;
import com.ato.backendapi.repositories.UtilisateursRepository;
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
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class DepartementsController {
    @Autowired
    private DepartementsRepository departementsRepository;
    @Autowired
    private DirectionRepository directionRepository;
    @Autowired
    private UtilisateursRepository utilisateursRepository;

    //GET
//    @GetMapping("/AllDepartements")
//    public ResponseEntity<List<Departements>> listeDepartements(){
//        List<Departements> departements= new ArrayList<Departements>();
//
//        departementsRepository.findAll().forEach(departements::add);
//        if (departements.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(departements, HttpStatus.OK);
//    }

    @GetMapping("/AllDepartements")
    public ResponseEntity<List<DepartementDTO>> listeDepartements(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Departements> departements;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Departements> pageDepartements = departementsRepository.findAll(PageRequest.of(offset, pageSize));
            departements = pageDepartements.getContent();
        } else {
            departements = departementsRepository.findAll();
        }

        if (departements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DepartementDTO> departementDTOs = new ArrayList<>();
        for (Departements departement : departements) {
            DepartementDTO departementDTO = new DepartementDTO();
            departementDTO.setIdDepartement(departement.getIdDepartement());
            departementDTO.setDescription(departement.getDescription());

            // Récupérer le nombre d'utilisateurs pour ce département
            int userCount = utilisateursRepository.countByDepartements(departement);
            departementDTO.setUserCount(userCount);

            // Ajouter les informations sur la direction
            Direction direction = departement.getDirection();
            if (direction != null) {
                departementDTO.setDirectionId(direction.getIdDirection());
                departementDTO.setDirectionDescription(direction.getDescription());
            }

            departementDTOs.add(departementDTO);
        }

        return new ResponseEntity<>(departementDTOs, HttpStatus.OK);
    }


    //
    @GetMapping("/departements")
    public ResponseEntity<List<DepartementDTO>> getDepartements(@RequestParam(required = false) Long idDirection) {
        List<Departements> departements;

        if (idDirection != null) {
            departements = departementsRepository.findByDirection_IdDirection(idDirection);
        } else {
            departements = departementsRepository.findAll();
        }

        List<DepartementDTO> departementDTOs = departements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(departementDTOs);
    }

    private DepartementDTO convertToDTO(Departements departement) {
        DepartementDTO dto = new DepartementDTO();
        dto.setIdDepartement(departement.getIdDepartement());
        dto.setDescription(departement.getDescription());
        dto.setDirectionId(departement.getDirection().getIdDirection());
        dto.setDirectionDescription(departement.getDirection().getDescription());
        dto.setUserCount(departement.getUtilisateurs().size());  // Compter le nombre d'utilisateurs
        return dto;
    }


    //@GetMapping("/{idUtilisateur}/departements")
//    public ResponseEntity<List<Departements>> getAllDepartementsByUtilisateurId(@PathVariable Long idUtilisateur) {
//        Optional<Utilisateurs> utilisateurOptional = utilisateursRepository.findById(idUtilisateur);
//
//        if (!utilisateurOptional.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        Utilisateurs utilisateur = utilisateurOptional.get();
//        Departements departements = utilisateur.getDepartements();
//
//        if (departements == null) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<Departements> departementsList = Collections.singletonList(departements);
//        return new ResponseEntity<>(departementsList, HttpStatus.OK);
//    }

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

//    @GetMapping("/departements/{id}")
//    public ResponseEntity<Departements> getDepartementById(@PathVariable("id") Long id) {
//        Departements departements = departementsRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Department with id = " + id));
//
//        return new ResponseEntity<>(departements, HttpStatus.OK);
//    }
    @GetMapping("/departements/{id}")
    public ResponseEntity<DepartementDTO> getDepartementById(@PathVariable("id") Long id) {
        Departements departements = departementsRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Not found Department with id = " + id));


        if (departements == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        DepartementDTO departementDTO = new DepartementDTO();
        departementDTO.setIdDepartement(departements.getIdDepartement());
        departementDTO.setDescription(departements.getDescription());


        // Ajoutez les informations sur la direction
        Direction direction = departements.getDirection();
        if (direction != null) {
            departementDTO.setDirectionId(direction.getIdDirection());
            departementDTO.setDirectionDescription(direction.getDescription());
        }

        return new ResponseEntity<>(departementDTO, HttpStatus.OK);
    }

    //POST
    @PostMapping("/departements")
    public ResponseEntity<?> addDepartements(@RequestBody Departements departements, @RequestParam Long idDirection) {
        try {
            Direction direction = directionRepository.findById(idDirection)
                    .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvé avec l'ID : " + idDirection));

            departements.setDirection(direction);
            Departements savedDepartements = departementsRepository.save(departements);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Département créé avec succès."); // Message de succès
            responseObject.put("data", savedDepartements); // Les informations du département créé
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création du département
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création du département."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
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
    public ResponseEntity<Departements> editDepartements(@PathVariable("id") Long id,
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
