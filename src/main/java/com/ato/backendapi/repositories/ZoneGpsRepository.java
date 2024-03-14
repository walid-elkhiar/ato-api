package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.ZoneGPS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ZoneGpsRepository extends JpaRepository<ZoneGPS,Long> {
}
