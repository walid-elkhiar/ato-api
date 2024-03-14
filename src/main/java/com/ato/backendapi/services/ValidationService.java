package com.ato.backendapi.services;

import com.ato.backendapi.dto.*;
import com.ato.backendapi.entities.Demandes;
import com.ato.backendapi.entities.Validateur;
import com.ato.backendapi.entities.Validation;
import com.ato.backendapi.repositories.ValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    private ValidationMapper validationMapper;
    
    private ValidateurMapper validateurMapper;

    @Autowired
    private DemandesMapper demandesMapper;

    public List<ValidationDTO> getDemandesByValidateur(Long validateurId) {
        List<Validation> validations = validationRepository.findByValidateurId(validateurId);
        return validations.stream()
                .map(validationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Map<ValidateurDTO, List<DemandesDTO>> getDemandesGroupedByValidateur() {
        List<Validation> validations = validationRepository.findAll();

        Map<Validateur, List<Demandes>> groupedDemandes = validations.stream()
                .collect(Collectors.groupingBy(
                        Validation::getValidateur,
                        Collectors.mapping(Validation::getDemandes, Collectors.toList())
                ));

        Map<ValidateurDTO, List<DemandesDTO>> result = new HashMap<>();
        for (Map.Entry<Validateur, List<Demandes>> entry : groupedDemandes.entrySet()) {
            result.put(
                    validateurMapper.toDTO(entry.getKey()),
                    entry.getValue().stream().map(demandesMapper::toDTO).collect(Collectors.toList())
            );
        }

        return result;
    }
}
