package com.ato.backendapi.security.service;

import com.ato.backendapi.entities.Contrats;
import com.ato.backendapi.entities.Departements;
import com.ato.backendapi.entities.Profil;
import com.ato.backendapi.entities.Utilisateurs;
import com.ato.backendapi.repositories.ContratsRepository;
import com.ato.backendapi.repositories.DepartementsRepository;
import com.ato.backendapi.repositories.ProfilRepository;
import com.ato.backendapi.repositories.UtilisateursRepository;
import com.ato.backendapi.security.dto.RequestResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UtilisateursRepository utilisateursRepository;
    @Autowired
    private ProfilRepository profilRepository;
    @Autowired
    private ContratsRepository contratsRepository;
    @Autowired
    private DepartementsRepository departementsRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public RequestResp signUp(RequestResp registrationRequest){
        RequestResp resp = new RequestResp();
        try {
            // Récupérer les instances de Profil, Departements et Contrats à partir de leurs identifiants
            Profil profil = profilRepository.findById(registrationRequest.getProfilId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profil not found with id: " + registrationRequest.getProfilId()));

            Departements departements = departementsRepository.findById(registrationRequest.getDepartementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Departements not found with id: " + registrationRequest.getDepartementId()));

            Contrats contrats = contratsRepository.findById(registrationRequest.getContratId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contrats not found with id: " + registrationRequest.getContratId()));

            Utilisateurs ourUsers = new Utilisateurs();
            ourUsers.setAdresseMail(registrationRequest.getAdresseMail());
            //ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            ourUsers.setProfil(profil);
            ourUsers.setDepartements(departements);
            ourUsers.setContrats(contrats);
            ourUsers.setNom(registrationRequest.getNom());
            ourUsers.setTelephone(registrationRequest.getTelephone());
            ourUsers.setPhoto(registrationRequest.getPhoto());
            ourUsers.setPrenom(registrationRequest.getPrenom());
            ourUsers.setMatricule(registrationRequest.getMatricule());
            ourUsers.setActif(registrationRequest.isActif());
            ourUsers.setDatePassModifie(registrationRequest.getDatePassModifie());
            ourUsers.setDateEntree(registrationRequest.getDateEntree());
            ourUsers.setDateFinContrat(registrationRequest.getDateFinContrat());
            ourUsers.setAdresseIpTel(registrationRequest.getAdresseIpTel());
            ourUsers.setPosteUtilisateur(registrationRequest.getPosteUtilisateur());

            Utilisateurs ourUserResult = utilisateursRepository.save(ourUsers);
            if (ourUserResult != null && ourUserResult.getIdUtilisateur()>0) {
                resp.setUsers(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public RequestResp signIn(RequestResp signinRequest){
        RequestResp response = new RequestResp();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getAdresseMail(),signinRequest.getMatricule()));
            //var user = utilisateursRepository.findByAdresseMail(signinRequest.getAdresseMail()).orElseThrow();
            var user = utilisateursRepository.findByAdresseMail(signinRequest.getAdresseMail());

            System.out.println("USER IS: "+ user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public RequestResp refreshToken(RequestResp refreshTokenReqiest){
        RequestResp response = new RequestResp();
        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        //Utilisateurs users = utilisateursRepository.findByAdresseMail(ourEmail).orElseThrow();
        Utilisateurs users = utilisateursRepository.findByAdresseMail(ourEmail);
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }
}
