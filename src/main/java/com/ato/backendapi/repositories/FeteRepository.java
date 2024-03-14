package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Fete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeteRepository extends JpaRepository<Fete,Long> {
    List<Fete> findByDateDebutBetween(Date startDate, Date endDate);
    List<Fete> findByDateDebutBetweenOrDateFinBetween(Date startDate, Date endDate, Date startDate2, Date endDate2);
}
