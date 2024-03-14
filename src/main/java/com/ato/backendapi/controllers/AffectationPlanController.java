package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.*;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class AffectationPlanController {
    @Autowired
    private AffectationPlanRepository affectationPlanRepository;
    @Autowired
    private UtilisateursRepository utilisateursRepository;
    @Autowired
    private PlansTravailRepository plansTravailRepository;
    @Autowired
    private ZoneGpsRepository zoneGpsRepository;
    @Autowired
    private DetailPlansTravailRepository detailPlansTravailRepository;
    @Autowired
    private DetailPlansJournalierRepository detailPlansJournalierRepository;




    //GET
//    @GetMapping("/AllAffectationsPlan")
//    public ResponseEntity<List<AffectationPlan>> listeAffectations(){
//        List<AffectationPlan> affectationPlans = new ArrayList<AffectationPlan>();
//
//        affectationPlanRepository.findAll().forEach(affectationPlans::add);
//        if (affectationPlans.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(affectationPlans, HttpStatus.OK);
//    }
    @GetMapping("/AllAffectationsPlan")
    public ResponseEntity<List<AffectationPlanDTO>> listeAffectations(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<AffectationPlan> affectationPlans;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<AffectationPlan> pageAffectationPlans = affectationPlanRepository.findAll(PageRequest.of(offset, pageSize));
            affectationPlans = pageAffectationPlans.getContent();
        } else {
            affectationPlans = affectationPlanRepository.findAll();
        }

        if (affectationPlans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<AffectationPlanDTO> affectationPlanDTOs = new ArrayList<>();
        for (AffectationPlan affectationPlan : affectationPlans) {
            AffectationPlanDTO affectationPlanDTO = new AffectationPlanDTO();
            affectationPlanDTO.setIdAffectationPlan(affectationPlan.getIdAffectationPlan());
            affectationPlanDTO.setDatePlan(affectationPlan.getDatePlan());
            affectationPlanDTO.setTypePlan(affectationPlan.getTypePlan());
            affectationPlanDTO.setDateCycle(affectationPlan.getDateCycle());

            // Ajoutez les informations sur l'utilisateur
            Utilisateurs utilisateurs = affectationPlan.getUtilisateurs();
            if (utilisateurs != null) {
                affectationPlanDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
                affectationPlanDTO.setMatricule(utilisateurs.getMatricule());
                affectationPlanDTO.setNom(utilisateurs.getNom());
                affectationPlanDTO.setPrenom(utilisateurs.getPrenom());
                affectationPlanDTO.setAdresseMail(utilisateurs.getAdresseMail());
                affectationPlanDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
                affectationPlanDTO.setTelephone(utilisateurs.getTelephone());
                affectationPlanDTO.setDateEntree(utilisateurs.getDateEntree());
                affectationPlanDTO.setActif(utilisateurs.isActif());
                affectationPlanDTO.setPhoto(utilisateurs.getPhoto());
                affectationPlanDTO.setPassword(utilisateurs.getPassword());
                affectationPlanDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
                affectationPlanDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
                affectationPlanDTO.setDateEntree(utilisateurs.getDateEntree());
                affectationPlanDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
                affectationPlanDTO.setRole(utilisateurs.getRole());
            }

            // Ajoutez les informations sur la zoneGps
            ZoneGPS zoneGPS = affectationPlan.getZoneGPS();
            if (zoneGPS != null) {
                affectationPlanDTO.setZoneGpsId(zoneGPS.getIdZone());
                affectationPlanDTO.setLibelleZone(zoneGPS.getLibelleZone());
                affectationPlanDTO.setRayon(zoneGPS.getRayon());
                affectationPlanDTO.setCentreLong(zoneGPS.getCentreLong());
                affectationPlanDTO.setCentreLat(zoneGPS.getCentreLat());
            }

            // Ajoutez les informations sur le PlanTravail
            PlansTravail plansTravail = affectationPlan.getPlansTravail();
            if (plansTravail != null) {
                affectationPlanDTO.setPlanTravailId(plansTravail.getIdPlansTravail());
                affectationPlanDTO.setType(plansTravail.getType());
                affectationPlanDTO.setCode(plansTravail.getCode());
                affectationPlanDTO.setLibelle(plansTravail.getLibelle());
                affectationPlanDTO.setNbrJour(plansTravail.getNbrJour());
            }

            affectationPlanDTOs.add(affectationPlanDTO);
        }

        return new ResponseEntity<>(affectationPlanDTOs, HttpStatus.OK);
    }


    //Get All Affectations+filtre par datePlan
    @GetMapping("/AffectationsPlan")
    public ResponseEntity<List<AffectationPlanDTO>> allAffectationPlansAvecFiltreDatePlan(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date datePlan) {
        List<AffectationPlan> affectationPlans;

        if (datePlan != null) {
            affectationPlans = affectationPlanRepository.findByDatePlan(datePlan);
        } else {
            affectationPlans = affectationPlanRepository.findAll();
        }

        List<AffectationPlanDTO> affectationPlanDTOs = affectationPlans.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(affectationPlanDTOs);
    }

    private AffectationPlanDTO mapToDTO(AffectationPlan affectationPlan) {
        AffectationPlanDTO dto = new AffectationPlanDTO();
        dto.setIdAffectationPlan(affectationPlan.getIdAffectationPlan());
        dto.setDatePlan(affectationPlan.getDatePlan());
        dto.setDateCycle(affectationPlan.getDateCycle());
        dto.setTypePlan(affectationPlan.getTypePlan());

        // Informations sur Utilisateurs
        Utilisateurs user = affectationPlan.getUtilisateurs();
        dto.setIdUtilisateur(user.getIdUtilisateur());
        dto.setMatricule(user.getMatricule());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setAdresseMail(user.getAdresseMail());
        dto.setPosteUtilisateur(user.getPosteUtilisateur());
        dto.setTelephone(user.getTelephone());
        dto.setDateEntree(user.getDateEntree());
        dto.setActif(user.isActif());
        dto.setPhoto(user.getPhoto());
        dto.setPassword(user.getPassword());
        dto.setAdresseIpTel(user.getAdresseIpTel());
        dto.setDatePassModifie(user.getDatePassModifie());
        dto.setDateFinContrat(user.getDateFinContrat());
        dto.setRole(user.getRole());

        // Informations sur le ZoneGps
        ZoneGPS zoneGPS = affectationPlan.getZoneGPS();
        dto.setZoneGpsId(zoneGPS.getIdZone());
        dto.setLibelleZone(zoneGPS.getLibelleZone());
        dto.setCentreLat(zoneGPS.getCentreLat());
        dto.setCentreLong(zoneGPS.getCentreLong());
        dto.setRayon(zoneGPS.getRayon());

        // Informations sur le PlanTravail
        PlansTravail plansTravail = affectationPlan.getPlansTravail();
        dto.setPlanTravailId(plansTravail.getIdPlansTravail());
        dto.setCode(plansTravail.getCode());
        dto.setLibelle(plansTravail.getLibelle());
        dto.setType(plansTravail.getType());
        dto.setNbrJour(plansTravail.getNbrJour());

        return dto;
    }

//    @GetMapping("/affectations/{userId}/AllAffectations")
//    public ResponseEntity<List<AffectationPlan>> getAllAffectationsByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<AffectationPlan> affectationPlans = new ArrayList<>();
//        affectationPlans.addAll(utilisateurs.getAffectations());
//
//        return new ResponseEntity<>(affectationPlans, HttpStatus.OK);
//    }

//    @GetMapping("/affectations/{planTravailId}/AllAffectations")
//    public ResponseEntity<List<AffectationPlan>> getAllAffectationsByPlanTravailId(@PathVariable(value = "planTravailId") Long planTravailId) {
//        PlansTravail plansTravail = plansTravailRepository.findById(planTravailId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found PlanTravail with id = " + planTravailId));
//
//        List<AffectationPlan> affectationPlans = new ArrayList<>();
//        affectationPlans.addAll(plansTravail.getAffectationPlans());
//
//        return new ResponseEntity<>(affectationPlans, HttpStatus.OK);
//    }

//    @GetMapping("/affectations/{zoneGpsId}/AllAffectations")
//    public ResponseEntity<List<AffectationPlan>> getAllAffectationsByZoneGpsId(@PathVariable(value = "zoneGpsId") Long zoneGpsId) {
//        ZoneGPS zoneGPS = zoneGpsRepository.findById(zoneGpsId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found ZoneGps with id = " + zoneGpsId));
//
//        List<AffectationPlan> affectationPlans = new ArrayList<>();
//        affectationPlans.addAll(zoneGPS.getAffectationPlans());
//
//        return new ResponseEntity<>(affectationPlans, HttpStatus.OK);
//    }

//    @GetMapping("/affectations/{id}")
//    public ResponseEntity<AffectationPlan> getAffectationById(@PathVariable("id") Long id) {
//        AffectationPlan affectationPlan = affectationPlanRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Affectation with id = " + id));
//
//        return new ResponseEntity<>(affectationPlan, HttpStatus.OK);
//    }

    @GetMapping("/affectations/{id}")
    public ResponseEntity<AffectationPlanDTO> getAffectationById(@PathVariable("id") Long id) {
        AffectationPlan affectationPlan = affectationPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Affectation with id = " + id));


        if (affectationPlan == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        AffectationPlanDTO affectationPlanDTO = new AffectationPlanDTO();
        affectationPlanDTO.setIdAffectationPlan(affectationPlan.getIdAffectationPlan());
        affectationPlanDTO.setDatePlan(affectationPlan.getDatePlan());
        affectationPlanDTO.setTypePlan(affectationPlan.getTypePlan());
        affectationPlanDTO.setDateCycle(affectationPlan.getDateCycle());


        // Ajoutez les informations sur l'utilisateur
        Utilisateurs utilisateurs = affectationPlan.getUtilisateurs();
        if (utilisateurs != null) {
            affectationPlanDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
            affectationPlanDTO.setMatricule(utilisateurs.getMatricule());
            affectationPlanDTO.setNom(utilisateurs.getNom());
            affectationPlanDTO.setPrenom(utilisateurs.getPrenom());
            affectationPlanDTO.setAdresseMail(utilisateurs.getAdresseMail());
            affectationPlanDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
            affectationPlanDTO.setTelephone(utilisateurs.getTelephone());
            affectationPlanDTO.setDateEntree(utilisateurs.getDateEntree());
            affectationPlanDTO.setActif(utilisateurs.isActif());
            affectationPlanDTO.setPhoto(utilisateurs.getPhoto());
            affectationPlanDTO.setPassword(utilisateurs.getPassword());
            affectationPlanDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
            affectationPlanDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
            affectationPlanDTO.setDateEntree(utilisateurs.getDateEntree());
            affectationPlanDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
            affectationPlanDTO.setRole(utilisateurs.getRole());

        }

        // Ajoutez les informations sur la zoneGps
        ZoneGPS zoneGPS = affectationPlan.getZoneGPS();
        if (zoneGPS != null) {
            affectationPlanDTO.setZoneGpsId(zoneGPS.getIdZone());
            affectationPlanDTO.setLibelleZone(zoneGPS.getLibelleZone());
            affectationPlanDTO.setRayon(zoneGPS.getRayon());
            affectationPlanDTO.setCentreLong(zoneGPS.getCentreLong());
            affectationPlanDTO.setCentreLat(zoneGPS.getCentreLat());
        }

        // Ajoutez les informations sur le PlanTravail
        PlansTravail plansTravail = affectationPlan.getPlansTravail();
        if (plansTravail != null) {
            affectationPlanDTO.setPlanTravailId(plansTravail.getIdPlansTravail());
            affectationPlanDTO.setType(plansTravail.getType());
            affectationPlanDTO.setCode(plansTravail.getCode());
            affectationPlanDTO.setLibelle(plansTravail.getLibelle());
            affectationPlanDTO.setNbrJour(plansTravail.getNbrJour());
        }

        return new ResponseEntity<>(affectationPlanDTO, HttpStatus.OK);
    }

//    @GetMapping("/utilisateur/{userId}/between-dates")
//    public ResponseEntity<List<AffectationPlanDTO>> getAffectationsByUtilisateurAndDateRange(
//            @PathVariable Long userId,
//            @RequestParam("startDate") String startDateString,
//            @RequestParam("endDate") String endDateString) {
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//            Date startDate = dateFormat.parse(startDateString);
//            Date endDate = dateFormat.parse(endDateString);
//
//            List<AffectationPlan> affectationPlans = affectationPlanRepository
//                    .findByUtilisateurs_IdUtilisateurAndDatePlanBetween(userId, startDate, endDate);
//
//            List<AffectationPlanDTO> affectationPlanDTOs = new ArrayList<>();
//            for (AffectationPlan affectationPlan : affectationPlans) {
//                AffectationPlanDTO affectationPlanDTO = new AffectationPlanDTO();
//                affectationPlanDTO.setIdAffectationPlan(affectationPlan.getIdAffectationPlan());
//                affectationPlanDTO.setDatePlan(affectationPlan.getDatePlan());
//                affectationPlanDTO.setTypePlan(affectationPlan.getTypePlan());
//                affectationPlanDTO.setDateCycle(affectationPlan.getDateCycle());
//
//                // Ajoutez les informations sur l'utilisateur
//                Utilisateurs utilisateurs = affectationPlan.getUtilisateurs();
//                if (utilisateurs != null) {
//                    affectationPlanDTO.setIdUtilisateur(utilisateurs.getIdUtilisateur());
//                    affectationPlanDTO.setMatricule(utilisateurs.getMatricule());
//                    affectationPlanDTO.setNom(utilisateurs.getNom());
//                    affectationPlanDTO.setPrenom(utilisateurs.getPrenom());
//                    affectationPlanDTO.setAdresseMail(utilisateurs.getAdresseMail());
//                    affectationPlanDTO.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
//                    affectationPlanDTO.setTelephone(utilisateurs.getTelephone());
//                    affectationPlanDTO.setDateEntree(utilisateurs.getDateEntree());
//                    affectationPlanDTO.setActif(utilisateurs.getActif());
//                    affectationPlanDTO.setPhoto(utilisateurs.getPhoto());
//                    affectationPlanDTO.setPassword(utilisateurs.getPassword());
//                    affectationPlanDTO.setAdresseIpTel(utilisateurs.getAdresseIpTel());
//                    affectationPlanDTO.setDatePassModifie(utilisateurs.getDatePassModifie());
//                    affectationPlanDTO.setDateEntree(utilisateurs.getDateEntree());
//                    affectationPlanDTO.setDateFinContrat(utilisateurs.getDateFinContrat());
//                    affectationPlanDTO.setRole(utilisateurs.getRole());
//                }
//
//                // Ajoutez les informations sur la zoneGps
//                ZoneGPS zoneGPS = affectationPlan.getZoneGPS();
//                if (zoneGPS != null) {
//                    affectationPlanDTO.setZoneGpsId(zoneGPS.getIdZone());
//                    affectationPlanDTO.setLibelleZone(zoneGPS.getLibelleZone());
//                    affectationPlanDTO.setRayon(zoneGPS.getRayon());
//                    affectationPlanDTO.setCentreLong(zoneGPS.getCentreLong());
//                    affectationPlanDTO.setCentreLat(zoneGPS.getCentreLat());
//                }
//
//                // Ajoutez les informations sur le PlanTravail
//                PlansTravail plansTravail = affectationPlan.getPlansTravail();
//                if (plansTravail != null) {
//                    affectationPlanDTO.setPlanTravailId(plansTravail.getIdPlansTravail());
//                    affectationPlanDTO.setType(plansTravail.getType());
//                    affectationPlanDTO.setCode(plansTravail.getCode());
//                    affectationPlanDTO.setLibelle(plansTravail.getLibelle());
//                    affectationPlanDTO.setNbrJour(plansTravail.getNbrJour());
//                }
//
//                affectationPlanDTOs.add(affectationPlanDTO);
//            }
//
//            return new ResponseEntity<>(affectationPlanDTOs, HttpStatus.OK);
//        } catch (ParseException e) {
//            // Gérer les erreurs de parsing de date ici ...
//            // Par exemple, vous pouvez renvoyer une réponse d'erreur avec un code approprié
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/affectations")
    public ResponseEntity<?> addAffectationPlan(@RequestBody AffectationPlan affectationPlan,
                                                @RequestParam List<Long> idUsers,
                                                @RequestParam Long idZone,
                                                @RequestParam Long idPlanTravail,
                                                @RequestParam("dateDebut") String dateDebut,
                                                @RequestParam("dateFin") String dateFin) {
        try {
            LocalDate start = LocalDate.parse(dateDebut);
            LocalDate end = LocalDate.parse(dateFin);
            List<Long> allAffectationIds = new ArrayList<>();

            for (Long idUser : idUsers) {
                Utilisateurs utilisateur = utilisateursRepository.findById(idUser)
                        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + idUser));

                affectationPlanRepository.deleteAffectationsInInterval(utilisateur,
                        Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant()));

                List<Long> affectationIds;
                if (affectationPlan.getTypePlan().equals("Cyclique")) {
                    if (affectationPlan.getDateCycle() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Le champ 'dateCycle' est requis pour un plan cyclique.");
                    }
                    affectationIds = handleCycliquePlan(affectationPlan, utilisateur, idZone, idPlanTravail, start, end);
                } else {
                    affectationIds = handleNormalPlan(affectationPlan, utilisateur, idZone, idPlanTravail, start, end);
                }

                allAffectationIds.addAll(affectationIds);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Affectations créées avec succès. IDs : " + allAffectationIds);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur non prévue lors de la création des affectations de plan. Détails : " + e.getMessage());
        }
    }

    private List<Long> handleCycliquePlan(AffectationPlan affectationPlan, Utilisateurs utilisateur, Long idZone, Long idPlanTravail, LocalDate start, LocalDate end) {
        try {
            ZoneGPS zoneGPS = zoneGpsRepository.findById(idZone)
                    .orElseThrow(() -> new ResourceNotFoundException("ZoneGPS non trouvée avec l'ID : " + idZone));
            PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
                    .orElseThrow(() -> new ResourceNotFoundException("PlanTravail non trouvé avec l'ID : " + idPlanTravail));

            List<AffectationPlan> savedAffectations = new ArrayList<>();
            //int cycleDays = plansTravail.getDetailPlansTravail().size();
            //LocalDate dateCycle = affectationPlan.getDateCycle().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                AffectationPlan newAffectation = new AffectationPlan();
                newAffectation.setUtilisateurs(utilisateur);
                newAffectation.setZoneGPS(zoneGPS);
                newAffectation.setDatePlan(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                newAffectation.setTypePlan(affectationPlan.getTypePlan()); // Utiliser directement le typePlan passé dans le body
                newAffectation.setPlansTravail(plansTravail);
                newAffectation.setDateCycle(affectationPlan.getDateCycle());

                AffectationPlan savedAffectation = affectationPlanRepository.save(newAffectation);
                savedAffectations.add(savedAffectation);
            }

            return savedAffectations.stream()
                    .map(AffectationPlan::getIdAffectationPlan)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création des affectations cycliques.");
        }
    }


    // Gérer le plan normal pour chaque utilisateur
    private List<Long> handleNormalPlan(AffectationPlan affectationPlan, Utilisateurs utilisateur, Long idZone, Long idPlanTravail, LocalDate start, LocalDate end) {
        try {
            ZoneGPS zoneGPS = zoneGpsRepository.findById(idZone)
                    .orElseThrow(() -> new ResourceNotFoundException("ZoneGPS non trouvée avec l'ID : " + idZone));
            PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
                    .orElseThrow(() -> new ResourceNotFoundException("PlanTravail non trouvé avec l'ID : " + idPlanTravail));
            List<AffectationPlan> savedAffectations = new ArrayList<>();

            // Créer les affectations pour chaque jour entre start et end
            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                AffectationPlan newAffectation = new AffectationPlan();
                newAffectation.setUtilisateurs(utilisateur);
                newAffectation.setZoneGPS(zoneGPS);
                newAffectation.setDatePlan(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                newAffectation.setTypePlan(affectationPlan.getTypePlan());
                newAffectation.setPlansTravail(plansTravail);

                AffectationPlan savedAffectation = affectationPlanRepository.save(newAffectation);
                savedAffectations.add(savedAffectation);
            }

            return savedAffectations.stream()
                    .map(AffectationPlan::getIdAffectationPlan)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création des affectations de plan normal.");
        }
    }

    @GetMapping("/utilisateur/{userId}/between-dates")
    public ResponseEntity<List<Map<String, Object>>> getAffectationsBetween(
            @PathVariable Long userId,
            @RequestParam("startDate") String startDateString,
            @RequestParam("endDate") String endDateString) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            List<AffectationPlan> affectationPlans = affectationPlanRepository
                    .findByUtilisateurs_IdUserAndDatePlanBetween(userId, startDate, endDate);
            List<Map<String, Object>> response = new ArrayList<>();
            Set<String> addedDates = new HashSet<>();

            for (AffectationPlan affectationPlan : affectationPlans) {
                Utilisateurs utilisateurs = affectationPlan.getUtilisateurs();
                Date datePlan = affectationPlan.getDatePlan();
                String formattedDatePlan = formatDate(datePlan);
                String typePlan = affectationPlan.getTypePlan();

                PlansTravail plansTravail = affectationPlan.getPlansTravail();
                List<DetailPlansTravail> detailPlansTravailList = detailPlansTravailRepository.findByPlansTravail(plansTravail);

                if (typePlan.equalsIgnoreCase("Cyclique")) {
                    Date dateCycle = affectationPlan.getDateCycle();
                    long daysBetween = ChronoUnit.DAYS.between(
                            dateCycle.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                            datePlan.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    );

                    // Calculer le jour du cycle en tenant compte des jours précédant `dateCycle`
                    int dayIndex = (int) ((daysBetween % detailPlansTravailList.size() + detailPlansTravailList.size()) % detailPlansTravailList.size());
                    DetailPlansTravail detailPlansTravail = detailPlansTravailList.get(dayIndex);

                    PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
                    List<DetailPlansJournalier> detailPlansJournaliers = detailPlansJournalierRepository.findByPlansJournalier_Id(plansJournalier.getIdPlansJournalier());

                    for (DetailPlansJournalier detailPlansJournalier : detailPlansJournaliers) {
                        String uniqueEntryKey = utilisateurs.getIdUtilisateur() + "-" + formattedDatePlan + "-" + detailPlansJournalier.getHeureDebut();

                        if (addedDates.add(uniqueEntryKey)) {
                            Map<String, Object> entry = new HashMap<>();
                            entry.put("idUtilisateur", utilisateurs.getIdUtilisateur());
                            entry.put("matricule", utilisateurs.getMatricule());
                            entry.put("nom", utilisateurs.getNom());
                            entry.put("prenom", utilisateurs.getPrenom());
                            entry.put("typePlan", typePlan);
                            entry.put("datePlan", formattedDatePlan);
                            entry.put("libellePlanJournalier", plansJournalier.getLibelle());
                            entry.put("heureDebut", detailPlansJournalier.getHeureDebut());
                            entry.put("heureFin", detailPlansJournalier.getHeureFin());

                            response.add(entry);
                        }
                    }
                } else {
                    String dayOfWeek = datePlan.toInstant().atZone(ZoneId.systemDefault()).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                    for (DetailPlansTravail detailPlansTravail : detailPlansTravailList) {
                        if (detailPlansTravail.getNomJour().equalsIgnoreCase(dayOfWeek)) {
                            PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
                            List<DetailPlansJournalier> detailPlansJournaliers = detailPlansJournalierRepository.findByPlansJournalier_Id(plansJournalier.getIdPlansJournalier());

                            for (DetailPlansJournalier detailPlansJournalier : detailPlansJournaliers) {
                                String uniqueEntryKey = utilisateurs.getIdUtilisateur() + "-" + formattedDatePlan + "-" + detailPlansJournalier.getHeureDebut();

                                if (addedDates.add(uniqueEntryKey)) {
                                    Map<String, Object> entry = new HashMap<>();
                                    entry.put("idUtilisateur", utilisateurs.getIdUtilisateur());
                                    entry.put("matricule", utilisateurs.getMatricule());
                                    entry.put("nom", utilisateurs.getNom());
                                    entry.put("prenom", utilisateurs.getPrenom());
                                    entry.put("typePlan", typePlan);
                                    entry.put("datePlan", formattedDatePlan);
                                    entry.put("libellePlanJournalier", plansJournalier.getLibelle());
                                    entry.put("heureDebut", detailPlansJournalier.getHeureDebut());
                                    entry.put("heureFin", detailPlansJournalier.getHeureFin());

                                    response.add(entry);
                                }
                            }
                        }
                    }
                }
            }

            return ResponseEntity.ok(response);

        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(Collections.singletonList(Map.of("error", "Invalid date format")));
        }
    }

    private String formatDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + localDate.toString();
    }







    //POST
