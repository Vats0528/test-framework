package com.test.models;

import com.framework.util.Auth;

/**
 * Sprint 11 bis: Modèle d'utilisateur implémentant l'interface Auth
 */
public class User implements Auth {
    
    private String username;
    private String password;
    private String role;
    private String email;
    
    public User() {
    }
    
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public User(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }
    
    @Override
    public boolean hasRole(String role) {
        return this.role != null && this.role.equals(role);
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "User{username='" + username + "', role='" + role + "', email='" + email + "'}";
    }
}
