package com.ato.backendapi.controllers;

import com.ato.backendapi.entities.Tracabilite;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.TracabiliteRepository;
import com.ato.backendapi.repositories.UtilisateursRepository;
import com.ato.backendapi.security.service.OurUserDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class TracabiliteAspect {

    @Autowired
    private TracabiliteRepository tracabiliteRepository;

    @Autowired
    private UtilisateursRepository utilisateursRepository;

    @Pointcut("execution(* com.ato.backendapi.controllers.*.*(..))")
    public void allControllerMethods() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMappingMethods() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMappingMethods() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMappingMethods() {}

    @AfterReturning("allControllerMethods() && (postMappingMethods() || putMappingMethods() || deleteMappingMethods())")
    public void logAfterMethod(JoinPoint joinPoint) {
        // Obtenez l'utilisateur actuel
        Utilisateurs user = getCurrentUser();

        // Vérifiez que l'utilisateur n'est pas nul avant de procéder
        if (user != null) {
            // Enregistrez l'action dans la table tracabilite
            Tracabilite tracabilite = new Tracabilite();
            tracabilite.setUtilisateurs(user);
            tracabilite.setOperation(joinPoint.getSignature().getName());
            tracabilite.setConcerne(user.getMatricule());

            // Vérifiez si des arguments sont présents
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0 && args[0] != null) {
                // Extraire le matricule de l'objet concerné s'il s'agit d'une instance de Utilisateurs
                if (args[0] instanceof Utilisateurs) {
                    Utilisateurs concernedUser = (Utilisateurs) args[0];
                    tracabilite.setConcerne(concernedUser.getMatricule());
                } else {
                    tracabilite.setConcerne("N/A"); // ou tout autre valeur par défaut appropriée
                }
            } else {
                tracabilite.setConcerne("N/A"); // ou tout autre valeur par défaut appropriée
            }

            tracabilite.setDateOperation(new Date());
            tracabilite.setAdreesePc(getClientIpAddress());

            tracabiliteRepository.save(tracabilite);
        }
    }

    private Utilisateurs getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof OurUserDetails) {
            String username = ((OurUserDetails) principal).getUsername();
            return utilisateursRepository.findByAdresseMail(username);
        }
        return null;
    }

    private String getClientIpAddress() {
        // Implémentez cette méthode pour obtenir l'adresse IP du client
        return "127.0.0.1";
    }
}