//    @PostMapping("/affectations")
//    public ResponseEntity<?> addAffectationPlan(@RequestBody AffectationPlan affectationPlan,
//                                                @RequestParam List<Long> idUsers,
//                                                @RequestParam Long idZone,
//                                                @RequestParam Long idPlanTravail,
//                                                @RequestParam("dateDebut") String dateDebut,
//                                                @RequestParam("dateFin") String dateFin) {
//        try {
//            LocalDate start;
//            LocalDate end;
//            try {
//                start = LocalDate.parse(dateDebut);
//                end = LocalDate.parse(dateFin);
//            } catch (DateTimeParseException e) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de format de date. Utilisez le format AAAA-MM-JJ.");
//            }
//
//            if (affectationPlan.getTypePlan() == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le champs 'typePlan' est requis.");
//            }
//
//            ZoneGPS zoneGPS = zoneGpsRepository.findById(idZone)
//                    .orElseThrow(() -> new ResourceNotFoundException("ZoneGPS non trouvée avec l'ID : " + idZone));
//            PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
//                    .orElseThrow(() -> new ResourceNotFoundException("PlanTravail non trouvé avec l'ID : " + idPlanTravail));
//
//            List<AffectationPlan> savedAffectations = new ArrayList<>();
//
//            for (Long idUser : idUsers) {
//                Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
//                        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + idUser));
//
//                affectationPlanRepository.deleteAffectationsInInterval(utilisateurs,
//                        Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()),
//                        Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//
//                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
//                    DayOfWeek dayOfWeek = date.getDayOfWeek();
//
//                    // Récupérer le détail du plan de travail pour le jour spécifique
////                    Optional<DetailPlansTravail> detailPlanOpt = plansTravail.getDetailPlansTravail().stream()
////                            .filter(d -> d.getNomJour().equalsIgnoreCase(dayOfWeek.name()))
////                            .findFirst();
//
////                    if (detailPlanOpt.isEmpty()) {
////                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
////                                .body("Détail de plan de travail manquant pour le jour : " + dayOfWeek);
////                    }
//
////                    DetailPlansTravail detailPlan = detailPlanOpt.get();
////                    PlansJournalier plansJournalier = detailPlan.getPlansJournalier()
//                    AffectationPlan newAffectation = new AffectationPlan();
//                    newAffectation.setUtilisateurs(utilisateurs);
//                    newAffectation.setZoneGPS(zoneGPS);
//                    newAffectation.setDatePlan(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                    newAffectation.setDateCycle(affectationPlan.getTypePlan().equalsIgnoreCase(TypePlansTravail.CYCLIQUE.name()) ? affectationPlan.getDateCycle() : null);
//                    newAffectation.setTypePlan(affectationPlan.getTypePlan());
//                    newAffectation.setPlansTravail(plansTravail);
//
//                    // Ajout du détail du plan journalier via le `PlansJournalier`
//                    // Si besoin, une référence au `DetailPlansTravail` peut être ajoutée à `AffectationPlan`
//
//                    AffectationPlan savedAffectation = affectationPlanRepository.save(newAffectation);
//                    savedAffectations.add(savedAffectation);
//                }
//            }
//
//            List<Long> affectationIds = savedAffectations.stream()
//                    .map(AffectationPlan::getIdAffectationPlan)
//                    .toList();
//            return ResponseEntity.status(HttpStatus.CREATED).body("Affectations créées avec succès. IDs : " + affectationIds);
//
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erreur non prévue lors de la création des affectations de plan. Détails : " + e.getMessage());
//        }
//    }


    //@GetMapping("/utilisateur/{userId}/between-dates")
    //    public ResponseEntity<List<Map<String, Object>>> getAffectationsBetween(
    //            @PathVariable Long userId,
    //            @RequestParam("startDate") String startDateString,
    //            @RequestParam("endDate") String endDateString) {
    //
    //        try {
    //            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //            Date startDate = dateFormat.parse(startDateString);
    //            Date endDate = dateFormat.parse(endDateString);
    //
    //            List<AffectationPlan> affectationPlans = affectationPlanRepository.findByUtilisateurs_IdUserAndDatePlanBetween(userId, startDate, endDate);
    //            List<Map<String, Object>> response = new ArrayList<>();
    //
    //            // Ensemble pour garder une trace des dates déjà ajoutées
    //            Set<String> addedDates = new HashSet<>();
    //
    //            for (AffectationPlan affectationPlan : affectationPlans) {
    //                Utilisateurs utilisateurs = affectationPlan.getUtilisateurs();
    //                PlansTravail plansTravail = affectationPlan.getPlansTravail();
    //                String datePlan = formatDate(affectationPlan.getDatePlan());
    //                String typePlan = affectationPlan.getTypePlan();
    //
    //                List<DetailPlansTravail> detailPlansTravailList = detailPlansTravailRepository.findByPlansTravail(plansTravail);
    //
    //                for (DetailPlansTravail detailPlansTravail : detailPlansTravailList) {
    //                    PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
    //                    List<DetailPlansJournalier> detailPlansJournaliers = detailPlansJournalierRepository.findByPlansJournalier_Id(plansJournalier.getIdPlansJournalier());
    //
    //                    // Créer un ensemble pour éviter les doublons de temps
    //                    Set<String> addedTimes = new HashSet<>();
    //
    //                    for (DetailPlansJournalier detailPlansJournalier : detailPlansJournaliers) {
    //                        String uniqueEntryKey = utilisateurs.getIdUtilisateur() + "-" + datePlan + "-" + detailPlansJournalier.getHeureDebut();
    //
    //                        // Vérifiez si l'entrée a déjà été ajoutée
    //                        String timeRange = detailPlansJournalier.getHeureDebut().toString() + detailPlansJournalier.getHeureFin().toString(); // Concaténation en String
    //                        if (addedDates.add(datePlan) && addedTimes.add(timeRange)) {  // Vérification des dates
    //                            Map<String, Object> entry = new HashMap<>();
    //                            entry.put("idUtilisateur", utilisateurs.getIdUtilisateur());
    //                            entry.put("matricule", utilisateurs.getMatricule());
    //                            entry.put("nom", utilisateurs.getNom());
    //                            entry.put("prenom", utilisateurs.getPrenom());
    //                            entry.put("typePlan", typePlan);
    //                            entry.put("datePlan", datePlan);
    //                            entry.put("libellePlanJournalier", plansJournalier.getLibelle());
    //                            entry.put("heureDebut", detailPlansJournalier.getHeureDebut());
    //                            entry.put("heureFin", detailPlansJournalier.getHeureFin());
    //
    //                            response.add(entry);
    //                        }
    //                    }
    //                }
    //            }
    //
    //            return ResponseEntity.ok(response);
    //
    //        } catch (ParseException e) {
    //            return ResponseEntity.badRequest().body(Collections.singletonList(Map.of("error", "Invalid date format")));
    //        }
    //    }
    //
    //    private String formatDate(Date date) {
    //        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    //        // Formatage de la date selon vos besoins
    //        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRANCE) + " " + localDate.toString();
    //    }

