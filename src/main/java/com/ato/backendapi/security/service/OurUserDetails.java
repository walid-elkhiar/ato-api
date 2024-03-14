package com.ato.backendapi.security.service;

import com.ato.backendapi.entities.Utilisateurs;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Getter
@Data
public class OurUserDetails implements UserDetails {

    private Utilisateurs utilisateurs;

    public OurUserDetails(Utilisateurs utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retournez les autorités de l'utilisateur ici
        return null;
    }

    @Override
    public String getPassword() {
        return utilisateurs.getPassword();
    }

    @Override
    public String getUsername() {
        return utilisateurs.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return utilisateurs.isActif();
    }

}

