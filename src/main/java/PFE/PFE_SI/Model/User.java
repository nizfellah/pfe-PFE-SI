package PFE.PFE_SI.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Document(collection = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private String password; // renommé depuis "motDePasse"

    // Implémentation de UserDetails 👇

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email; // on utilise l'email comme identifiant
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // à adapter si besoin
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // à adapter si besoin
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // à adapter si besoin
    }

    @Override
    public boolean isEnabled() {
        return true; // à adapter si besoin
    }
}