//    @GetMapping("/utilisateur/{userId}/between-dates")
//    public ResponseEntity<List<Map<String, Object>>> getAffectationsBetween(
//            @PathVariable Long userId,
//            @RequestParam("startDate") String startDateString,
//            @RequestParam("endDate") String endDateString) {
//
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date startDate = dateFormat.parse(startDateString);
//            Date endDate = dateFormat.parse(endDateString);
//
//            List<AffectationPlan> affectationPlans = affectationPlanRepository
//                    .findByUtilisateurs_IdUserAndDatePlanBetween(userId, startDate, endDate);
//            List<Map<String, Object>> response = new ArrayList<>();
//            Set<String> addedDates = new HashSet<>();
//
//            for (AffectationPlan affectationPlan : affectationPlans) {
//                Utilisateurs utilisateurs = affectationPlan.getUtilisateurs();
//                Date datePlan = affectationPlan.getDatePlan();
//                String formattedDatePlan = formatDate(datePlan);
//                String typePlan = affectationPlan.getTypePlan();
//
//                PlansTravail plansTravail = affectationPlan.getPlansTravail();
//                List<DetailPlansTravail> detailPlansTravailList = detailPlansTravailRepository.findByPlansTravail(plansTravail);
//
//                // Vérifier si le plan est de type cyclique ou normal
//                if (typePlan.equalsIgnoreCase("Cyclique")) {
//                    Date dateCycle = affectationPlan.getDateCycle();
//                    long daysBetween = ChronoUnit.DAYS.between(
//                            dateCycle.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
//                            datePlan.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
//                    );
//
//                    // Assurer que daysBetween est positif pour éviter l'index -1
//                    if (daysBetween >= 0) {
//                        int dayIndex = (int) (daysBetween % detailPlansTravailList.size());
//                        DetailPlansTravail detailPlansTravail = detailPlansTravailList.get(dayIndex);
//
//                        PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
//                        List<DetailPlansJournalier> detailPlansJournaliers = detailPlansJournalierRepository.findByPlansJournalier_Id(plansJournalier.getIdPlansJournalier());
//
//                        for (DetailPlansJournalier detailPlansJournalier : detailPlansJournaliers) {
//                            String uniqueEntryKey = utilisateurs.getIdUtilisateur() + "-" + formattedDatePlan + "-" + detailPlansJournalier.getHeureDebut();
//
//                            if (addedDates.add(uniqueEntryKey)) {
//                                Map<String, Object> entry = new HashMap<>();
//                                entry.put("idUtilisateur", utilisateurs.getIdUtilisateur());
//                                entry.put("matricule", utilisateurs.getMatricule());
//                                entry.put("nom", utilisateurs.getNom());
//                                entry.put("prenom", utilisateurs.getPrenom());
//                                entry.put("typePlan", typePlan);
//                                entry.put("datePlan", formattedDatePlan);
//                                entry.put("libellePlanJournalier", plansJournalier.getLibelle());
//                                entry.put("heureDebut", detailPlansJournalier.getHeureDebut());
//                                entry.put("heureFin", detailPlansJournalier.getHeureFin());
//
//                                response.add(entry);
//                            }
//                        }
//                    } else {
//                        // Gérer le cas où daysBetween est négatif
//                        System.out.println("Erreur : le jour calculé dans le cycle est négatif pour la date " + formattedDatePlan);
//                    }
//                }
//                else {
//                    // Logique pour les plans normaux (comme existant dans votre code actuel)
//                    String dayOfWeek = datePlan.toInstant().atZone(ZoneId.systemDefault()).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
//
//                    for (DetailPlansTravail detailPlansTravail : detailPlansTravailList) {
//                        if (detailPlansTravail.getNomJour().equalsIgnoreCase(dayOfWeek)) {
//                            PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
//                            List<DetailPlansJournalier> detailPlansJournaliers = detailPlansJournalierRepository.findByPlansJournalier_Id(plansJournalier.getIdPlansJournalier());
//
//                            for (DetailPlansJournalier detailPlansJournalier : detailPlansJournaliers) {
//                                String uniqueEntryKey = utilisateurs.getIdUtilisateur() + "-" + formattedDatePlan + "-" + detailPlansJournalier.getHeureDebut();
//
//                                if (addedDates.add(uniqueEntryKey)) {
//                                    Map<String, Object> entry = new HashMap<>();
//                                    entry.put("idUtilisateur", utilisateurs.getIdUtilisateur());
//                                    entry.put("matricule", utilisateurs.getMatricule());
//                                    entry.put("nom", utilisateurs.getNom());
//                                    entry.put("prenom", utilisateurs.getPrenom());
//                                    entry.put("typePlan", typePlan);
//                                    entry.put("datePlan", formattedDatePlan);
//                                    entry.put("libellePlanJournalier", plansJournalier.getLibelle());
//                                    entry.put("heureDebut", detailPlansJournalier.getHeureDebut());
//                                    entry.put("heureFin", detailPlansJournalier.getHeureFin());
//
//                                    response.add(entry);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            return ResponseEntity.ok(response);
//
//        } catch (ParseException e) {
//            return ResponseEntity.badRequest().body(Collections.singletonList(Map.of("error", "Invalid date format")));
//        }
//    }
//
//    private String formatDate(Date date) {
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + localDate.toString();
//    }





