package com.ga.Banking;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Users {
    private String username;
    private String password;
    private String role;
    private int failed_login_attempts;
    private LocalDateTime account_locked_until;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private String name;


    public Users(String username, String password, String role, String name) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name=name;
        this.failed_login_attempts = 0;
        this.account_locked_until= LocalDateTime.now();
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    public int getFailed_login_attempts() {
        return failed_login_attempts;
    }

    public void setFailed_login_attempts(int failed_login_attempts) {
        this.failed_login_attempts = failed_login_attempts;
    }

    public LocalDateTime getAccount_locked_until() {
        return account_locked_until;
    }

    public void setAccount_locked_until(LocalDateTime account_locked_until) {
        this.account_locked_until = account_locked_until;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
