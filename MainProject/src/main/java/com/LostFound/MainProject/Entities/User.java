package com.LostFound.MainProject.Entities;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LostItem> lostItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoundItem> foundItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;

    public User() {
        // Default constructor
    }

    public User(Long id, String name, String email, String password, Roles role, UserType userType,
                List<LostItem> lostItems, List<FoundItem> foundItems, List<Claim> claims) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.userType = userType;
        this.lostItems = lostItems;
        this.foundItems = foundItems;
        this.claims = claims;
    }

    // Getters and setters

    public User(Long id) {            // helper ctor used by service layer
        this.id = id;
    }

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<LostItem> getLostItems() {
        return lostItems;
    }

    public void setLostItems(List<LostItem> lostItems) {
        this.lostItems = lostItems;
    }

    public List<FoundItem> getFoundItems() {
        return foundItems;
    }

    public void setFoundItems(List<FoundItem> foundItems) {
        this.foundItems = foundItems;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    // Spring Security UserDetails methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return role as authority exactly as in enum, e.g., ROLE_ADMIN, ROLE_REPORTER
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;  // use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // customize as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // customize as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // customize as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // customize as needed
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", role=" + role + ", userType=" + userType + "]";
    }
}