//    @GetMapping("/utilisateur/{idUser}/affectations")
//    public ResponseEntity<List<Map<String, Object>>> getAffectations(
//            @PathVariable("idUser") Long idUser,
//            @RequestParam("startDate") String startDateString,
//            @RequestParam("endDate") String endDateString) {
//
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date startDate = dateFormat.parse(startDateString);
//            Date endDate = dateFormat.parse(endDateString);
//
//            // Récupère toutes les affectations de l'utilisateur dans l'intervalle
//            List<AffectationPlan> affectationPlans = affectationPlanRepository.findByUtilisateurs_IdUserAndDatePlanBetween(idUser, startDate, endDate);
//
//            System.out.println("Nombre d'affectationPlans trouvés : " + affectationPlans.size());
//            List<Map<String, Object>> response = new ArrayList<>();
//
//            for (AffectationPlan affectationPlan : affectationPlans) {
//                System.out.println("Traitement de AffectationPlan avec ID : " + affectationPlan.getIdAffectationPlan());
//
//                Utilisateurs utilisateurs = affectationPlan.getUtilisateurs();
//                PlansTravail plansTravail = affectationPlan.getPlansTravail();
//                String typePlan = affectationPlan.getTypePlan();
//                LocalDate datePlan = affectationPlan.getDatePlan().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//                LocalDate currentDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                LocalDate endDateLocal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//                while (!currentDate.isAfter(endDateLocal)) {
//                    String nomJour = null;
//
//                    if ("Normal".equalsIgnoreCase(typePlan) && currentDate.isEqual(datePlan)) {
//                        nomJour = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toLowerCase();
//                    } else if ("Cyclique".equalsIgnoreCase(typePlan)) {
//                        long daysBetween = ChronoUnit.DAYS.between(datePlan, currentDate);
//                        int cycleDay = (int) (daysBetween % plansTravail.getNbrJour()) + 1;
//                        nomJour = "day" + cycleDay;
//                    }
//
//                    System.out.println("NomJour déterminé : " + nomJour);
//
//                    if (nomJour == null) {
//                        currentDate = currentDate.plusDays(1);
//                        continue;
//                    }
//
//                    List<DetailPlansTravail> detailPlansTravailList = detailPlansTravailRepository.findByPlansTravailAndNomJour(plansTravail, nomJour);
//                    System.out.println("Nombre de DetailPlansTravail trouvés pour " + nomJour + " : " + detailPlansTravailList.size());
//
//                    for (DetailPlansTravail detailPlansTravail : detailPlansTravailList) {
//                        PlansJournalier plansJournalier = detailPlansTravail.getPlansJournalier();
//                        List<DetailPlansJournalier> detailPlansJournaliers = detailPlansJournalierRepository.findByPlansJournalier_Id(plansJournalier.getIdPlansJournalier());
//
//                        for (DetailPlansJournalier detailPlansJournalier : detailPlansJournaliers) {
//                            Map<String, Object> entry = new HashMap<>();
//                            entry.put("idUtilisateur", utilisateurs.getIdUtilisateur());
//                            entry.put("matricule", utilisateurs.getMatricule());
//                            entry.put("nom", utilisateurs.getNom());
//                            entry.put("prenom", utilisateurs.getPrenom());
//                            entry.put("heureDebut", detailPlansJournalier.getHeureDebut());
//                            entry.put("heureFin", detailPlansJournalier.getHeureFin());
//                            entry.put("typePlan", affectationPlan.getTypePlan());
//                            entry.put("datePlan", formatDate(Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
//                            entry.put("libellePlanJournalier", plansJournalier.getLibelle());
//
//                            response.add(entry);
//                        }
//                    }
//
//                    currentDate = currentDate.plusDays(1);
//                }
//            }
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//
//        } catch (ParseException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    //Post 3adiya
//    @PostMapping("/affectations")
//    public ResponseEntity<?> add(@RequestBody AffectationPlan affectationPlan,
//                                 @RequestParam List<Long> idUsers,
//                                 @RequestParam Long idZone,
//                                 @RequestParam Long idPlanTravail,
//                                 @RequestParam("dateDebut") String dateDebut,
//                                 @RequestParam("dateFin") String dateFin) {
//        try {
//            ZoneGPS zoneGPS = zoneGpsRepository.findById(idZone)
//                    .orElseThrow(() -> new ResourceNotFoundException("ZoneGPS non trouvée avec l'ID : " + idZone));
//            PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
//                    .orElseThrow(() -> new ResourceNotFoundException("PlanTravail non trouvé avec l'ID : " + idPlanTravail));
//
//            List<AffectationPlan> savedAffectations = new ArrayList<>();
//
//            LocalDate start = LocalDate.parse(dateDebut);
//            LocalDate end = LocalDate.parse(dateFin);
//
//            for (Long idUser : idUsers) {
//                Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
//                        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + idUser));
//
//                // Parcourir les dates entre dateDebut et dateFin
//                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
//                    AffectationPlan newAffectation = new AffectationPlan();
//                    newAffectation.setUtilisateurs(utilisateurs);
//                    newAffectation.setPlansTravail(plansTravail);
//                    newAffectation.setZoneGPS(zoneGPS);
//                    newAffectation.setDatePlan(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                    newAffectation.setDateCycle(affectationPlan.getDateCycle());
//                    newAffectation.setTypePlan(affectationPlan.getTypePlan());
//
//                    AffectationPlan savedAffectation = affectationPlanRepository.save(newAffectation);
//                    savedAffectations.add(savedAffectation);
//                }
//            }
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedAffectations);
//        } catch (DateTimeParseException | ResourceNotFoundException e) {
//            // Gestion des exceptions spécifiques
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la création des affectations de plan. Cause : " + e.getMessage());
//        } catch (Exception e) {
//            // Gestion des exceptions générales
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur non prévue lors de la création des affectations de plan.");
//        }
//    }


