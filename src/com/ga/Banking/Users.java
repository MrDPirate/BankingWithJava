package com.ga.Banking;


import java.time.LocalDateTime;

public class Users {
    private static String username;
    private static String password;
    private static String role;
    private static int failed_login_attempts;
    private static LocalDateTime account_locked_until;
    private static LocalDateTime created_at;
    private static LocalDateTime updated_at;
    private static String isLoggedIn;
    private static String name;


    public Users(String[] user) {
        username = user[0];
        password = user[1];
        role = user[2];
        failed_login_attempts = Integer.parseInt(user[3]);
        account_locked_until= LocalDateTime.parse(user[4]);
        created_at = LocalDateTime.parse(user[5]);
        updated_at = LocalDateTime.parse(user[6]);
        isLoggedIn = user[7];
        name = user [8];
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Users.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Users.password = password;
    }

    public static String getRole() {
        return role;
    }

    public  void setRole(String role) {
        Users.role = role;
    }

    public static int getFailed_login_attempts() {
        return failed_login_attempts;
    }

    public static void setFailed_login_attempts(int failed_login_attempts) {
        Users.failed_login_attempts = failed_login_attempts;
    }

    public static LocalDateTime getAccount_locked_until() {
        return account_locked_until;
    }

    public static void setAccount_locked_until(LocalDateTime account_locked_until) {
        Users.account_locked_until = account_locked_until;
    }

    public static LocalDateTime getCreated_at() {
        return created_at;
    }

    public static void setCreated_at(LocalDateTime created_at) {
        Users.created_at = created_at;
    }

    public static LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public static void setUpdated_at(LocalDateTime updated_at) {
        Users.updated_at = updated_at;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Users.name = name;
    }

    public static String getIsLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(String isLoggedIn) {
        Users.isLoggedIn = isLoggedIn;
    }
}
