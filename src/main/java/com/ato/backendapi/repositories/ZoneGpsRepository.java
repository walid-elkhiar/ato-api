package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.ZoneGPS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ZoneGpsRepository extends JpaRepository<ZoneGPS,Long> {
    Page<ZoneGPS> findAll(Pageable pageable);
}
