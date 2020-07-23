package com.kakaopay.coupon.api.persistence.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "unique_id", columnNames = "id")
})
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "id", length = 20, nullable = false, unique = true)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Builder
    public UserEntity(Long no, String id, String password, List<String> roles) {
        this.no = no;
        this.id = id;
        this.password = password;
        this.roles = roles;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<> ();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return Long.toString(no);
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
