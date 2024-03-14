package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.ZoneGPS;
import com.ato.backendapi.repositories.ZoneGpsRepository;
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
public class ZoneGpsController {

    @Autowired
    private ZoneGpsRepository zoneGpsRepository;

    //GET
    @GetMapping("/AllZoneGps")
    public ResponseEntity<List<ZoneGPS>> listeZoneGps(){
        List<ZoneGPS> zoneGPS = new ArrayList<ZoneGPS>();

        zoneGpsRepository.findAll().forEach(zoneGPS::add);
        if (zoneGPS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(zoneGPS, HttpStatus.OK);
    }

    @GetMapping("/zoneGps/{id}")
    public ResponseEntity<ZoneGPS> getZoneGpsById(@PathVariable("id") Long id) {
        ZoneGPS zoneGPS = zoneGpsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ZoneGps with id = " + id));

        return new ResponseEntity<>(zoneGPS, HttpStatus.OK);
    }

    //POST
    @PostMapping("/zoneGps")
    public ResponseEntity<ZoneGPS> add(@RequestBody ZoneGPS zoneGPS){
        ZoneGPS z = zoneGpsRepository.save(zoneGPS);
        return new ResponseEntity<>(z,HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("/zoneGps/{id}")
    public ResponseEntity<ZoneGPS> edit(@PathVariable("id") Long id, @RequestBody ZoneGPS zoneGPS){
        ZoneGPS zoneGPS1 = zoneGpsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found ZoneGps with id = " + id));
        zoneGPS1.setLibelleZone(zoneGPS.getLibelleZone());
        zoneGPS1.setRayon(zoneGPS.getRayon());
        //zoneGPS1.setAffectationPlans(zoneGPS.getAffectationPlans());
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
