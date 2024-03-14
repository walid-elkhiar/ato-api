package com.ato.backendapi.controllers;

import com.ato.backendapi.dto.DepartementDTO;
import com.ato.backendapi.dto.SignInResponseDTO;
import com.ato.backendapi.dto.UtilisateursDTO;
import com.ato.backendapi.emailService.EmailService;
import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
import com.ato.backendapi.security.dto.SignInRequest;
import com.ato.backendapi.security.service.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1")
public class UtilisateursController {

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    @Autowired
    private ContratsRepository contratsRepository;

    @Autowired
    private DepartementsRepository departementsRepository;
    @Autowired
    private CompteurRepository compteurRepository;

    @Autowired
    private ProfilRepository profilRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JWTUtils jwtUtils;

//    @Autowired
//    private JavaMailSender emailSender;


    private String generateRandomPassword() {
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Exclut les caractères spéciaux
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }


//    private void sendPasswordEmail(String to, String password) throws MessagingException {
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(to);
//        helper.setSubject("Votre mot de passe");
//        helper.setText("Votre mot de passe est : " + password);
//        emailSender.send(message);
//    }

    //GET
//    @GetMapping("/AllUsers")
//    public ResponseEntity<List<Utilisateurs>> listeUsers(){
//        List<Utilisateurs> utilisateurs = new ArrayList<Utilisateurs>();
//
//        utilisateursRepository.findAll().forEach(utilisateurs::add);
//        if (utilisateurs.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
//    }



//    @GetMapping("/AllUsers")
////        List<Utilisateurs> utilisateurs = utilisateursRepository.findAll();
//        public ResponseEntity<List<UtilisateursDTO>> listeUsers(
//                @RequestParam(required = false) Long idContrat,
//                @RequestParam(required = false) Long idDepartement,
//                @RequestParam(required = false) Long idProfil) {
//
//            // Filtrer les utilisateurs en fonction des IDs de contrat, de département et de profil
//            List<Utilisateurs> utilisateurs = utilisateursRepository.findAllByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil(
//                    idContrat, idDepartement, idProfil);
//
//            if (utilisateurs.isEmpty() || utilisateurs == null) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//
//        List<UtilisateursDTO> utilisateursDTOs = new ArrayList<>();
//        for (Utilisateurs utilisateur : utilisateurs) {
//            UtilisateursDTO utilisateurDTO = new UtilisateursDTO();
//            utilisateurDTO.setIdUtilisateur(utilisateur.getIdUtilisateur());
//            utilisateurDTO.setMatricule(utilisateur.getMatricule());
//            utilisateurDTO.setNom(utilisateur.getNom());
//            utilisateurDTO.setPrenom(utilisateur.getPrenom());
//            utilisateurDTO.setAdresseMail(utilisateur.getAdresseMail());
//            utilisateurDTO.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
//            utilisateurDTO.setTelephone(utilisateur.getTelephone());
//            utilisateurDTO.setDateEntree(utilisateur.getDateEntree());
//            utilisateurDTO.setActif(utilisateur.getActif());
//            utilisateurDTO.setPhoto(utilisateur.getPhoto());
//            utilisateurDTO.setPassword(utilisateur.getPassword());
//            utilisateurDTO.setAdresseIpTel(utilisateur.getAdresseIpTel());
//            utilisateurDTO.setDatePassModifie(utilisateur.getDatePassModifie());
//            utilisateurDTO.setDateEntree(utilisateur.getDateEntree());
//            utilisateurDTO.setDateFinContrat(utilisateur.getDateFinContrat());
//
//
//            Profil profil = utilisateur.getProfil();
//            if (profil != null) {
//                utilisateurDTO.setProfilId(profil.getIdProfil());
//                utilisateurDTO.setProfilDesignation(profil.getDesignation());
//            }
//
//
//            Departements departements = utilisateur.getDepartements();
//            if (departements != null) {
//                utilisateurDTO.setDepartementId(departements.getIdDepartement());
//                utilisateurDTO.setDepartementDescription(departements.getDescription());
//
//            }
//
//            Contrats contrat = utilisateur.getContrats();
//            if (contrat != null) {
//                utilisateurDTO.setContratId(contrat.getIdContrat());
//                utilisateurDTO.setContratDesignation(contrat.getDesignation());
//            }
//
//            utilisateursDTOs.add(utilisateurDTO);
//        }
//
//        return new ResponseEntity<>(utilisateursDTOs, HttpStatus.OK);
//    }

