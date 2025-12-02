package com.ga.Banking;

import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


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

    private void register(String username, String password, String role, long id){
        String hashed_password = hashPass256(password);
        users.put(username,new Users(username, hashed_password, role, id));
    }

    private boolean login(String username, String password) {
        if (users.containsKey(username)) {
            return users.get(username).getPassword().equals(hashPass256(password));
        } else return false;
    }

    public static void main(String[] args) {
        Auth test = new Auth();
        test.register("dfds","123","fds",2342);
        System.out.println(test.users.get("dfds").getPassword());

    }

}
