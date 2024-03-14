package com.ato.backendapi.repositories;

import com.ato.backendapi.entities.EmailConfiguration;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration,Long> {
    @Transactional
    void deleteById(Long id);

//    @Transactional
//    void deleteBySociete_IdSociete(Long societeId);
    @Transactional
    @Modifying
    @Query("UPDATE EmailConfiguration e SET e.statut = false WHERE e.statut = true")
    void deactivateAll();


}