    @GetMapping("/AllUsers")
    public ResponseEntity<List<UtilisateursDTO>> listeUsers(
            @RequestParam(required = false) Long idContrat,
            @RequestParam(required = false) Long idDepartement,
            @RequestParam(required = false) Long idProfil,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) Integer pageSize) {

        List<Utilisateurs> utilisateurs;

        if (offset != null && pageSize != null) {
            Pageable pageable = PageRequest.of(offset, pageSize);
            if (idContrat != null && idDepartement != null && idProfil != null) {
                utilisateurs = utilisateursRepository.findAllByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil(
                        idDepartement, idContrat, idProfil, pageable).getContent();
            } else if (idContrat != null && idDepartement != null) {
                utilisateurs = utilisateursRepository.findAllByDepartements_IdDepartementAndContrats_IdContrat(
                        idDepartement, idContrat, pageable).getContent();
            } else if (idContrat != null && idProfil != null) {
                utilisateurs = utilisateursRepository.findAllByContrats_IdContratAndProfil_IdProfil(
                        idContrat, idProfil, pageable).getContent();
            } else if (idDepartement != null && idProfil != null) {
                utilisateurs = utilisateursRepository.findAllByDepartements_IdDepartementAndProfil_IdProfil(
                        idDepartement, idProfil, pageable).getContent();
            } else if (idContrat != null) {
                utilisateurs = utilisateursRepository.findPaginatedByContrats_IdContrat(idContrat, pageable).getContent();
            } else if (idDepartement != null) {
                utilisateurs = utilisateursRepository.findAllByDepartements_IdDepartement(idDepartement, pageable).getContent();
            } else if (idProfil != null) {
                utilisateurs = utilisateursRepository.findAllByProfil_IdProfil(idProfil, pageable).getContent();
            } else {
                utilisateurs = utilisateursRepository.findAll(pageable).getContent();
            }
        } else {
            if (idContrat != null && idDepartement != null && idProfil != null) {
                utilisateurs = utilisateursRepository.findListByDepartements_IdDepartementAndContrats_IdContratAndProfil_IdProfil(
                        idDepartement, idContrat, idProfil);
            } else if (idContrat != null && idDepartement != null) {
                utilisateurs = utilisateursRepository.findListByDepartements_IdDepartementAndContrats_IdContrat(
                        idDepartement, idContrat);
            } else if (idContrat != null && idProfil != null) {
                utilisateurs = utilisateursRepository.findListByContrats_IdContratAndProfil_IdProfil(
                        idContrat, idProfil);
            } else if (idDepartement != null && idProfil != null) {
                utilisateurs = utilisateursRepository.findListByDepartements_IdDepartementAndProfil_IdProfil(
                        idDepartement, idProfil);
            } else if (idContrat != null) {
                utilisateurs = utilisateursRepository.findListByContrats_IdContrat(idContrat);
            } else if (idDepartement != null) {
                utilisateurs = utilisateursRepository.findByDepartements_IdDepartement(idDepartement);
            } else if (idProfil != null) {
                utilisateurs = utilisateursRepository.findAllByProfil_IdProfil(idProfil);
            } else {
                utilisateurs = utilisateursRepository.findAll();
            }
        }

        if (utilisateurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Convertir les utilisateurs en utilisateursDTO
        List<UtilisateursDTO> utilisateursDTOList = utilisateurs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(utilisateursDTOList, HttpStatus.OK);
    }

    private UtilisateursDTO convertToDTO(Utilisateurs utilisateur) {
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

        // Remplir les informations sur le profil
        Profil profil = utilisateur.getProfil();
        if (profil != null) {
            utilisateurDTO.setProfilId(profil.getIdProfil());
            utilisateurDTO.setProfilDesignation(profil.getDesignation());
        }

        // Remplir les informations sur le département
        Departements departements = utilisateur.getDepartements();
        if (departements != null) {
            utilisateurDTO.setDepartementId(departements.getIdDepartement());
            utilisateurDTO.setDepartementDescription(departements.getDescription());

            // Remplir les informations sur la direction
            Direction direction = departements.getDirection();
            if (direction != null) {
                utilisateurDTO.setDirectionId(direction.getIdDirection());
                utilisateurDTO.setDirectionDescription(direction.getDescription());
            }
        }

        // Remplir les informations sur le contrat
        Contrats contrat = utilisateur.getContrats();
        if (contrat != null) {
            utilisateurDTO.setContratId(contrat.getIdContrat());
            utilisateurDTO.setContratDesignation(contrat.getDesignation());
        }

        return utilisateurDTO;
    }


    private List<UtilisateursDTO> convertToListDTO(List<Utilisateurs> utilisateurs) {
        List<UtilisateursDTO> utilisateursDTOs = new ArrayList<>();
        for (Utilisateurs utilisateur : utilisateurs) {
            UtilisateursDTO utilisateurDTO = new UtilisateursDTO();
            // Remplir les données de l'utilisateur DTO à partir des données de l'utilisateur
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

            // Remplir les informations sur le profil
            Profil profil = utilisateur.getProfil();
            if (profil != null) {
                utilisateurDTO.setProfilId(profil.getIdProfil());
                utilisateurDTO.setProfilDesignation(profil.getDesignation());
            }

            // Remplir les informations sur le département
            Departements departements = utilisateur.getDepartements();
            if (departements != null) {
                utilisateurDTO.setDepartementId(departements.getIdDepartement());
                utilisateurDTO.setDepartementDescription(departements.getDescription());
            }

            // Remplir les informations sur le contrat
            Contrats contrat = utilisateur.getContrats();
            if (contrat != null) {
                utilisateurDTO.setContratId(contrat.getIdContrat());
                utilisateurDTO.setContratDesignation(contrat.getDesignation());
            }

            utilisateursDTOs.add(utilisateurDTO);
        }
        return utilisateursDTOs;
    }

//    @GetMapping("/users/{contratsId}/AllUsers")
//    public ResponseEntity<List<Utilisateurs>> getAllUsersByContratsId(@PathVariable(value = "contratsId") Long contratsId) {
//        Contrats contrats = contratsRepository.findById(contratsId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Contrat with id = " + contratsId));
//
//        List<Utilisateurs> utilisateurs = new ArrayList<>();
//        utilisateurs.addAll(contrats.getUtilisateurs());
//
//        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
//    }

//@GetMapping("/users/{departementId}/AllUsers")
//    public ResponseEntity<List<Utilisateurs>> getAllUsersByDepartementId(@PathVariable(value = "departementId") Long departementId) {
//        Departements departements = departementsRepository.findById(departementId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Departement with id = " + departementId));
//
//        List<Utilisateurs> utilisateurs = new ArrayList<>();
//        utilisateurs.addAll(departements.getUtilisateurs());
//
//        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
//    }

    @GetMapping("/{idDepartement}/AllUsers")
    public ResponseEntity<List<UtilisateursDTO>> getUsersByDepartement(@PathVariable Long idDepartement) {
        List<Utilisateurs> utilisateurs = utilisateursRepository.findByDepartements_IdDepartement(idDepartement);

        if (utilisateurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<UtilisateursDTO> utilisateursDTOs = convertToListDTO(utilisateurs);
        return new ResponseEntity<>(utilisateursDTOs, HttpStatus.OK);
    }

    @GetMapping("/{idUtilisateur}/departements")
    public ResponseEntity<List<DepartementDTO>> getAllDepartementsByUtilisateurIdWithCountUsers(@PathVariable Long idUtilisateur) {
        Optional<Utilisateurs> utilisateurOptional = utilisateursRepository.findById(idUtilisateur);

        if (utilisateurOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Utilisateurs utilisateur = utilisateurOptional.get();
        Departements departements = utilisateur.getDepartements();

        if (departements == null) {
            return ResponseEntity.noContent().build();
        }

        // Charger complètement les utilisateurs pour éviter les problèmes de sérialisation
        Hibernate.initialize(departements.getUtilisateurs());

        // Mapper le département vers le DTO avec le nombre d'utilisateurs
        DepartementDTO departementDTO = new DepartementDTO();
        departementDTO.setIdDepartement(departements.getIdDepartement());
        departementDTO.setDescription(departements.getDescription());
        departementDTO.setUserCount(departements.getUtilisateurs() != null ? departements.getUtilisateurs().size() : 0);

        return ResponseEntity.ok(Collections.singletonList(departementDTO));
    }

    @GetMapping("/utilisateurs/{idProfil}")
    public ResponseEntity<List<UtilisateursDTO>> getUtilisateursByProfil(@PathVariable Long idProfil) {
        List<Utilisateurs> utilisateurs = utilisateursRepository.findAllByProfil_IdProfil(idProfil);

        // Convertir la liste des entités Utilisateurs en DTO
        List<UtilisateursDTO> utilisateursDTOs = convertToListDTO(utilisateurs);

        return ResponseEntity.ok(utilisateursDTOs);
    }


//    @GetMapping("/{idDepartement}/nombre-utilisateurs")
//    public ResponseEntity<Long> getNombreUtilisateursForDepartement(@PathVariable Long idDepartement) {
//        Long nombreUtilisateurs = utilisateursRepository.countByDepartements_IdDepartement(idDepartement);
//
//        return new ResponseEntity<>(nombreUtilisateurs, HttpStatus.OK);
//    }


//    @GetMapping("/users/{profilId}/AllUsers")
//    public ResponseEntity<List<Utilisateurs>> getAllUsersByProfilId(@PathVariable(value = "profilId") Long profilId) {
//        Profil profil = profilRepository.findById(profilId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));
//
//        List<Utilisateurs> utilisateurs = new ArrayList<>();
//        utilisateurs.addAll(profil.getUtilisateurs());
//
//        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
//    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UtilisateursDTO> getUserById(@PathVariable("id") Long id) {
        Utilisateurs utilisateur = utilisateursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + id));

        if (utilisateur == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

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
        utilisateurDTO.setPassword(utilisateur.getPassword());
        utilisateurDTO.setAdresseIpTel(utilisateur.getAdresseIpTel());
        utilisateurDTO.setDatePassModifie(utilisateur.getDatePassModifie());
        utilisateurDTO.setDateEntree(utilisateur.getDateEntree());
        utilisateurDTO.setDateFinContrat(utilisateur.getDateFinContrat());

        // Construire l'URL complète de la photo
        if (utilisateur.getPhoto() != null && !utilisateur.getPhoto().isEmpty()) {
            String photoUrl = utilisateur.getPhoto();
            if (!photoUrl.startsWith("http://") && !photoUrl.startsWith("https://")) {
                photoUrl = "http://139.99.193.34:8081/images/" + photoUrl;
            }
            utilisateurDTO.setPhoto(photoUrl);
        } else {
            utilisateurDTO.setPhoto(null);
        }

        // Ajoutez les informations sur le profil
        Profil profil = utilisateur.getProfil();
        if (profil != null) {
            utilisateurDTO.setProfilId(profil.getIdProfil());
            utilisateurDTO.setProfilDesignation(profil.getDesignation());
        }

        // Ajoutez les informations sur le département
        Departements departements = utilisateur.getDepartements();
        if (departements != null) {
            utilisateurDTO.setDepartementId(departements.getIdDepartement());
            utilisateurDTO.setDepartementDescription(departements.getDescription());
        }

        // Ajoutez les informations sur le contrat
        Contrats contrat = utilisateur.getContrats();
        if (contrat != null) {
            utilisateurDTO.setContratId(contrat.getIdContrat());
            utilisateurDTO.setContratDesignation(contrat.getDesignation());
        }

        return new ResponseEntity<>(utilisateurDTO, HttpStatus.OK);
    }



    @GetMapping("/{idUser}/sousCodeTraitements")
    public ResponseEntity<?> getSousCodeTraitementsByUtilisateurId(@PathVariable Long idUser) {
        try {
            // Récupérer l'utilisateur via son ID
            Utilisateurs utilisateur = utilisateursRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + idUser));

            // Récupérer le profil de l'utilisateur
            Profil profil = utilisateur.getProfil();

            // Récupérer la liste des sousCodesTraitements associés à ce profil
            List<SousCodeTraitement> sousCodeTraitements = profil.getSousCodeTraitement();

            // Retourner la liste des sousCodesTraitements
            return ResponseEntity.ok(sousCodeTraitements);

        } catch (ResourceNotFoundException e) {
            // En cas d'utilisateur non trouvé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé avec l'ID : " + idUser);
        } catch (Exception e) {
            // En cas d'erreur générale
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération des sousCodeTraitements.");
        }
    }
    //POST
    @PostMapping("/users")
    public ResponseEntity<?> addUtilisateur(
            @ModelAttribute Utilisateurs utilisateurs,
            @RequestParam(required = false) Long idDepartement,
            @RequestParam(required = false) Long idContrats,
            @RequestParam(required = false) Long idProfil,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // Vérifier si les IDs sont vides
        if (idDepartement == null || idContrats == null || idProfil == null) {
            return ResponseEntity.badRequest().body("Les IDs des départements, contrats et profils sont obligatoires.");
        }

        // Rechercher le département, le contrat et le profil
        Departements departement = departementsRepository.findById(idDepartement)
                .orElseThrow(() -> new ResourceNotFoundException("Departement non trouvé avec l'ID : " + idDepartement));
        Contrats contrats = contratsRepository.findById(idContrats)
                .orElseThrow(() -> new ResourceNotFoundException("Contrats non trouvé avec l'ID : " + idContrats));
        Profil profil = profilRepository.findById(idProfil)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'ID : " + idProfil));

        // Vérifier et définir le rôle par défaut à "USER" si non spécifié
        if (utilisateurs.getRole() == null || utilisateurs.getRole().trim().isEmpty()) {
            utilisateurs.setRole("USER");
        }

        // Génération automatique du mot de passe
        String generatedPassword = generateRandomPassword();
        System.out.println("Mot de passe généré : " + generatedPassword);

        // Cryptage du mot de passe
        String encryptedPassword = passwordEncoder.encode(generatedPassword);
        utilisateurs.setPassword(encryptedPassword);

        // Gestion de la photo
        if (file != null && !file.isEmpty()) {
            String uploadDir = "C:/photos-back/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            try {
                Files.createDirectories(filePath.getParent());
                file.transferTo(filePath.toFile());
                String photoUrl = "http://139.99.193.34:8081/images/" + fileName;
                utilisateurs.setPhoto(photoUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
            }
        }

        // Définir les relations
        utilisateurs.setDepartements(departement);
        utilisateurs.setContrats(contrats);
        utilisateurs.setProfil(profil);

        try {
            // Enregistrement de l'utilisateur dans la base de données
            Utilisateurs savedUser = utilisateursRepository.save(utilisateurs);

            // Création et enregistrement du compteur associé
            Compteur compteur = new Compteur();
            compteur.setUtilisateurs(savedUser);
            compteur.setDroitAnnuel(0);
            compteur.setDroit(0);
            compteur.setPris(0);
            compteur.setSolde(0);
            compteurRepository.save(compteur);

            // Envoi d'un e-mail à l'utilisateur avec son mot de passe
            emailService.sendPasswordEmail(utilisateurs.getAdresseMail(), generatedPassword);

            // Créer l'objet JSON de retour pour succès
            JSONObject responseObject = new JSONObject();
            responseObject.put("success", 1);
            responseObject.put("message", "Utilisateur et compteur créés avec succès.");
            responseObject.put("data", savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseObject.toString());
        } catch (Exception e) {
            // En cas d'erreur lors de l'insertion
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("success", 0);
            errorResponse.put("message", "Erreur lors de la création de l'utilisateur.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toString());
        }
    }





    //Authentification
//    @PostMapping("/signIn")
//    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequest signInRequest) {
//        // Vérifier si l'adresse mail et le matricule sont présents
//        if (signInRequest.getAdresseMail() == null || signInRequest.getMatricule() == null) {
//            // Si l'un des champs est manquant, retourner un message d'erreur
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'adresse mail et le matricule sont obligatoires.");
//        }
//
//        // Rechercher l'utilisateur en fonction de l'adresse mail et du matricule
//        Utilisateurs utilisateur = utilisateursRepository.findByAdresseMailAndMatricule(signInRequest.getAdresseMail(), signInRequest.getMatricule());
//
//        // Vérifier si l'utilisateur existe et si l'adresse mail et le matricule correspondent
//        if (utilisateur != null) {
//            // Générer un token JWT pour l'utilisateur
//            String token = jwtUtils.generateToken(utilisateur);
//            // Définir l'expirationTime à une semaine (7 jours) à partir de maintenant
//            Instant expirationTime = Instant.now().plus(Duration.ofDays(7));
//
//            // Créer un objet SignInResponseDTO contenant le token, l'expirationTime et les informations de l'utilisateur
//            SignInResponseDTO responseBody = new SignInResponseDTO();
//            responseBody.setToken(token);
//            responseBody.setExpirationTime(expirationTime.toString());
//            responseBody.setIdUtilisateur(utilisateur.getIdUtilisateur());
//            responseBody.setMatricule(utilisateur.getMatricule());
//            responseBody.setNom(utilisateur.getNom());
//            responseBody.setPrenom(utilisateur.getPrenom());
//            responseBody.setAdresseMail(utilisateur.getAdresseMail());
//            responseBody.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
//            responseBody.setTelephone(utilisateur.getTelephone());
//            responseBody.setDateEntree(utilisateur.getDateEntree());
//            responseBody.setActif(utilisateur.isActif());
//            responseBody.setPhoto(utilisateur.getPhoto());
//            responseBody.setPassword(utilisateur.getPassword());
//            responseBody.setAdresseIpTel(utilisateur.getAdresseIpTel());
//            responseBody.setDatePassModifie(utilisateur.getDatePassModifie());
//            responseBody.setDateFinContrat(utilisateur.getDateFinContrat());
//
//            // Ajouter les informations sur le profil
//            Profil profil = utilisateur.getProfil();
//            if (profil != null) {
//                responseBody.setProfilId(profil.getIdProfil());
//                responseBody.setProfilDesignation(profil.getDesignation());
//            }
//
//            // Informations sur le département
//            Departements departements = utilisateur.getDepartements();
//            if (departements != null) {
//                responseBody.setDepartementId(departements.getIdDepartement());
//                responseBody.setDepartementDescription(departements.getDescription());
//
//                // Mapping des informations sur la direction
//                Direction direction = departements.getDirection();
//                if (direction != null) {
//                    responseBody.setDirectionId(direction.getIdDirection());
//                    responseBody.setDirectionDesignation(direction.getDescription());
//                }
//            }
//
//            // Ajouter les informations sur le contrat
//            Contrats contrat = utilisateur.getContrats();
//            if (contrat != null) {
//                responseBody.setContratId(contrat.getIdContrat());
//                responseBody.setContratDesignation(contrat.getDesignation());
//            }
//
//            // Retourner la réponse avec le token, l'expirationTime et les informations de l'utilisateur
//            return ResponseEntity.ok(responseBody);
//        } else {
//            // Si l'utilisateur n'est pas trouvé ou si les données ne correspondent pas, renvoyer une erreur d'authentification
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Adresse mail ou matricule incorrect.");
//        }
//    }



    //Authentification with password généré et adresseMail
//    @PostMapping("/authenticateWithMail")
//    public ResponseEntity<?> authenticateUserWithPasswordAndMail(@RequestBody SignInRequest authenticationRequest) {
//        // Vérifier si l'adresse mail et le mot de passe sont présents
//        if (authenticationRequest.getAdresseMail() == null || authenticationRequest.getPassword() == null) {
//            // Si l'un des champs est manquant, retourner un message d'erreur
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'adresse mail et le mot de passe sont obligatoires.");
//        }
//
//        // Rechercher l'utilisateur en fonction de l'adresse mail
//        Utilisateurs utilisateur = utilisateursRepository.findByAdresseMail(authenticationRequest.getAdresseMail());
//
//        // Vérifier si un utilisateur avec cette adresse mail existe
//        if (utilisateur == null) {
//            // Si aucun utilisateur n'est trouvé avec cette adresse mail, retourner une erreur d'authentification
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Adresse mail incorrecte.");
//        }
//
//        // Vérifier si le mot de passe correspond au mot de passe généré automatiquement
//        if (!passwordEncoder.matches(authenticationRequest.getPassword(), utilisateur.getPassword())) {
//            // Si le mot de passe ne correspond pas, retourner une erreur d'authentification
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect.");
//        }
//
//        // Générer un token JWT pour l'utilisateur
//        String token = jwtUtils.generateToken(utilisateur);
//        // Définir l'expirationTime à une semaine (7 jours) à partir de maintenant
//        Instant expirationTime = Instant.now().plus(Duration.ofDays(7));
//
//        // Créer un objet JSON contenant le token et l'expirationTime
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("token", token);
//        responseBody.put("expirationTime", expirationTime.toString());
//
//        // Retourner la réponse avec le token et l'expirationTime
//        return ResponseEntity.ok(responseBody);
//    }

    // Authentification avec le matricule ola mail + password
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest authenticationRequest) {
        // Vérifier si l'identifiant et le mot de passe sont présents
        if (authenticationRequest.getIdentifiant() == null || authenticationRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'identifiant et le mot de passe sont obligatoires.");
        }

        Utilisateurs utilisateur = null;

        // Rechercher l'utilisateur en fonction de l'identifiant (adresse mail ou matricule)
        if (authenticationRequest.getIdentifiant().contains("@")) {
            // On suppose que c'est une adresse mail
            utilisateur = utilisateursRepository.findByAdresseMail(authenticationRequest.getIdentifiant());
        } else {
            // On suppose que c'est un matricule
            utilisateur = utilisateursRepository.findByMatricule(authenticationRequest.getIdentifiant());
        }

        // Vérifier si un utilisateur avec cet identifiant existe
        if (utilisateur == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiant incorrect.");
        }

        // Vérifier si le mot de passe correspond au mot de passe de l'utilisateur
        // Log pour voir le mot de passe saisi et le mot de passe stocké
        // Hada mdp 9dim : $2a$10$6hHWpFgIN/jBkRuxf56LM.xbiKN60CXMisZDDPLcjgH/QIYEig3Cq
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String motDePasseHache = passwordEncoder.encode("MHnabrnh22");
        System.out.println(motDePasseHache);
        System.out.println("Mot de passe saisi : " + authenticationRequest.getPassword());
        System.out.println("Mot de passe stocké : " + utilisateur.getPassword());
        boolean isMatch = passwordEncoder.matches(authenticationRequest.getPassword(), utilisateur.getPassword());
        System.out.println("Mot de passe correspond : " + isMatch);

        if (!isMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect.");
        }
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), utilisateur.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect.");
        }


        // Générer un token JWT pour l'utilisateur
        String token = jwtUtils.generateToken(utilisateur);
        Instant expirationTime = Instant.now().plus(Duration.ofDays(7));

        // Créer un objet SignInResponseDTO contenant le token, l'expirationTime et les informations de l'utilisateur
        SignInResponseDTO responseBody = new SignInResponseDTO();
        responseBody.setToken(token);
        responseBody.setExpirationTime(expirationTime.toString());
        responseBody.setIdUtilisateur(utilisateur.getIdUtilisateur());
        responseBody.setMatricule(utilisateur.getMatricule());
        responseBody.setNom(utilisateur.getNom());
        responseBody.setPrenom(utilisateur.getPrenom());
        responseBody.setAdresseMail(utilisateur.getAdresseMail());
        responseBody.setPosteUtilisateur(utilisateur.getPosteUtilisateur());
        responseBody.setTelephone(utilisateur.getTelephone());
        responseBody.setDateEntree(utilisateur.getDateEntree());
        responseBody.setActif(utilisateur.isActif());
        responseBody.setPhoto(utilisateur.getPhoto());
        responseBody.setPassword(utilisateur.getPassword());
        responseBody.setAdresseIpTel(utilisateur.getAdresseIpTel());
        responseBody.setDatePassModifie(utilisateur.getDatePassModifie());
        responseBody.setDateFinContrat(utilisateur.getDateFinContrat());

        // Ajouter les informations sur le profil
        Profil profil = utilisateur.getProfil();
        if (profil != null) {
            responseBody.setProfilId(profil.getIdProfil());
            responseBody.setProfilDesignation(profil.getDesignation());
        }

        // Ajouter les informations sur le département
        Departements departements = utilisateur.getDepartements();
        if (departements != null) {
            responseBody.setDepartementId(departements.getIdDepartement());
            responseBody.setDepartementDescription(departements.getDescription());

            // Ajouter les informations sur la direction
            Direction direction = departements.getDirection();
            if (direction != null) {
                responseBody.setDirectionId(direction.getIdDirection());
                responseBody.setDirectionDesignation(direction.getDescription());
            }
        }

        // Ajouter les informations sur le contrat
        Contrats contrat = utilisateur.getContrats();
        if (contrat != null) {
            responseBody.setContratId(contrat.getIdContrat());
            responseBody.setContratDesignation(contrat.getDesignation());
        }

        // Retourner la réponse avec le token, l'expirationTime et les informations de l'utilisateur
        return ResponseEntity.ok(responseBody);
    }





