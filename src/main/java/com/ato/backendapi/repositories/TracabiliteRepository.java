package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Tracabilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TracabiliteRepository extends JpaRepository<Tracabilite,Long> {
}
