package com.ato.backendapi.dto;

import com.ato.backendapi.entities.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidationMapper {

    @Autowired
    private DemandesMapper demandesMapper;

    public ValidationDTO toDTO(Validation validation) {
        if (validation == null) {
            return null;
        }

        ValidationDTO dto = new ValidationDTO();
        dto.setIdValidation(validation.getIdValidation());
        dto.setDateValidation(validation.getDateValidation());
        dto.setIdValidateur(validation.getValidateur().getIdValidateur());
        dto.setNiveau(validation.getValidateur().getNiveau());
        dto.setUtilisateur(UtilisateursMapper.toDTO(validation.getValidateur().getUtilisateurs()));
        dto.setValidateur(UtilisateursMapper.toDTO(validation.getValidateur().getValidateur()));

        // Utiliser DemandesMapper pour convertir les demandes
        dto.setIdDemande(validation.getDemandes().getIdDemande());
        dto.setDateSaisie(validation.getDemandes().getDateSaisie());
        dto.setCodeMotif(validation.getDemandes().getCodeMotif());
        dto.setDateDebutMission(validation.getDemandes().getDateDebutMission());
        dto.setDateFinMission(validation.getDemandes().getDateFinMission());
        dto.setDuree(validation.getDemandes().getDuree());
        dto.setObjet(validation.getDemandes().getObjet());
        dto.setDateReprise(validation.getDemandes().getDateReprise());
        dto.setPlage(validation.getDemandes().getPlage());
        dto.setEtatValidation(validation.getDemandes().getEtatValidation());
        dto.setUtilisateur_demande(UtilisateursMapper.toDTO(validation.getDemandes().getUtilisateurs()));
        dto.setUtilisateur_saisi_demande(UtilisateursMapper.toDTO(validation.getDemandes().getUtilisateurSaisiDemande()));

        return dto;
    }
}