//    @PostMapping("/users/{userId}/affectations")
//    public ResponseEntity<AffectationPlan> createAffectationByUserId(@PathVariable(value = "userId") Long userId,
//                                                  @RequestBody AffectationPlan affectationPlan) {
//        AffectationPlan affectationPlan1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getAffectations().add(affectationPlan);
//            return affectationPlanRepository.save(affectationPlan);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(affectationPlan1, HttpStatus.CREATED);
//    }

//    @PostMapping("/planTravail/{planTravailId}/affectations")
//    public ResponseEntity<AffectationPlan> createAffectationByPlanTravailId(@PathVariable(value = "planTravailId") Long planTravailId,
//                                                                     @RequestBody AffectationPlan affectationPlan) {
//        AffectationPlan affectationPlan1 = plansTravailRepository.findById(planTravailId).map(plTr -> {
//            plTr.getAffectationPlans().add(affectationPlan);
//            return affectationPlanRepository.save(affectationPlan);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found PlanTravail with id = " + planTravailId));
//
//        return new ResponseEntity<>(affectationPlan1, HttpStatus.CREATED);
//    }

//    @PostMapping("/zoneGps/{zoneGpsId}/affectations")
//    public ResponseEntity<AffectationPlan> createAffectationByZoneGpsId(@PathVariable(value = "zoneGpsId") Long zoneGpsId,
//                                                                            @RequestBody AffectationPlan affectationPlan) {
//        AffectationPlan affectationPlan1 = zoneGpsRepository.findById(zoneGpsId).map(zone -> {
//            zone.getAffectationPlans().add(affectationPlan);
//            return affectationPlanRepository.save(affectationPlan);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found ZoneGps with id = " + zoneGpsId));
//
//        return new ResponseEntity<>(affectationPlan1, HttpStatus.CREATED);
//    }

    //Affecter

