package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DemandesDTO;
import com.ato.backendapi.dto.UtilisateursDTO;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
import com.ato.backendapi.services.DemandesService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private CompteurRepository compteurRepository;

    @Autowired
    private SousCodeTraitementRepository sousCodeTraitementRepository;

    @Autowired
    private ValidateurRepository validateurRepository;

    private static final Logger log = LoggerFactory.getLogger(DemandesController.class);

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
            Utilisateurs utilisateurSaisieDemande = demande.getUtilisateurSaisiDemande();
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
        Utilisateurs utilisateurSaisieDemande = demandes.getUtilisateurSaisiDemande();
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
//    @GetMapping("/demandesParProfil")
//    public ResponseEntity<List<Demandes>> getDemandesParProfil(@RequestParam Long idProfil) {
//        List<Demandes> demandes = demandesRepository.findByUtilisateurs_Profil_IdProfil(idProfil);
//        if (demandes.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(demandes);
//    }
    @GetMapping("/demandesParProfil")
    public ResponseEntity<List<DemandesDTO>> getDemandesParProfil(@RequestParam Long idProfil) {
        // Récupérer la liste des demandes associées au profil
        List<Demandes> demandes = demandesRepository.findByUtilisateurs_Profil_IdProfil(idProfil);

        if (demandes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Convertir la liste des demandes en DemandesDTO
        List<DemandesDTO> demandesDTOList = demandes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(demandesDTOList);
    }
    private DemandesDTO convertToDTO(Demandes demandes) {
        DemandesDTO demandesDTO = new DemandesDTO();

        demandesDTO.setIdDemande(demandes.getIdDemande());
        demandesDTO.setDateSaisie(demandes.getDateSaisie());
        demandesDTO.setCodeMotif(demandes.getCodeMotif());
        demandesDTO.setDateDebutMission(demandes.getDateDebutMission());
        demandesDTO.setDateFinMission(demandes.getDateFinMission());
        demandesDTO.setDuree(demandes.getDuree());
        demandesDTO.setObjet(demandes.getObjet());
        demandesDTO.setDateReprise(demandes.getDateReprise());
        demandesDTO.setPlage(demandes.getPlage());
        demandesDTO.setEtatValidation(demandes.getEtatValidation());

        // Mapper utilisateur_demande
        if (demandes.getUtilisateurs() != null) {
            demandesDTO.setUtilisateur_demande(convertUtilisateurToDTO(demandes.getUtilisateurs()));
        }

        // Mapper utilisateur_saisi_demande
        if (demandes.getUtilisateurSaisiDemande() != null) {
            demandesDTO.setUtilisateur_saisi_demande(convertUtilisateurToDTO(demandes.getUtilisateurSaisiDemande()));
        }

        return demandesDTO;
    }
    private UtilisateursDTO convertUtilisateurToDTO(Utilisateurs utilisateur) {
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

        // Ajouter les informations du profil, département, contrat et direction
        if (utilisateur.getProfil() != null) {
            utilisateurDTO.setProfilId(utilisateur.getProfil().getIdProfil());
            utilisateurDTO.setProfilDesignation(utilisateur.getProfil().getDesignation());
        }

        if (utilisateur.getDepartements() != null) {
            utilisateurDTO.setDepartementId(utilisateur.getDepartements().getIdDepartement());
            utilisateurDTO.setDepartementDescription(utilisateur.getDepartements().getDescription());
        }

        if (utilisateur.getContrats() != null) {
            utilisateurDTO.setContratId(utilisateur.getContrats().getIdContrat());
            utilisateurDTO.setContratDesignation(utilisateur.getContrats().getDesignation());
        }
        if (utilisateur.getDepartements().getDirection() != null) {
            utilisateurDTO.setDirectionId(utilisateur.getDepartements().getDirection().getIdDirection());
            utilisateurDTO.setDirectionDescription(utilisateur.getDepartements().getDirection().getDescription());
        }

        return utilisateurDTO;
    }


    // Calcul du durée
    // Méthode principale pour le calcul de la durée
    @GetMapping("/calculerDuree")
    public ResponseEntity<?> calculerDuree(@RequestParam Long idUser,
                                           @RequestParam(required = false) String dateDebut,
                                           @RequestParam(required = false) String dateFin,
                                           @RequestParam String typeScTraitement) {
        try {
            // Parse des dates avec format yyyy-MM-dd pour ne garder que la partie date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(dateDebut, formatter);
            LocalDateTime endDate = LocalDateTime.parse(dateFin, formatter);

            // Récupération de l'utilisateur et de son profil
            Utilisateurs utilisateur = utilisateursRepository.findById(idUser)
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
            Profil profil = utilisateur.getProfil();

            // Récupérer les sousCodesTraitement associés au profil
            List<SousCodeTraitement> sousCodeTraitements = profil.getSousCodeTraitement();
            SousCodeTraitement sousCodeTraitement = sousCodeTraitements.stream()
                    .filter(sct -> sct.getType().name().equalsIgnoreCase(typeScTraitement))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("SousCodeTraitement introuvable pour le type spécifié."));

            // Récupération des affectations de plans
            List<AffectationPlan> affectationPlans = affectationPlanRepository.findByUtilisateurs_IdUtilisateur(idUser);

            // Vérification si l'utilisateur a des affectations sur la plage de dates
            boolean hasAffectationOnInterval = affectationPlans.stream()
                    .anyMatch(ap -> {
                        // Convertir la datePlan de AffectationPlan en LocalDateTime
                        LocalDateTime planDateTime = ap.getDatePlan().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        // Comparer les dates
                        return (planDateTime.isEqual(startDate) || planDateTime.isAfter(startDate)) &&
                                (planDateTime.isEqual(endDate) || planDateTime.isBefore(endDate));
                    });

            if (!hasAffectationOnInterval) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aucune affectation trouvée pour l'utilisateur sur l'intervalle de dates spécifié.");
            }

            // Jours fériés
            List<Fete> joursFeries = feteRepository.findAll();

            // Initialisation des compteurs
            long joursTravailles = 0;
            long joursNonTravailles = 0;
            Duration heuresTravailles = Duration.ZERO;
            Duration heuresNonTravailles = Duration.ZERO;

            // Heure de reprise par défaut
            LocalDateTime dateReprise = null;

            // Parcours de chaque jour de l'intervalle
            for (LocalDate date = startDate.toLocalDate(); !date.isAfter(endDate.toLocalDate()); date = date.plusDays(1)) {
                boolean isTravaille = isJourTravaille(date, affectationPlans, joursFeries);

                if (isTravaille) {
                    joursTravailles++;
                } else {
                    joursNonTravailles++;
                }
            }

            // Traitement spécifique selon le type de traitement
            if ("ABSENCE_HORAIRES".equalsIgnoreCase(typeScTraitement)) {
                DetailPlansJournalier detailPlan = getDetailPlanJournalier(affectationPlans, startDate);
                if (detailPlan != null) {
                    LocalTime heureDebutPlan = detailPlan.getHeureDebut();
                    LocalTime heureFinPlan = detailPlan.getHeureFin();

                    LocalTime absenceDebut = startDate.toLocalDate().atStartOfDay().toLocalTime();
                    LocalTime absenceFin = endDate.toLocalDate().atStartOfDay().toLocalTime();

                    if (absenceDebut.isBefore(heureFinPlan) && absenceFin.isAfter(heureDebutPlan)) {
                        LocalTime debutEffectif = absenceDebut.isAfter(heureDebutPlan) ? absenceDebut : heureDebutPlan;
                        LocalTime finEffectif = absenceFin.isBefore(heureFinPlan) ? absenceFin : heureFinPlan;

                        Duration absenceEffective = Duration.between(debutEffectif, finEffectif);
                        heuresNonTravailles = absenceEffective;

                        Duration totalPlanDuration = Duration.between(heureDebutPlan, heureFinPlan);
                        heuresTravailles = totalPlanDuration.minus(absenceEffective);
                    } else {
                        heuresNonTravailles = Duration.between(absenceDebut, absenceFin);
                        heuresTravailles = Duration.ZERO;
                    }
                } else {
                    heuresNonTravailles = Duration.between(startDate.toLocalDate().atStartOfDay(), endDate.toLocalDate().atStartOfDay());
                    heuresTravailles = Duration.ZERO;
                }
                dateReprise = endDate.toLocalDate().atStartOfDay();
            } else if ("ABSENCE_PREDEFINIS".equalsIgnoreCase(typeScTraitement)) {
                long joursAbsence = sousCodeTraitement.getAbsence();
                joursNonTravailles = joursAbsence;

                LocalDate tempReprise = getProchaineDateValide(
                        joursAbsence > 0 ? startDate.toLocalDate().plusDays(joursAbsence) : endDate.toLocalDate().plusDays(1),
                        joursFeries
                );

                while (isWeekendOrFerie(tempReprise, joursFeries)) {
                    tempReprise = tempReprise.plusDays(1);
                }

                DetailPlansJournalier detailPlan = getDetailPlanJournalier(affectationPlans, startDate);
                if (detailPlan != null) {
                    dateReprise = LocalDateTime.of(tempReprise, detailPlan.getHeureDebut());
                } else {
                    dateReprise = LocalDateTime.of(tempReprise, LocalTime.of(9, 0));
                }
            } else if ("ABSENCE_ANNUEL".equalsIgnoreCase(typeScTraitement)) {
                Compteur compteur = compteurRepository.findOneByUtilisateurs_IdUtilisateur(idUser);
                if (compteur == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Compteur non trouvé pour l'utilisateur.");
                }

                float soldeConges = compteur.getSolde();
                long joursAbsence = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;

                if (joursAbsence > soldeConges) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Solde de congé insuffisant pour couvrir l'absence.");
                }

                joursNonTravailles = 0;
                for (LocalDate date = startDate.toLocalDate(); !date.isAfter(endDate.toLocalDate()); date = date.plusDays(1)) {
                    if (!isWeekendOrFerie(date, joursFeries)) {
                        joursNonTravailles++;
                    }
                }

                LocalDate tempReprise = endDate.toLocalDate().plusDays(1);
                while (isWeekendOrFerie(tempReprise, joursFeries)) {
                    tempReprise = tempReprise.plusDays(1);
                }

                DetailPlansJournalier detailPlan = getDetailPlanJournalier(affectationPlans, startDate);
                if (detailPlan != null) {
                    dateReprise = LocalDateTime.of(tempReprise, detailPlan.getHeureDebut());
                } else {
                    dateReprise = LocalDateTime.of(tempReprise, LocalTime.of(9, 0));
                }
            }



            // Construction de la réponse JSON
            JSONObject responseObject = new JSONObject();
            responseObject.put("joursTravailles", joursTravailles);
            responseObject.put("joursNonTravailles", joursNonTravailles);
            responseObject.put("heuresTravailles", heuresTravailles.toHours());
            responseObject.put("heuresNonTravailles", heuresNonTravailles.toHours());
            responseObject.put("dateRepriseTravail", dateReprise != null ? dateReprise.toString() : "Non déterminée");
            responseObject.put("message", "Calcul des jours travaillés, non travaillés et date de reprise réussi.");

            return ResponseEntity.ok(responseObject.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du calcul: " + e.getMessage());
        }
    }


    // Méthode utilitaire pour vérifier si le jour est travaillé, y compris les week-ends
    private boolean isJourTravaille(LocalDate date, List<AffectationPlan> affectationPlans, List<Fete> joursFeries) {
        boolean isFerie = isFerie(date, joursFeries);

        // Vérifier si l'utilisateur a une affectation de plan pour ce jour-là
        boolean isPlanTravaille = affectationPlans.stream()
                .flatMap(ap -> ap.getPlansTravail().getDetailPlansTravail().stream())
                .anyMatch(dpt -> dpt.getNomJour().equalsIgnoreCase(date.getDayOfWeek().name()) &&
                        dpt.getPlansJournalier().getDetailPlansJournaliers().stream()
                                .anyMatch(detail -> detail.getObjectif() != null && detail.getObjectif().isAfter(LocalTime.of(0, 0))));

        // Un jour est travaillé si l'utilisateur a un plan de travail valide ce jour-là
        return !isFerie && isPlanTravaille;
    }

    // Méthode pour vérifier si une date est un jour férié
    private boolean isFerie(LocalDate date, List<Fete> joursFeries) {
        return joursFeries.stream().anyMatch(fete ->
                !date.isBefore(fete.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) &&
                        !date.isAfter(fete.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
    }

    // Méthode pour obtenir la prochaine date valide en excluant les week-ends et jours fériés
    private LocalDate getProchaineDateValide(LocalDate date, List<Fete> joursFeries) {
        while (isWeekendOrFerie(date, joursFeries)) {
            date = date.plusDays(1);
        }
        return date;
    }

    // Méthode pour vérifier si la date est un week-end ou un jour férié
    private boolean isWeekendOrFerie(LocalDate date, List<Fete> joursFeries) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || isFerie(date, joursFeries);
    }

    // Méthode pour récupérer le détail du plan journalier pour une date donnée
    private DetailPlansJournalier getDetailPlanJournalier(List<AffectationPlan> affectationPlans, LocalDateTime date) {
        return affectationPlans.stream()
                .flatMap(ap -> ap.getPlansTravail().getDetailPlansTravail().stream())
                .filter(dpt -> dpt.getNomJour().equalsIgnoreCase(date.getDayOfWeek().name()))
                .flatMap(dpt -> dpt.getPlansJournalier().getDetailPlansJournaliers().stream())
                .filter(detail -> detail.getObjectif() != null)
                .findFirst()
                .orElse(null);
    }


//    @GetMapping("/calculerDuree")
//    public ResponseEntity<?> calculerDuree(@RequestParam Long idUser,
//                                           @RequestParam(required = false) String dateDebut,
//                                           @RequestParam(required = false) String dateFin,
//                                           @RequestParam String typeScTraitement) {
//        try {
//            // Vérification des paramètres obligatoires
//            if (idUser == null || typeScTraitement == null || typeScTraitement.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("L'ID utilisateur et le type de sous-code de traitement sont obligatoires.");
//            }
//
//            if (dateDebut == null || dateFin == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("Les dates de début et de fin sont obligatoires.");
//            }
//
//            LocalDateTime startDate;
//            LocalDateTime endDate;
//
//            // Conversion des dates
//            try {
//                startDate = LocalDateTime.parse(dateDebut, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
//                endDate = LocalDateTime.parse(dateFin, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
//            } catch (DateTimeParseException e) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("Le format des dates est invalide. Utilisez le format 'yyyy-MM-dd'T'HH:mm'.");
//            }
//
//            if (startDate.isAfter(endDate)) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("La date de début ne peut pas être après la date de fin.");
//            }
//
//            long nombreJoursDemandes = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()) + 1;
//
//            // Récupération de l'utilisateur
//            Utilisateurs utilisateur = utilisateursRepository.findById(idUser)
//                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé."));
//
//            // Vérification du compteur associé
//            Compteur compteur = compteurRepository.findFirstByUtilisateurs_IdUtilisateur(utilisateur.getIdUtilisateur())
//                    .orElseThrow(() -> new ResourceNotFoundException("Compteur non trouvé pour l'utilisateur."));
//
//            // Vérification du solde pour les absences annuelles
//            if ("ABSENCE_ANNUEL".equals(typeScTraitement)) {
//                if (compteur.getSolde() < nombreJoursDemandes) {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                            .body("Solde insuffisant pour l'absence annuelle. Solde actuel : " + compteur.getSolde() +
//                                    ", Jours demandés : " + nombreJoursDemandes);
//                }
//            }
//
//            // Vérification du plan de travail assigné
//            PlansTravail plansTravail = getPlanTravailUtilisateur(utilisateur);
//            if (plansTravail == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("L'utilisateur avec l'ID " + idUser + " n'a pas de plan de travail assigné.");
//            }
//
//            // Vérification des affectations
//            if (!verifierAffectations(utilisateur.getIdUtilisateur(), startDate, endDate)) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body("L'utilisateur n'a pas les affectations nécessaires pour la période demandée.");
//            }
//
//            // Calcul des jours fériés
//            List<LocalDate> joursFeries = getJoursFeries(startDate.toLocalDate(), endDate.toLocalDate());
//
//            int joursTravailles = 0;
//            int joursNonTravailles = 0;
//
//            for (LocalDate date = startDate.toLocalDate(); !date.isAfter(endDate.toLocalDate()); date = date.plusDays(1)) {
//                boolean estJourTravaille = estJourTravaille(plansTravail, date);
//                boolean estFerie = joursFeries.contains(date);
//
//                log.info("Date : {}, Est travaillé : {}, Est férié : {}", date, estJourTravaille, estFerie);
//
//                if (estJourTravaille && !estFerie) {
//                    joursTravailles++;
//                } else {
//                    joursNonTravailles++;
//                }
//            }
//
//            LocalDateTime dateReprise = calculerDateReprise(startDate, endDate, plansTravail, typeScTraitement, joursFeries);
//
//            JSONObject responseObject = new JSONObject();
//            responseObject.put("joursTravailles", joursTravailles);
//            responseObject.put("joursNonTravailles", joursNonTravailles);
//            responseObject.put("dateRepriseTravail", dateReprise.toString());
//            responseObject.put("message", "Calcul des jours travaillés, non travaillés et date de reprise réussi.");
//
//            return ResponseEntity.ok(responseObject.toString());
//
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            log.error("Erreur lors du calcul de durée : {}", e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erreur non prévue lors du calcul.");
//        }
//    }
//
//    // Méthode pour vérifier les affectations d'un utilisateur
//    public boolean verifierAffectations(Long idUser, LocalDateTime startDate, LocalDateTime endDate) {
//        // Vérifier les paramètres
//        if (idUser == null || startDate == null || endDate == null) {
//            throw new IllegalArgumentException("L'ID utilisateur, la date de début et la date de fin sont obligatoires.");
//        }
//
//        // Convertir LocalDateTime en Date pour la méthode de repository
//        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
//        Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());
//
//        // Rechercher les affectations dans la période donnée
//        List<AffectationPlan> affectations = affectationPlanRepository.findByUtilisateurs_IdUserAndDatePlanBetween(idUser, start, end);
//
//        // Retourner true si des affectations sont trouvées, sinon false
//        return !affectations.isEmpty();
//    }
//
//
//    // Méthode pour calculer la date de reprise en fonction du type de traitement
//    private LocalDateTime calculerDateReprise(LocalDateTime startDate, LocalDateTime endDate, PlansTravail plansTravail,
//                                              String typeScTraitement, List<LocalDate> joursFeries) {
//        LocalDateTime dateReprise = endDate;
//
//        switch (typeScTraitement) {
//            case "ABSENCE_HORAIRES":
//                dateReprise = calculerDateRepriseHoraires(startDate, endDate, plansTravail, joursFeries);
//                break;
//
//            case "ABSENCE_ANNUEL":
//                dateReprise = calculerDateRepriseAnnuel(endDate, plansTravail, joursFeries);
//                break;
//
//            case "ABSENCE_PREDEFINIS":
//                dateReprise = calculerDateReprisePredefinis(startDate, endDate);
//                break;
//
//            default:
//                break;
//        }
//
//        return dateReprise;
//    }
//
//    // Calcul de la date reprise pour une absence horaires (type ABSENCE_HORAIRES)
//    private LocalDateTime calculerDateRepriseHoraires(LocalDateTime startDate, LocalDateTime endDate,
//                                                      PlansTravail plansTravail, List<LocalDate> joursFeries) {
//        LocalDateTime dateReprise = endDate;
//        boolean isAfterWorkingHours = false;
//        LocalTime heureDebutReprise = null;
//
//        for (DetailPlansTravail detail : plansTravail.getDetailPlansTravail()) {
//            PlansJournalier plansJournalier = detail.getPlansJournalier();
//            for (DetailPlansJournalier detailPlanJour : plansJournalier.getDetailPlansJournaliers()) {
//                if (endDate.toLocalTime().isAfter(detailPlanJour.getHeureFin())) {
//                    isAfterWorkingHours = true;
//                    break;
//                } else {
//                    heureDebutReprise = detailPlanJour.getHeureDebut();
//                }
//            }
//            if (isAfterWorkingHours) break;
//        }
//
//        if (heureDebutReprise == null) {
//            heureDebutReprise = plansTravail.getDetailPlansTravail().get(0).getPlansJournalier()
//                    .getDetailPlansJournaliers().get(0).getHeureDebut();
//        }
//
//        if (isAfterWorkingHours) {
//            dateReprise = dateReprise.plusDays(1).withHour(heureDebutReprise.getHour()).withMinute(heureDebutReprise.getMinute());
//        } else {
//            dateReprise = dateReprise.withHour(heureDebutReprise.getHour()).withMinute(heureDebutReprise.getMinute());
//        }
//
//        while (!estJourTravaille(plansTravail, dateReprise.toLocalDate()) || joursFeries.contains(dateReprise.toLocalDate())) {
//            log.info("Date non travaillée ou fériée : {}", dateReprise.toLocalDate());
//            dateReprise = dateReprise.plusDays(1).withHour(heureDebutReprise.getHour()).withMinute(heureDebutReprise.getMinute());
//        }
//
//        log.info("Date de reprise finale : {}", dateReprise);
//        return dateReprise;
//    }
//
//
//    // Calcul de la date reprise pour un congé annuel (type ABSENCE_ANNUEL)
//    private LocalDateTime calculerDateRepriseAnnuel(LocalDateTime endDate, PlansTravail plansTravail, List<LocalDate> joursFeries) {
//        LocalDateTime dateReprise = endDate.plusDays(1).withHour(9).withMinute(0);
//        int maxAttempts = 100;  // Limiter le nombre d'itérations pour éviter une boucle infinie
//        int attempts = 0;
//
//        while ((!estJourTravaille(plansTravail, dateReprise.toLocalDate()) || joursFeries.contains(dateReprise.toLocalDate())) && attempts < maxAttempts) {
//            log.info("Date non travaillée ou fériée : {}", dateReprise.toLocalDate());
//            dateReprise = dateReprise.plusDays(1).withHour(9).withMinute(0);
//            attempts++;
//        }
//
//        if (attempts >= maxAttempts) {
//            log.error("Boucle infinie détectée lors du calcul de la date de reprise.");
//            throw new IllegalStateException("La date de reprise ne peut pas être calculée après un trop grand nombre de tentatives.");
//        }
//
//        log.info("Date de reprise finale : {}", dateReprise);
//        return dateReprise;
//    }
//
//
//
//    // Calcul de la date reprise pour les absences prédéfinies (type ABSENCE_PREDEFINIS)
//    private LocalDateTime calculerDateReprisePredefinis(LocalDateTime startDate, LocalDateTime endDate) {
//        if (startDate.toLocalTime().isBefore(LocalTime.NOON)) {
//            return startDate.withHour(14).withMinute(0); // Reprise à 14h00 le même jour
//        } else {
//            return startDate.plusDays(1).withHour(9).withMinute(0); // Reprise à 9h00 le lendemain
//        }
//    }
//
//    // Méthode pour vérifier si un jour est travaillé en fonction du plan
//    private boolean estJourTravaille(PlansTravail plansTravail, LocalDate date) {
//        DayOfWeek jourSemaine = date.getDayOfWeek();
//        int nbrJour = plansTravail.getNbrJour();
//        int dayBit = 1 << (jourSemaine.getValue() - 1); // Lundi = 1, Mardi = 2, ..., Dimanche = 7
//        return (nbrJour & dayBit) != 0;
//    }
//
//    // Méthode pour obtenir le plan de travail d'un utilisateur
//        private PlansTravail getPlanTravailUtilisateur(Utilisateurs utilisateur) {
//            return affectationPlanRepository.findFirstByUtilisateursOrderByDatePlanDesc(utilisateur)
//                    .map(AffectationPlan::getPlansTravail)
//                    .orElse(null);
//        }
//
//        // Méthode pour obtenir la liste des jours fériés dans une période
//        private List<LocalDate> getJoursFeries(LocalDate startDate, LocalDate endDate) {
//            List<LocalDate> joursFeries = new ArrayList<>();
//            List<Fete> fetes = feteRepository.findByDateDebutBetween(
//                    Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
//                    Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
//            );
//
//            for (Fete fete : fetes) {
//                LocalDate dateDebut = fete.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                LocalDate dateFin = fete.getDateFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                for (LocalDate date = dateDebut; !date.isAfter(dateFin); date = date.plusDays(1)) {
//                    joursFeries.add(date);
//                }
//            }
//            return joursFeries;
//        }



    //Get dyal les demandes par idUtilisateur , katchéquer profil li 3and dak
    //utilisateur ila kan profil=admin ghadi ijib kolchi
    //Ila kan manager li hiya le champs Validateur f la table Validateur ghadi tjib les demandes li howa validateur
    @GetMapping("/demandesByUser/{utilisateurId}")
    public ResponseEntity<?> getDemandesByUtilisateur(@PathVariable Long utilisateurId) {
        try {
            Optional<Utilisateurs> utilisateurOpt = utilisateursRepository.findById(utilisateurId);

            if (utilisateurOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
            }

            Utilisateurs utilisateur = utilisateurOpt.get();
            List<Validateur> validateurs = validateurRepository.findByUtilisateurs(utilisateur);
            List<DemandesDTO> demandesDTOs;

            if (!validateurs.isEmpty()) {
                List<Demandes> demandesParValidateur = demandesRepository.findByUtilisateurs(utilisateur);
                demandesDTOs = demandesParValidateur.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            } else {
                List<Demandes> demandesParUtilisateur = demandesRepository.findByUtilisateurSaisiDemande(utilisateur);
                demandesDTOs = demandesParUtilisateur.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }

            return ResponseEntity.ok(demandesDTOs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération des demandes.");
        }
    }


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
            demandes.setUtilisateurSaisiDemande(userSaisiDemande);

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
        demandes1.setUtilisateurSaisiDemande(userSaisiDemande);
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
