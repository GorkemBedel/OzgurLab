package com.ozgur.Hospital.System.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "laborants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Laborant implements UserDetails {


    @Id
    @Column(name = "laborant_id")
    private Long laborant_id;
    private String name;
    private String surname;
    private String department;

    @OneToMany(mappedBy = "laborant", cascade = CascadeType.ALL)
    private List<Report> reports;

    //Fields for the UserDetails interface
    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    private boolean accountNonExpired;
    private boolean isEnabled;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_LABORANT;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "all_authorities", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> authorities = new HashSet<>(List.of(Role.ROLE_LABORANT));
}
