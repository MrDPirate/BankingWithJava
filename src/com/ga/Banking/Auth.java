package com.ga.Banking;

import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;


public class Auth{
    private HashMap<String, Users> users = new HashMap<>();
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
            if (Objects.equals(hashPass256(password), user[1])) {
                dbHelper.setLoggedIn(username);
                return true;
            } else {
                System.out.println("Username or password is wrong");
                return false;
            }
        } else {
            System.out.println("Username or password is wrong");
            return false;
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
