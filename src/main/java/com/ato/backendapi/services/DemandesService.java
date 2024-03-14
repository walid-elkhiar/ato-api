package com.ato.backendapi.services;

import com.ato.backendapi.dto.DemandesDTO;
import com.ato.backendapi.dto.DemandesMapper;
import com.ato.backendapi.dto.ValidationMapper;
import com.ato.backendapi.entities.Demandes;
import com.ato.backendapi.repositories.DemandesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandesService {
    @Autowired
    private DemandesRepository demandesRepository;
    @Autowired
    private ValidationMapper validationMapper;
    @Autowired
    private DemandesMapper demandesMapper;

    public List<DemandesDTO> getDemandesByValidateurAndEtat(Long validateurId, String etatValidation) {
        List<Demandes> demandes = demandesRepository.findDemandesByValidateurAndEtat(validateurId, etatValidation);
        return demandes.stream()
                .map(demandesMapper::toDTO)
                .collect(Collectors.toList());
    }
}

