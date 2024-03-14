package com.ato.backendapi.dto;

import com.ato.backendapi.dto.DemandesDTO;
import com.ato.backendapi.entities.Demandes;
import org.springframework.stereotype.Component;

@Component
public class DemandesMapper {

    public DemandesDTO toDTO(Demandes demandes) {
        if (demandes == null) {
            return null;
        }

        DemandesDTO dto = new DemandesDTO();
        dto.setIdDemande(demandes.getIdDemande());
        dto.setDateSaisie(demandes.getDateSaisie());
        dto.setCodeMotif(demandes.getCodeMotif());
        dto.setDateDebutMission(demandes.getDateDebutMission());
        dto.setDateFinMission(demandes.getDateFinMission());
        dto.setDuree(demandes.getDuree());
        dto.setObjet(demandes.getObjet());
        dto.setDateReprise(demandes.getDateReprise());
        dto.setPlage(demandes.getPlage());
        dto.setEtatValidation(demandes.getEtatValidation());

        dto.setUtilisateur_demande(UtilisateursMapper.toDTO(demandes.getUtilisateurs()));
        dto.setUtilisateur_saisi_demande(UtilisateursMapper.toDTO(demandes.getUtilisateurSaisiDemande()));

        return dto;
    }
}