//        @PostMapping("/affecter")
//        public ResponseEntity<String> affecterPlanTravail(@RequestBody AffectationPlanDTO affectationPlanDTO) {
//            // Recherche de l'utilisateur, de la zone GPS et du plan de travail à partir des IDs fournis
//            Optional<Utilisateurs> optionalUtilisateur = utilisateursRepository.findById(affectationPlanDTO.getIdUtilisateur());
//            Optional<ZoneGPS> optionalZoneGPS = zoneGpsRepository.findById(affectationPlanDTO.getZoneGpsId());
//            Optional<PlansTravail> optionalPlansTravail = plansTravailRepository.findById(affectationPlanDTO.getPlanTravailId());
//
//            if (optionalUtilisateur.isPresent() && optionalZoneGPS.isPresent() && optionalPlansTravail.isPresent()) {
//                Utilisateurs utilisateur = optionalUtilisateur.get();
//                ZoneGPS zoneGPS = optionalZoneGPS.get();
//                PlansTravail plansTravail = optionalPlansTravail.get();
//
//                // Création de l'affectation du plan de travail
//                AffectationPlan affectationPlan = new AffectationPlan();
//                affectationPlan.setDatePlan(affectationPlanDTO.getDatePlan());
//                affectationPlan.setDateCycle(affectationPlanDTO.getDateCycle());
//                affectationPlan.setTypePlan(affectationPlanDTO.getTypePlan());
//                affectationPlan.setUtilisateurs(utilisateur);
//                affectationPlan.setZoneGPS(zoneGPS);
//                affectationPlan.setPlansTravail(plansTravail);
//
//                // Enregistrement de l'affectation du plan de travail dans la base de données
//                affectationPlanRepository.save(affectationPlan);
//
//                return new ResponseEntity<>("Affectation de plan de travail réussie", HttpStatus.CREATED);
//            } else {
//                return new ResponseEntity<>("Utilisateur, zone GPS ou plan de travail introuvable", HttpStatus.NOT_FOUND);
//            }
//        }




    //PUT
    @PutMapping("/affectations/{id}")
    public ResponseEntity<AffectationPlan> editAffectationPlan(@PathVariable("id") Long id, @RequestBody AffectationPlan affectationPlan,
                                                               @RequestParam Long idUser,
                                                               @RequestParam Long idZone,
                                                               @RequestParam Long idPlanTravail){
        AffectationPlan affectationPlan1 = affectationPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found AffectationPlan with id = " + id));
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        ZoneGPS zoneGPS = zoneGpsRepository.findById(idZone)
                .orElseThrow(() -> new ResourceNotFoundException("ZoneGps non trouvé avec l'ID : " + idZone));
        PlansTravail plansTravail = plansTravailRepository.findById(idPlanTravail)
                .orElseThrow(() -> new ResourceNotFoundException("PlanTravail non trouvé avec l'ID : " + idPlanTravail));
        affectationPlan1.setUtilisateurs(utilisateurs);
        affectationPlan1.setPlansTravail(plansTravail);
        affectationPlan1.setZoneGPS(zoneGPS);
        affectationPlan1.setDatePlan(affectationPlan.getDatePlan());
        affectationPlan1.setTypePlan(affectationPlan.getTypePlan());
        affectationPlan1.setDateCycle(affectationPlan.getDateCycle());

        return new ResponseEntity<>(affectationPlanRepository.save(affectationPlan1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/affectations/{id}")
    public ResponseEntity<HttpStatus> deleteAffectationById(@PathVariable("id") Long id) {
        affectationPlanRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/affectations")
    public ResponseEntity<HttpStatus> deleteAllAffectations() {
        affectationPlanRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/affectations")
//    public ResponseEntity<List<AffectationPlan>> deleteAllAffectationsOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeAffectations();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/planTravail/{planTravailId}/affectations")
//    public ResponseEntity<List<AffectationPlan>> deleteAllAffectationsOfPlanTravail(@PathVariable(value = "planTravailId") Long planTravailId) {
//        PlansTravail plansTravail = plansTravailRepository.findById(planTravailId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + planTravailId));
//
//        plansTravail.removeAffectationsPlan();
//        plansTravailRepository.save(plansTravail);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/zoneGps/{zoneGpsId}/affectations")
//    public ResponseEntity<List<AffectationPlan>> deleteAllAffectationsOfZoneGps(@PathVariable(value = "zoneGpsId") Long zoneGpsId) {
//        ZoneGPS zoneGPS = zoneGpsRepository.findById(zoneGpsId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + zoneGpsId));
//
//        zoneGPS.removeAffectationsPlan();
//        zoneGpsRepository.save(zoneGPS);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
