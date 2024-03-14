package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.entities.SousCodeTraitement;
import com.ato.backendapi.repositories.ProfilRepository;
import com.ato.backendapi.repositories.SousCodeTraitementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ParametrageDemandeController {

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private SousCodeTraitementRepository sousCodeTraitementRepository;

    // Méthode POST pour gérer l'ajout ou la suppression
    @PostMapping("/parametrageDemande/update")
    public ResponseEntity<String> updateParametrageDemande(
            @RequestParam Long idProfil,
            @RequestParam Long idSousCodeTraitement,
            @RequestParam boolean isChecked) {

        // Récupérer le profil et le sous-code de traitement
        Profil profil = profilRepository.findById(idProfil).orElse(null);
        SousCodeTraitement sousCodeTraitement = sousCodeTraitementRepository.findById(idSousCodeTraitement).orElse(null);

        if (profil == null || sousCodeTraitement == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profil ou SousCodeTraitement introuvable.");
        }

        if (isChecked) {
            // Ajouter le sous-code de traitement au profil si la case est cochée
            sousCodeTraitement.addProfil(profil);
            sousCodeTraitementRepository.save(sousCodeTraitement);
            return ResponseEntity.status(HttpStatus.OK).body("Paramétrage ajouté avec succès.");
        } else {
            // Supprimer le sous-code de traitement du profil si la case est décochée
            sousCodeTraitement.removeProfil(idProfil);
            sousCodeTraitementRepository.save(sousCodeTraitement);
            return ResponseEntity.status(HttpStatus.OK).body("Paramétrage supprimé avec succès.");
        }
    }
}