//    @PostMapping("/contrats/{contratsId}/users")
//    public ResponseEntity<Utilisateurs> createUserByContrats(@PathVariable(value = "contratsId") Long contratsId,
//                                                          @RequestBody Utilisateurs utilisateurs) {
//        Utilisateurs utilisateurs1 = contratsRepository.findById(contratsId).map(con -> {
//            con.getUtilisateurs().add(utilisateurs);
//            return utilisateursRepository.save(utilisateurs);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Contrat with id = " + contratsId));
//
//        return new ResponseEntity<>(utilisateurs1, HttpStatus.CREATED);
//    }

//    @PostMapping("/departements/{departementId}/users")
//    public ResponseEntity<Utilisateurs> createUserByDepartement(@PathVariable(value = "departementId") Long departementId,
//                                                                @RequestBody Utilisateurs utilisateurs) {
//        Utilisateurs utilisateurs1 = departementsRepository.findById(departementId).map(dep -> {
//            dep.getUtilisateurs().add(utilisateurs);
//            return utilisateursRepository.save(utilisateurs);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Department with id = " + departementId));
//
//        return new ResponseEntity<>(utilisateurs1, HttpStatus.CREATED);
//    }

    //    @PostMapping("/profils/{profilId}/users")
//    public ResponseEntity<Utilisateurs> createUserByProfil(@PathVariable(value = "profilId") Long profilId,
//                                                                @RequestBody Utilisateurs utilisateurs) {
//        Utilisateurs utilisateurs1 = profilRepository.findById(profilId).map(prof -> {
//            prof.getUtilisateurs().add(utilisateurs);
//            return utilisateursRepository.save(utilisateurs);
//        }).orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));
//
//        return new ResponseEntity<>(utilisateurs1, HttpStatus.CREATED);
//    }


    //PUT
    @PutMapping("/users/{id}")
    public ResponseEntity<?> editUtilisateur(
            @PathVariable("id") Long id,
            @ModelAttribute Utilisateurs utilisateurs,
            @RequestParam Long idDepartement,
            @RequestParam Long idContrats,
            @RequestParam Long idProfil,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // Vérifier si l'utilisateur existe
        Optional<Utilisateurs> existingUserOptional = utilisateursRepository.findById(id);
        if (!existingUserOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé avec l'ID : " + id);
        }

        Utilisateurs existingUser = existingUserOptional.get();

        // Vérification des entités liées : département, contrat, profil
        Departements departement = departementsRepository.findById(idDepartement)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + idDepartement));
        Contrats contrats = contratsRepository.findById(idContrats)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat non trouvé avec l'ID : " + idContrats));
        Profil profil = profilRepository.findById(idProfil)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'ID : " + idProfil));

        // Gestion de la photo
        if (file != null && !file.isEmpty()) {
            String uploadDir = "C:/photos-back/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            try {
                Files.createDirectories(filePath.getParent());
                file.transferTo(filePath.toFile());
                String newPhotoUrl = "http://139.99.193.34:8081/images/" + fileName;
                existingUser.setPhoto(newPhotoUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement du fichier.");
            }
        }

        // Mise à jour des autres données
        if (utilisateurs.getMatricule() != null) existingUser.setMatricule(utilisateurs.getMatricule());
        if (utilisateurs.getPrenom() != null) existingUser.setPrenom(utilisateurs.getPrenom());
        if (utilisateurs.getNom() != null) existingUser.setNom(utilisateurs.getNom());
        if (utilisateurs.getAdresseMail() != null) existingUser.setAdresseMail(utilisateurs.getAdresseMail());
        if (utilisateurs.getPosteUtilisateur() != null) existingUser.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
        if (utilisateurs.getTelephone() != null) existingUser.setTelephone(utilisateurs.getTelephone());
        if (utilisateurs.getDateEntree() != null) existingUser.setDateEntree(utilisateurs.getDateEntree());
        if (utilisateurs.getDatePassModifie() != null) existingUser.setDatePassModifie(utilisateurs.getDatePassModifie());
        if (utilisateurs.getDateFinContrat() != null) existingUser.setDateFinContrat(utilisateurs.getDateFinContrat());
        if (utilisateurs.getPassword() != null) existingUser.setPassword(utilisateurs.getPassword());
        if (utilisateurs.getAdresseIpTel() != null) existingUser.setAdresseIpTel(utilisateurs.getAdresseIpTel());
        if (utilisateurs.getRole() != null) existingUser.setRole(utilisateurs.getRole());
        existingUser.setActif(utilisateurs.isActif());

        // Mise à jour des relations
        existingUser.setDepartements(departement);
        existingUser.setProfil(profil);
        existingUser.setContrats(contrats);

        // Enregistrer les modifications
        Utilisateurs savedUser = utilisateursRepository.save(existingUser);

        if (savedUser != null) {
            return ResponseEntity.ok("Modification de l'utilisateur avec l'ID " + id + " effectuée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de la modification de l'utilisateur.");
        }
    }



    //DELETE
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        utilisateursRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        utilisateursRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/contrats/{contratsId}/users")
//    public ResponseEntity<List<Utilisateurs>> deleteAllUsersOfContrat(@PathVariable(value = "contratsId") Long contratsId) {
//        Contrats contrats = contratsRepository.findById(contratsId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Contrat with id = " + contratsId));
//
//        contrats.removeUtilisateurs();
//        contratsRepository.save(contrats);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/profils/{profilId}/users")
//    public ResponseEntity<List<Utilisateurs>> deleteAllUsersOfProfil(@PathVariable(value = "profilId") Long profilId) {
//        Profil profil = profilRepository.findById(profilId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Profil with id = " + profilId));
//
//        profil.removeUtilisateurs();
//        profilRepository.save(profil);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @DeleteMapping("/departements/{depatementsId}/users")
//    public ResponseEntity<List<Utilisateurs>> deleteAllUsersOfDepartement(@PathVariable(value = "depatementsId") Long depatementsId) {
//        Departements departements = departementsRepository.findById(depatementsId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Department with id = " + depatementsId));
//
//        departements.removeUtilisateurs();
//        departementsRepository.save(departements);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
