package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Habilitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HabilitationRepository extends JpaRepository<Habilitation, Long> {

    @RestResource(path = "findByProfilIdAndActif", rel = "findByProfilIdAndActif")
    List<Habilitation> findByProfil_IdProfilAndActifTrue(Long idProfil);

    @RestResource(exported = false)
    List<Habilitation> findByProfil_IdProfil(Long idProfil);

    @RestResource(path = "findByProfilId", rel = "findByProfilId")
    Page<Habilitation> findByProfil_IdProfil(Long idProfil, Pageable pageable);
}

