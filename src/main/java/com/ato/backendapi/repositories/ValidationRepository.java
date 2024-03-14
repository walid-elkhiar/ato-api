package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Validation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidationRepository extends JpaRepository<Validation,Long> {
    Page<Validation> findAll(Pageable pageable);
    @Query("SELECT v FROM Validation v JOIN v.validateur val WHERE val.validateur.idUtilisateur = :validateurId")
    List<Validation> findByValidateurId(@Param("validateurId") Long validateurId);
    List<Validation> findAll();
}
