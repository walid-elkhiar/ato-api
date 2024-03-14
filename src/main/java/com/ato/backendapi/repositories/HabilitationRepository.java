package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Habilitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HabilitationRepository extends JpaRepository<Habilitation,Long> {
}
