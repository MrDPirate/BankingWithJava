package com.ga.Banking;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;


public class Auth extends Users{


    public Auth(String[] user) {
        super(user);
    }

    public static String hashPass256(String textToHash) {
        try {
            // Get an instance of the SHA-256 MessageDigest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input string to bytes using UTF-8 encoding
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);

            // Compute the hash
            byte[] hashedByteArray = digest.digest(byteOfTextToHash);

            // Encode the resulting byte array to a Base64 string for easy representation
            return Base64.getEncoder().encodeToString(hashedByteArray);

        } catch (NoSuchAlgorithmException e) {
            // Handle the case where SHA-256 algorithm is not available
            e.printStackTrace();
            return null;
        }
    }

    public static void register(String username, String password, String role,String name, String savingAccount, String checkingAccount,String savingAmount, String checkingAmount, String saving, String checking, String overdraftAttempts){
        String hashed_password = hashPass256(password);
        if (dbHelper.checkUserExist(username)){
            System.out.println("User Already exist");
            return;
        }
        System.out.println("Creating account for: "+username);
        dbHelper.addNew(username,hashed_password,role,name,savingAccount,checkingAccount,savingAmount, checkingAmount,saving,checking,overdraftAttempts);
    }

    public static boolean login(Users user,String username, String password) {
        if (dbHelper.checkUserExist(username)) {

            // guard to check if the account is locked
            if (user.getAccount_locked_until().isAfter(LocalDateTime.now())
                    && user.getFailed_login_attempts()==3){

                System.out.println("Your account is locked until: " +user.getAccount_locked_until());
                return false;
            }


            if (Objects.equals(hashPass256(password), user.getPassword())) {
                user.setIsLoggedIn();
                dbHelper.updateData(dbHelper.userTOoArray(user));
                return true;
            } else {
                System.out.println("Username or password is wrong");

                if (user.getFailed_login_attempts()==2){
                    user.setAccount_locked_for_one_minute();
                }
                user.incrementFailed_login_attempts();
                return false;
            }
        } else {
            System.out.println("Username or password is wrong - register");
            return false;
        }
    }

    public static void changePass(Users user, String oldPass, String newPass, String newPass2){
        if (!dbHelper.checkUserExist(user.getUsername())) {
            System.out.println("No data for the user, register first");
            return;
        }
            if (!user.getIsLoggedIn()){
            System.out.println("Login First");
            return;
        }
        if (Objects.equals(Auth.hashPass256(oldPass), oldPass)){
            if (Objects.equals(newPass,newPass2)){
                user.setPassword(newPass);
                dbHelper.updateData(dbHelper.userTOoArray(user));
                System.out.println("Password changed successfully");
            }else {
                System.out.println("Your new passwords does not match");
            }
        }else {
            System.out.println("Old pass is wrong");
        }
    }

    public static boolean logout(Users user){
        if (dbHelper.checkUserExist(user.getUsername())) {
            user.unsetIsLoggedIn();
            dbHelper.updateData(dbHelper.userTOoArray(user));
            return true;
        } else {
            System.out.println("Error happened, no such a user");
            return false;
        }
    }

    public static void main(String[] args) {
//        password is always: 123
//        Auth.register("hasan","123","Banker", "Mr Khalil","yes","no","2000","-100");
//        Auth.register("khalil","123","Banker", "Mr Khalil","yes","no","0","20");
//        Auth.login("hasan","123");
//        Auth.logout("hasan");
    }

}
