package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.ZoneGPS;
import com.ato.backendapi.repositories.ZoneGpsRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class ZoneGpsController {

    @Autowired
    private ZoneGpsRepository zoneGpsRepository;

    //GET
    @GetMapping("/AllZoneGps")
    public ResponseEntity<Map<String, Object>> listeZoneGps(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<ZoneGPS> zoneGPSList;
        Map<String, Object> response = new HashMap<>();

        if (offset == null || pageSize == null) {
            // Retrieve all entries without pagination
            zoneGPSList = zoneGpsRepository.findAll();
            response.put("AllZones", zoneGPSList);
            response.put("totalItems", zoneGPSList.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Use pagination
            Pageable pageable = PageRequest.of(offset, pageSize);
            Page<ZoneGPS> zoneGPSPage = zoneGpsRepository.findAll(pageable);

            if (zoneGPSPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            zoneGPSList = zoneGPSPage.getContent();
            response.put("AllZones", zoneGPSList);
            response.put("currentPage", zoneGPSPage.getNumber());
            response.put("totalItems", zoneGPSPage.getTotalElements());
            response.put("totalPages", zoneGPSPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/zoneGps/{id}")
    public ResponseEntity<ZoneGPS> getZoneGpsById(@PathVariable("id") Long id) {
        ZoneGPS zoneGPS = zoneGpsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ZoneGps with id = " + id));

        return new ResponseEntity<>(zoneGPS, HttpStatus.OK);
    }

    //POST
    @PostMapping("/zoneGps")
    public ResponseEntity<?> addZoneGPS(@RequestBody ZoneGPS zoneGPS) {
        try {
            ZoneGPS z = zoneGpsRepository.save(zoneGPS);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Zone GPS créée avec succès."); // Message de succès
            responseObject.put("data", z); // Les informations de la zone GPS créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de la zone GPS
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de la zone GPS."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


    //PUT
    @PutMapping("/zoneGps/{id}")
    public ResponseEntity<ZoneGPS> editZoneGPS(@PathVariable("id") Long id, @RequestBody ZoneGPS zoneGPS){
        ZoneGPS zoneGPS1 = zoneGpsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ZoneGps with id = " + id));
        zoneGPS1.setLibelleZone(zoneGPS.getLibelleZone());
        zoneGPS1.setRayon(zoneGPS.getRayon());
        zoneGPS1.setCentreLat(zoneGPS.getCentreLat());
        zoneGPS1.setCentreLong(zoneGPS.getCentreLong());

        return new ResponseEntity<>(zoneGpsRepository.save(zoneGPS1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/zoneGps/{id}")
    public ResponseEntity<HttpStatus> deleteZoneGpsById(@PathVariable("id") Long id) {
        zoneGpsRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/zoneGps")
    public ResponseEntity<HttpStatus> deleteAllZoneGps() {
        zoneGpsRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
