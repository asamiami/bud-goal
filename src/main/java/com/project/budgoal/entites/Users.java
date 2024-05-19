package com.project.budgoal.entites;


import com.project.budgoal.enums.AccountStatus;
import com.project.budgoal.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Users extends BaseEntity implements UserDetails {

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String nickName;

    private String verificationCode;

    @Enumerated
    private Roles userRoles ;

    private AccountStatus accountStatus;

    @NonNull
    private String email;

    @NonNull
    private String password;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRoles.name()));
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
