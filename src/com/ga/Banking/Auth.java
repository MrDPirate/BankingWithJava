package com.ga.Banking;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;


public class Auth{
    public static String hashPass256(String textToHash) {
        try {
            // Get an instance of the SHA-256 MessageDigest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input string to bytes using UTF-8 encoding
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);

            // Compute the hash
            byte[] hashedByteArray = digest.digest(byteOfTextToHash);

            // Encode the resulting byte array to a Base64 string for easy representation
            String encodedHash = Base64.getEncoder().encodeToString(hashedByteArray);

            return encodedHash;

        } catch (NoSuchAlgorithmException e) {
            // Handle the case where SHA-256 algorithm is not available
            e.printStackTrace();
            return null;
        }
    }

    private static void register(String username, String password, String role,String name){
        String hashed_password = hashPass256(password);
        if (dbHelper.checkUserExist(username)){
            System.out.println("User Already exist");
            return;
        }
        System.out.println("Creating account for: "+username);
        dbHelper.addNew(username,hashed_password,role,name);
    }

    private static boolean login(String username, String password) {
        if (dbHelper.checkUserExist(username)) {
            String[] user = dbHelper.getUserData(username);


            // guard to check if the account is locked
            if (dbHelper.getAccount_locked_until(username).isAfter(LocalDateTime.now())
                    && dbHelper.getFailed_login_attempts(username)==3){

                System.out.println("Your account is locked until:" +Users.getAccount_locked_until());
                return false;
            }


            if (Objects.equals(hashPass256(password), user[1])) {
                dbHelper.setLoggedIn(username);
                return true;
            } else {
                System.out.println("Username or password is wrong");
                
                if (dbHelper.getFailed_login_attempts(username)==2){
                    dbHelper.setAccount_locked_for_one_minute(username);
                }
                dbHelper.incrementFailed_login_attempts(username);
                return false;
            }
        } else {
            System.out.println("Username or password is wrong");
            return false;
        }
    }

    private static void changePass(String username, String oldPass, String newPass, String newPass2){
        String [] user = dbHelper.getUserData(username);
        if (Objects.equals(Auth.hashPass256(oldPass), oldPass)){
            if (Objects.equals(newPass,newPass2)){
                user[1] = newPass;
                dbHelper.setPass(username,newPass);
            }else {
                System.out.println("Your new passwords does not match");
            }
        }else {
            System.out.println("Old pass is wrong");
        }
    }

    private static boolean logout(String username){
        if (dbHelper.checkUserExist(username)) {
            dbHelper.setLoggedOut(username);
            return true;
        } else {
            System.out.println("Error happened, no such a user");
            return false;
        }
    }

    public static void main(String[] args) {
//        password is always: 123
//        Auth.register("khalil","123","Banker", "Mr Khalil");
//        Auth.login("hasan","123");
        Auth.logout("hasan");
    }

}
