package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.DetailPlansTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DetailPlansTravailRepository extends JpaRepository<DetailPlansTravail,Long> {
}
