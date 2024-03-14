package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.*;
import com.ato.backendapi.repositories.*;
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
public class UtilisateursController {

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    @Autowired
    private ContratsRepository contratsRepository;

    @Autowired
    private DepartementsRepository departementsRepository;

    @Autowired
    private ProfilRepository profilRepository;

    //GET
    @GetMapping("/AllUsers")
    public ResponseEntity<List<Utilisateurs>> listeUsers(){
        List<Utilisateurs> utilisateurs = new ArrayList<Utilisateurs>();

        utilisateursRepository.findAll().forEach(utilisateurs::add);
        if (utilisateurs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
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

//    @GetMapping("/users/{departementId}/AllUsers")
//    public ResponseEntity<List<Utilisateurs>> getAllUsersByDepartementId(@PathVariable(value = "departementId") Long departementId) {
//        Departements departements = departementsRepository.findById(departementId)
//                .orElseThrow(() -> new ResourceNotFoundException("Not found Departement with id = " + departementId));
//
//        List<Utilisateurs> utilisateurs = new ArrayList<>();
//        utilisateurs.addAll(departements.getUtilisateurs());
//
//        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
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
    public ResponseEntity<Utilisateurs> getUserById(@PathVariable("id") Long id) {
        Utilisateurs utilisateurs = utilisateursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + id));

        return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
    }
    //POST
    @PostMapping("/users")
    public ResponseEntity<Utilisateurs> add(@RequestBody Utilisateurs utilisateurs, @RequestParam Long idDepartement,
    @RequestParam Long idContrats,@RequestParam Long idProfil) {
        Departements departement = departementsRepository.findById(idDepartement)
                .orElseThrow(() -> new ResourceNotFoundException("Departement non trouvé avec l'ID : " + idDepartement));
        Contrats contrats = contratsRepository.findById(idContrats)
                .orElseThrow(() -> new ResourceNotFoundException("Contrats non trouvé avec l'ID : " + idContrats));
        Profil profil = profilRepository.findById(idProfil)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'ID : " + idProfil));

        utilisateurs.setDepartements(departement);
        utilisateurs.setContrats(contrats);
        utilisateurs.setProfil(profil);

        Utilisateurs u = utilisateursRepository.save(utilisateurs);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
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
    public ResponseEntity<Utilisateurs> edit(@PathVariable("id") Long id, @RequestBody Utilisateurs utilisateurs,@RequestParam Long idDepartement,
                                             @RequestParam Long idContrats,@RequestParam Long idProfil) {
        Utilisateurs existingUser = utilisateursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID : " + id));

        Departements departement = departementsRepository.findById(idDepartement)
                .orElseThrow(() -> new ResourceNotFoundException("Département non trouvé avec l'ID : " + idDepartement));

        Profil profil = profilRepository.findById(idProfil)
                .orElseThrow(() -> new ResourceNotFoundException("Profil non trouvé avec l'ID : " + idProfil));

        Contrats contrats = contratsRepository.findById(idContrats)
                .orElseThrow(() -> new ResourceNotFoundException("Contrats non trouvé avec l'ID : " + idContrats));

        existingUser.setDepartements(departement);
        existingUser.setProfil(profil);
        existingUser.setContrats(contrats);
//        utilisateurs1.setTracabilites(utilisateurs.getTracabilites());
//        utilisateurs1.setValidateurs(utilisateurs.getValidateurs());
//        utilisateurs1.setTraitements(utilisateurs.getTraitements());
//        utilisateurs1.setPointages(utilisateurs.getPointages());
//        utilisateurs1.setDocumentRH(utilisateurs.getDocumentRH());
        existingUser.setActif(utilisateurs.getActif());
        existingUser.setMatricule(utilisateurs.getMatricule());
        existingUser.setPrenom(utilisateurs.getPrenom());
        existingUser.setPassword(utilisateurs.getPassword());
        existingUser.setPhoto(utilisateurs.getPhoto());
        existingUser.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
        existingUser.setNom(utilisateurs.getNom());
        existingUser.setAdresseMail(utilisateurs.getAdresseMail());
        existingUser.setDateEntree(utilisateurs.getDateEntree());
        existingUser.setDatePassModifie(utilisateurs.getDatePassModifie());
        existingUser.setDateFinContrat(utilisateurs.getDateFinContrat());
        existingUser.setTelephone(utilisateurs.getTelephone());
        existingUser.setAdresseIpTel(utilisateurs.getAdresseIpTel());


        return new ResponseEntity<>(utilisateursRepository.save(existingUser), HttpStatus.OK);
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
