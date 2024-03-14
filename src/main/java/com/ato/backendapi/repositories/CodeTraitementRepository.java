package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.CodeTraitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CodeTraitementRepository extends JpaRepository<CodeTraitement,Long> {
}
