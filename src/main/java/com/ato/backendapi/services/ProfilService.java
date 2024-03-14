package com.ato.backendapi.services;

import com.ato.backendapi.entities.Habilitation;
import com.ato.backendapi.entities.NiveauHabilitation;
import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.repositories.HabilitationRepository;
import com.ato.backendapi.repositories.ProfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfilService {

    @Autowired
    private ProfilRepository profilRepository;

    @Autowired
    private HabilitationRepository habilitationRepository;

    public Profil createProfilWithHabilitations(Profil profil) {
        // Créer le profil
        Profil savedProfil = profilRepository.save(profil);

        // Ajouter les habilitations avec les valeurs par défaut
        List<Habilitation> habilitations = Arrays.asList(
                new Habilitation("Dashboard", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Employees", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Employee Profile", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Validator Management", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Zone Assignment", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Scheduling", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("View Schedule", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Modify/Assign", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Counters", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Counter Balances", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Add Counter Credits", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Absences", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Vacation Requests", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Absence Requests", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Request Tracking", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Request History", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Document Management", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("HR Document Request", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("View HR Documents", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("View Payroll", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Validate HR Documents", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Reporting", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Clock In/Out", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Non-Logged Status", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Anomaly Type Report", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Lunch Break Exceedance", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Calculations Report", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Events Report", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Good Exit Report", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Unjustified Absence", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Undefined Time", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Worked Hours", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Lateness Report", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Document Recap", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Tools", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Calculate Attendance", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Retrieve Attendance", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Manual Attendance", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Attendance Management", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Overtime Management", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Settings", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Company Information", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Department/Division", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Authorizations", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Work Schedules", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Shift Planning", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Document Management", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Holidays and Vacations", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Categories", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Processing Code", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Requests by Profile", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Traceability", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Bonus", true, false, NiveauHabilitation.NIVEAU1, savedProfil),
                new Habilitation("Email Configuration", true, false, NiveauHabilitation.NIVEAU1, savedProfil)
        );

        habilitationRepository.saveAll(habilitations);

        return savedProfil;
    }
}
