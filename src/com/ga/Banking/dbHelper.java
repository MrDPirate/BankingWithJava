package com.ga.Banking;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class dbHelper {

    static String path = "db/";

    public static void addNew(String username, String password, String role, String name){
        if (role.equals("Banker")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Banker-"+name+"-"+username, true))) {
                writer.write(username + ", " + password + ", " + role + "0" + LocalDateTime.now() + ", " + LocalDateTime.now() +", "+ LocalDateTime.now());
            } catch (IOException e) {
                System.out.println("Error Creating new User: " + e.getMessage());
            }
        } else if (role.equals("Customer")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Customer-"+name+"-"+username, true))) {
                writer.write(username + ", " + password + ", " + role + "0" + LocalDateTime.now() + ", " + LocalDateTime.now() +", "+ LocalDateTime.now());
            } catch (IOException e) {
                System.out.println("Error Creating new User: " + e.getMessage());
            }
        }
        else {
            System.out.println("Please provide a valid info");
        }
    }

    //TODO: implement the function of getting the users by username

    static Path pathP;
    public static boolean checkUserExist(String username){
        pathP = Paths.get("-"+username);
        try (BufferedReader reader = Files.newBufferedReader(pathP)){
            System.out.println("exist");
            return true;
        } catch (IOException e) {
            System.out.println("not");
            return false;
        }
    }
}
