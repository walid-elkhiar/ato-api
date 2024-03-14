package com.ato.backendapi.dto;

import com.ato.backendapi.entities.Utilisateurs;

public class UtilisateursMapper {

    public static UtilisateursDTO toDTO(Utilisateurs utilisateurs) {
        if (utilisateurs == null) {
            return null;
        }

        UtilisateursDTO dto = new UtilisateursDTO();
        dto.setIdUtilisateur(utilisateurs.getIdUtilisateur());
        dto.setMatricule(utilisateurs.getMatricule());
        dto.setNom(utilisateurs.getNom());
        dto.setPrenom(utilisateurs.getPrenom());
        dto.setAdresseMail(utilisateurs.getAdresseMail());
        dto.setPosteUtilisateur(utilisateurs.getPosteUtilisateur());
        dto.setTelephone(utilisateurs.getTelephone());
        dto.setDateEntree(utilisateurs.getDateEntree());
        dto.setActif(utilisateurs.isActif());
        dto.setPhoto(utilisateurs.getPhoto());
        dto.setPassword(utilisateurs.getPassword());
        dto.setAdresseIpTel(utilisateurs.getAdresseIpTel());
        dto.setDatePassModifie(utilisateurs.getDatePassModifie());
        dto.setDateFinContrat(utilisateurs.getDateFinContrat());
        dto.setRole(utilisateurs.getRole());

        if (utilisateurs.getProfil() != null) {
            dto.setProfilId(utilisateurs.getProfil().getIdProfil());
            dto.setProfilDesignation(utilisateurs.getProfil().getDesignation());
        }

        if (utilisateurs.getDepartements() != null) {
            dto.setDepartementId(utilisateurs.getDepartements().getIdDepartement());
            dto.setDepartementDescription(utilisateurs.getDepartements().getDescription());
        }

        if (utilisateurs.getContrats() != null) {
            dto.setContratId(utilisateurs.getContrats().getIdContrat());
            dto.setContratDesignation(utilisateurs.getContrats().getDesignation());
        }

        // Vous pouvez ajouter d'autres champs si nécessaire

        return dto;
    }
}
