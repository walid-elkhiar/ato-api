package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.DocumentRH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DocumentRhRepository extends JpaRepository<DocumentRH,Long> {
}
