package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DirectionRepository extends JpaRepository<Direction,Long> {
}
