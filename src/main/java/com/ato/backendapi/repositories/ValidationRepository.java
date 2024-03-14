package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Validation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ValidationRepository extends JpaRepository<Validation,Long> {
    Page<Validation> findAll(Pageable pageable);
}
