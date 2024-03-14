package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.ListeAttestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ListeAttestationRepository extends JpaRepository<ListeAttestation,Long> {
}
