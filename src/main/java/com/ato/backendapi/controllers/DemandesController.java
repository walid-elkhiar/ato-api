package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DemandesDTO;
import com.ato.backendapi.dto.UtilisateursDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
import com.ato.backendapi.services.DemandesService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class DemandesController {

    @Autowired
    private DemandesRepository demandesRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    @Autowired
    private DemandesService demandesService;

    @Autowired
    private FeteRepository feteRepository;

    @Autowired
    private AffectationPlanRepository affectationPlanRepository;

    @Autowired
    private CompteurRepository compteursRepository;

    @Autowired
    private DetailPlansJournalierRepository detailPlansJournalierRepository;

    //GET
    @GetMapping("/AllDemandes")
    public ResponseEntity<List<DemandesDTO>> listeDemandes(
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Demandes> demandes;

        // Vérifiez si les paramètres offset et pageSize sont fournis
        if (offset != null && pageSize != null) {
            Page<Demandes> pageDemandes = demandesRepository.findAll(PageRequest.of(offset, pageSize));
            demandes = pageDemandes.getContent();
        } else {
            demandes = demandesRepository.findAll();
        }

        if (demandes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<DemandesDTO> demandesDTOS = new ArrayList<>();
        for (Demandes demande : demandes) {
            DemandesDTO demandesDTO = new DemandesDTO();
            demandesDTO.setIdDemande(demande.getIdDemande());
            demandesDTO.setCodeMotif(demande.getCodeMotif());
            demandesDTO.setDuree(demande.getDuree());
            demandesDTO.setPlage(demande.getPlage());
            demandesDTO.setObjet(demande.getObjet());
            demandesDTO.setDateReprise(demande.getDateReprise());
            demandesDTO.setDateSaisie(demande.getDateSaisie());
            demandesDTO.setDateDebutMission(demande.getDateDebutMission());
            demandesDTO.setDateFinMission(demande.getDateFinMission());
            demandesDTO.setEtatValidation(demande.getEtatValidation());

            // Informations sur l'utilisateur demande
            Utilisateurs utilisateur = demande.getUtilisateurs();
            if (utilisateur != null) {
                UtilisateursDTO utilisateurDTO = convertToUtilisateurDTO(utilisateur);
                demandesDTO.setUtilisateur_demande(utilisateurDTO);
            }

            // Informations sur l'utilisateur saisie de la demande
            Utilisateurs utilisateurSaisieDemande = demande.getUtilisateur_saisi_demande();
            if (utilisateurSaisieDemande != null) {
                UtilisateursDTO utilisateurSaisieDemandeDTO = convertToUtilisateurDTO(utilisateurSaisieDemande);
                demandesDTO.setUtilisateur_saisi_demande(utilisateurSaisieDemandeDTO);
            }

            demandesDTOS.add(demandesDTO);
        }

        return new ResponseEntity<>(demandesDTOS, HttpStatus.OK);
    }

    private UtilisateursDTO convertToUtilisateurDTO(Utilisateurs utilisateur) {
        UtilisateursDTO utilisateurDTO = new UtilisateursDTO();
        utilisateurDTO.setIdUtilisateur(utilisateur.getIdUtilisateur());
        utilisateurDTO.setMatricule(utilisateur.getMatricule());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setAdresseMail(utilisateur.getAdresseMail());
        utilisateurDTO.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
        utilisateurDTO.setTelephone(utilisateur.getTelephone());
        utilisateurDTO.setDateEntree(utilisateur.getDateEntree());
        utilisateurDTO.setActif(utilisateur.isActif());
        utilisateurDTO.setPhoto(utilisateur.getPhoto());
        utilisateurDTO.setPassword(utilisateur.getPassword());
        utilisateurDTO.setAdresseIpTel(utilisateur.getAdresseIpTel());
        utilisateurDTO.setDatePassModifie(utilisateur.getDatePassModifie());
        utilisateurDTO.setDateFinContrat(utilisateur.getDateFinContrat());
        utilisateurDTO.setRole(utilisateur.getRole());

        return utilisateurDTO;
    }


//    @GetMapping("/demandes/{userId}/AllDemandes")
//    public ResponseEntity<List<Demandes>> getAllDemandesByUserId(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        List<Demandes> demandes = new ArrayList<>();
//        demandes.addAll(utilisateurs.getDemandes());
//
//        return new ResponseEntity<>(demandes, HttpStatus.OK);
//    }

    @GetMapping("/demandes/{id}")
    public ResponseEntity<DemandesDTO> getDemandeById(@PathVariable("id") Long id) {
        Optional<Demandes> demandesOptional = demandesRepository.findById(id);

        if (demandesOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Demandes demandes = demandesOptional.get();
        DemandesDTO demandesDTO = convertToDemandesDTO(demandes);

        return new ResponseEntity<>(demandesDTO, HttpStatus.OK);
    }

    private DemandesDTO convertToDemandesDTO(Demandes demandes) {
        DemandesDTO demandesDTO = new DemandesDTO();
        demandesDTO.setIdDemande(demandes.getIdDemande());
        demandesDTO.setCodeMotif(demandes.getCodeMotif());
        demandesDTO.setDuree(demandes.getDuree());
        demandesDTO.setPlage(demandes.getPlage());
        demandesDTO.setObjet(demandes.getObjet());
        demandesDTO.setDateReprise(demandes.getDateReprise());
        demandesDTO.setDateSaisie(demandes.getDateSaisie());
        demandesDTO.setDateDebutMission(demandes.getDateDebutMission());
        demandesDTO.setDateFinMission(demandes.getDateFinMission());
        demandesDTO.setEtatValidation(demandes.getEtatValidation());

        // Convertir les informations sur l'utilisateur demande
        Utilisateurs utilisateur = demandes.getUtilisateurs();
        if (utilisateur != null) {
            UtilisateursDTO utilisateurDTO = new UtilisateursDTO();
            utilisateurDTO.setIdUtilisateur(utilisateur.getIdUtilisateur());
            utilisateurDTO.setMatricule(utilisateur.getMatricule());
            utilisateurDTO.setNom(utilisateur.getNom());
            utilisateurDTO.setPrenom(utilisateur.getPrenom());
            utilisateurDTO.setAdresseMail(utilisateur.getAdresseMail());
            utilisateurDTO.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
            utilisateurDTO.setTelephone(utilisateur.getTelephone());
            utilisateurDTO.setDateEntree(utilisateur.getDateEntree());
            utilisateurDTO.setActif(utilisateur.isActif());
            utilisateurDTO.setPhoto(utilisateur.getPhoto());
            utilisateurDTO.setPassword(utilisateur.getPassword());
            utilisateurDTO.setAdresseIpTel(utilisateur.getAdresseIpTel());
            utilisateurDTO.setDatePassModifie(utilisateur.getDatePassModifie());
            utilisateurDTO.setDateFinContrat(utilisateur.getDateFinContrat());
            utilisateurDTO.setRole(utilisateur.getRole());
            // Remplir les informations sur le profil de l'utilisateurDTO
//            Profil profil = utilisateur.getProfil();
//            if (profil != null) {
//                utilisateurDTO.setProfilId(profil.getIdProfil());
//                utilisateurDTO.setProfilDesignation(profil.getDesignation());
//            }
//            // Remplir les informations sur le département de l'utilisateurDTO
//            Departements departements = utilisateur.getDepartements();
//            if (departements != null) {
//                utilisateurDTO.setDepartementId(departements.getIdDepartement());
//                utilisateurDTO.setDepartementDescription(departements.getDescription());
//            }
//            // Remplir les informations sur le contrat de l'utilisateurDTO
//            Contrats contrat = utilisateur.getContrats();
//            if (contrat != null) {
//                utilisateurDTO.setContratId(contrat.getIdContrat());
//                utilisateurDTO.setContratDesignation(contrat.getDesignation());
//            }
            demandesDTO.setUtilisateur_demande(utilisateurDTO);
        }

        // Convertir les informations sur l'utilisateur saisie de la demande
        Utilisateurs utilisateurSaisieDemande = demandes.getUtilisateur_saisi_demande();
        if (utilisateurSaisieDemande != null) {
            UtilisateursDTO utilisateurSaisieDemandeDTO = new UtilisateursDTO();
            utilisateurSaisieDemandeDTO.setIdUtilisateur(utilisateurSaisieDemande.getIdUtilisateur());
            utilisateurSaisieDemandeDTO.setMatricule(utilisateurSaisieDemande.getMatricule());
            utilisateurSaisieDemandeDTO.setNom(utilisateurSaisieDemande.getNom());
            utilisateurSaisieDemandeDTO.setPrenom(utilisateurSaisieDemande.getPrenom());
            utilisateurSaisieDemandeDTO.setAdresseMail(utilisateurSaisieDemande.getAdresseMail());
            utilisateurSaisieDemandeDTO.setPosteUtilisateur(utilisateurSaisieDemande.getPosteUtilisateur());
            utilisateurSaisieDemandeDTO.setTelephone(utilisateurSaisieDemande.getTelephone());
            utilisateurSaisieDemandeDTO.setDateEntree(utilisateurSaisieDemande.getDateEntree());
            utilisateurSaisieDemandeDTO.setActif(utilisateurSaisieDemande.isActif());
            utilisateurSaisieDemandeDTO.setPhoto(utilisateurSaisieDemande.getPhoto());
            utilisateurSaisieDemandeDTO.setPassword(utilisateurSaisieDemande.getPassword());
            utilisateurSaisieDemandeDTO.setAdresseIpTel(utilisateurSaisieDemande.getAdresseIpTel());
            utilisateurSaisieDemandeDTO.setDatePassModifie(utilisateurSaisieDemande.getDatePassModifie());
            utilisateurSaisieDemandeDTO.setDateFinContrat(utilisateurSaisieDemande.getDateFinContrat());
            utilisateurSaisieDemandeDTO.setRole(utilisateurSaisieDemande.getRole());
            // Remplir les autres détails du validateurDTO ici...
            demandesDTO.setUtilisateur_saisi_demande(utilisateurSaisieDemandeDTO);
        }

        return demandesDTO;
    }

    //Get dyal les demandes dyal les utilisateurs li ana validateur après had la liste kanfiltriha b etat_validation
    @GetMapping("/by-validateur-etat")
    public ResponseEntity<List<DemandesDTO>> getDemandesByValidateurAndEtat(
            @RequestParam Long validateurId,
            @RequestParam String etatValidation) {

        List<DemandesDTO> demandesDTOs = demandesService.getDemandesByValidateurAndEtat(validateurId, etatValidation);

        if (demandesDTOs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(demandesDTOs, HttpStatus.OK);
    }

    // Get Demandes par Profil
    @GetMapping("/demandesParProfil")
    public ResponseEntity<List<Demandes>> getDemandesParProfil(@RequestParam Long idProfil) {
        List<Demandes> demandes = demandesRepository.findByUtilisateurs_Profil_IdProfil(idProfil);
        if (demandes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(demandes);
    }

    //Calcul du duréé
//    @GetMapping("/calculerDuree")
//    public ResponseEntity<?> calculerDuree(@RequestParam Long idUser,
//                                           @RequestParam String dateDebut,
//                                           @RequestParam String dateFin) {
//        try {
//            Utilisateurs utilisateur = utilisateursRepository.findById(idUser)
//                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + idUser));
//
//            LocalDate startDate = LocalDate.parse(dateDebut);
//            LocalDate endDate = LocalDate.parse(dateFin);
//
//            // Récupérer les affectations de plan de travail pour l'utilisateur dans l'intervalle de dates spécifié
//            List<AffectationPlan> affectations = affectationPlanRepository.findByUtilisateursAndDatePlanBetween(utilisateur,
//                    Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
//                    Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//
//            // Récupérer les jours fériés depuis la base de données
//            List<LocalDate> joursFeries = getJoursFeries(startDate, endDate);
//
//            int totalMinutesTravailles = 0;
//            LocalDate dateReprise = null;
//
//            // Compter les jours travaillés dans l'intervalle
//            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//                boolean estJourTravaille = false;
//                boolean estJourFerie = joursFeries.contains(date);
//                if (estJourFerie) continue;
//
//                for (AffectationPlan affectation : affectations) {
//                    PlansTravail plansTravail = affectation.getPlansTravail();
//                    // Vérifier si la date est un jour travaillé
//                    if (estJourTravaille(plansTravail, date)) {
//                        estJourTravaille = true;
//
//                        // Récupérer les détails du plan journalier
//                        List<DetailPlansJournalier> detailsPlans = detailPlansJournalierRepository.findByIdPlansTravail(plansTravail.getIdPlansTravail());
//                        for (DetailPlansJournalier detail : detailsPlans) {
//                            LocalTime heureDebut = detail.getHeureDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
//                            LocalTime heureFin = detail.getHeureFin().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
//                            totalMinutesTravailles += Duration.between(heureDebut, heureFin).toMinutes();
//                        }
//
//                        break;
//                    }
//                }
//            }
//
//            // Calculer le solde restant
//            Compteur compteurs = compteursRepository.findByUtilisateurs_IdUtilisateur(idUser)
//                    .orElseThrow(() -> new ResourceNotFoundException("Compteurs non trouvé pour l'utilisateur avec l'ID : " + idUser));
//
//            float soldeRestant = compteurs.getSolde() - (totalMinutesTravailles / 60.0f / 8.0f); // supposer 8 heures par jour
//            String message = "Calcul de la durée réussi.";
//            if (soldeRestant < 0) {
//                soldeRestant = 0; // Assurer que le solde ne soit pas négatif
//                message = "L'utilisateur n'a pas assez de solde de congé.";
//            } else {
//                // Calculer la date de reprise
//                dateReprise = endDate.plusDays(1);
//                // Sauter les jours fériés et les jours non travaillés
//                while (joursFeries.contains(dateReprise) || !estJourTravaille(getPlanTravailUtilisateur(utilisateur), dateReprise)) {
//                    dateReprise = dateReprise.plusDays(1);
//                }
//            }
//
//            // Convertir les minutes totales travaillées en heures et minutes
//            int heuresTravailles = totalMinutesTravailles / 60;
//            int minutesTravailles = totalMinutesTravailles % 60;
//
//            // Créer l'objet JSON de retour
//            JSONObject responseObject = new JSONObject();
//            responseObject.put("joursTravailles", totalMinutesTravailles / (60 * 8)); // supposer 8 heures par jour
//            responseObject.put("duree", heuresTravailles + " heures et " + minutesTravailles + " minutes");
//            responseObject.put("soldeRestant", soldeRestant);
//            responseObject.put("dateReprise", dateReprise != null ? dateReprise.toString() : "N/A");
//            responseObject.put("message", message);
//
//            return ResponseEntity.ok(responseObject.toString());
//        } catch (DateTimeParseException | ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du calcul de la durée. Cause : " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur non prévue lors du calcul de la durée.");
//        }
//    }
//
//
//    private boolean estJourTravaille(PlansTravail plansTravail, LocalDate date) {
//        // Récupérer le jour de la semaine
//        DayOfWeek jourSemaine = date.getDayOfWeek();
//        int nbrJour = plansTravail.getNbrJour(); // Supposons que cette valeur est un masque de bits représentant les jours travaillés
//
//        // Mapping du jour de la semaine à une valeur binaire
//        int dayBit = 1 << (jourSemaine.getValue() - 1); // Lundi = 1, Mardi = 2, ..., Dimanche = 7
//
//        // Vérifier si le jour de la semaine est un jour travaillé en utilisant un masque de bits
//        return (nbrJour & dayBit) != 0;
//    }
//
//    private PlansTravail getPlanTravailUtilisateur(Utilisateurs utilisateur) {
//        return affectationPlanRepository.findFirstByUtilisateursOrderByDatePlanDesc(utilisateur)
//                .orElseThrow(() -> new ResourceNotFoundException("Plan de travail non trouvé pour l'utilisateur avec l'ID : " + utilisateur.getIdUtilisateur()))
//                .getPlansTravail();
//    }



//    private List<LocalDate> getJoursFeries(LocalDate startDate, LocalDate endDate) {
//        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        List<Fete> fetes = feteRepository.findByDateDebutBetween(start, end);
//
//        List<LocalDate> joursFeries = new ArrayList<>();
//        for (Fete fete : fetes) {
//            LocalDate dateDebut = fete.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            LocalDate dateFin = fete.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            for (LocalDate date = dateDebut; !date.isAfter(dateFin); date = date.plusDays(1)) {
//                joursFeries.add(date);
//            }
//        }
//        return joursFeries;
//    }



    //POST
    @PostMapping("/demandes")
    public ResponseEntity<?> addDemandes(@RequestBody Demandes demandes,
                                         @RequestParam Long idUser,
                                         @RequestParam Long idUserSaisiDemande) {
        try {
            Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));

            Utilisateurs userSaisiDemande = utilisateursRepository.findById(idUserSaisiDemande)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUserSaisiDemande));

            demandes.setUtilisateurs(utilisateurs);
            demandes.setUtilisateur_saisi_demande(userSaisiDemande);

            // Définir l'état de validation par défaut à "nouvelle"
            demandes.setEtatValidation("nouvelle");

            Demandes savedDemandes = demandesRepository.save(demandes);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1); // Succès
            responseObject.put("message", "Demande créée avec succès."); // Message de succès
            responseObject.put("data", savedDemandes); // Les informations de la demande créée
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de la création de la demande
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0); // Échec
            errorResponse.put("message", "Erreur lors de la création de la demande."); // Message d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }


//    @PostMapping("/users/{userId}/demandes")
//    public ResponseEntity<Demandes> createDemande(@PathVariable(value = "userId") Long userId,
//                                                  @RequestBody Demandes demandes) {
//        Demandes demandes1 = utilisateursRepository.findById(userId).map(user -> {
//            user.getDemandes().add(demandes);
//            return demandesRepository.save(demandes);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        return new ResponseEntity<>(demandes1, HttpStatus.CREATED);
//    }

    //PUT
    @PutMapping("/demandes/{id}")
    public ResponseEntity<Demandes> editDemandes(@PathVariable("id") Long id, @RequestBody Demandes demandes,
                                         @RequestParam Long idUser,
                                         @RequestParam Long idUserSaisiDemande){
        Utilisateurs utilisateurs = utilisateursRepository.findById(idUser)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUser));
        Utilisateurs userSaisiDemande = utilisateursRepository.findById(idUserSaisiDemande)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateurs non trouvé avec l'ID : " + idUserSaisiDemande));
        Demandes demandes1 = demandesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Validateur with id = " + id));
        demandes1.setUtilisateurs(utilisateurs);
        demandes1.setUtilisateur_saisi_demande(userSaisiDemande);
        demandes1.setDateSaisie(demandes.getDateSaisie());
        demandes1.setObjet(demandes.getObjet());
        demandes1.setDuree(demandes.getDuree());
        demandes1.setPlage(demandes.getPlage());
        demandes1.setEtatValidation(demandes.getEtatValidation());
        demandes1.setDateFinMission(demandes.getDateFinMission());
        demandes1.setDateReprise(demandes.getDateReprise());
        demandes1.setCodeMotif(demandes.getCodeMotif());
        demandes1.setDateDebutMission(demandes.getDateDebutMission());


        return new ResponseEntity<>(demandesRepository.save(demandes1), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/demandes/{id}")
    public ResponseEntity<HttpStatus> deleteDemandeById(@PathVariable("id") Long id) {
        demandesRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/demandes")
    public ResponseEntity<HttpStatus> deleteAllDemandes() {
        demandesRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/users/{userId}/demandes")
//    public ResponseEntity<List<Demandes>> deleteAllDemandesOfUtilisateur(@PathVariable(value = "userId") Long userId) {
//        Utilisateurs utilisateurs = utilisateursRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Utilisateur with id = " + userId));
//
//        utilisateurs.removeDemandes();
//        utilisateursRepository.save(utilisateurs);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
