package com.ozgur.Hospital.System.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ozgur.Hospital.System.dto.ReportDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
@Builder
@Entity
public class Patient implements UserDetails {

    @Id
    private Long TC;
    @Column(nullable = false, unique = true)
    private String name;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Report> reports;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;


    //Fields for the UserDetails interface
    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    @JsonIgnore
    private boolean accountNonExpired;
    @JsonIgnore
    private boolean isEnabled;
    @JsonIgnore
    private boolean accountNonLocked;
    @JsonIgnore
    private boolean credentialsNonExpired;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "all_authorities", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Set<Role> authorities = new HashSet<>(List.of(Role.ROLE_USER));


}
