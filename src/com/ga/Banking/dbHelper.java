package com.ga.Banking;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class dbHelper {

    static String path = "db/";

    public static void addNew(String username, String password, String role, String name){

        if (role.equals("Banker")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Banker-"+name+"-"+username, true))) {
                writer.write(username + "," + password + "," + role +","+ "0"+"," + LocalDateTime.now() + "," + LocalDateTime.now() +","+ LocalDateTime.now()+","+"No");
            } catch (IOException e) {
                System.out.println("Error Creating new User: " + e.getMessage());
            }
        }
        else if (role.equals("Customer")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Customer-"+name+"-"+username, true))) {
                writer.write(username + "," + password + "," + role +","+ "0"+"," + LocalDateTime.now() + "," + LocalDateTime.now() +","+ LocalDateTime.now()+","+"No");
            } catch (IOException e) {
                System.out.println("Error Creating new User: " + e.getMessage());
            }
        }
        else {
            System.out.println("Please provide a valid info");
        }
    }

    //Get the file names of the users
    public static List<String> getFileNamesIn_db() {

        Path dir = Paths.get(path);
        List<String> fileNames = null;

        try (Stream<Path> paths = Files.list(dir)) {
            fileNames = paths
                    .filter(Files::isRegularFile)// filter sub dir
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .toList();

        } catch (IOException e) {
            System.out.println("Error getting files: " + e.getMessage());
        }

        return fileNames;
    }

    // Check if the user exist
    public static boolean checkUserExist(String username){
        List<String> files = getFileNamesIn_db();
        return files.stream()
                .anyMatch(f -> f.endsWith("-"+username));
    }

    public static String getUserFile(String username){
        List<String> users = getFileNamesIn_db();

        return users.stream()
                .filter(u -> u.endsWith("-"+username))
                .findFirst()
                .orElse("");
    }

    //Fetch user data in a list
    public static String[] getUserData(String username){

        String data = "";
        String userFile = getUserFile(username);

        try {
            data =  Files.readString(Paths.get(path + userFile), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Can't get data of user: "+ username);
            System.out.println(userFile);
        }

        //Guard
        if (data.isEmpty()) {
            System.out.println("Can't get data of user: "+ username);
            return new String[0];
        }

        return data.split(",");
    }

    public static void updateDate(String[] user){

        String updatedUserData = String.join(",", user);
        String fileName = getUserFile(user[0]);

        try {
        Files.writeString(Paths.get(path+fileName), updatedUserData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Cant update user data: "+user[0]);
        }
    }

    public static void setLoggedIn(String username){
        String [] user = getUserData(username);

        if (user[7].equals("Yes")){
            System.out.println("User already logged in");
        } else {
            user[7] = "Yes";
            updateDate(user);
            System.out.println("Logged In Successfully");
        }
    }

    public static void setLoggedOut(String username){
        String [] user = getUserData(username);

        if (user[7].equals("No")){
            System.out.println("User already logged out");
        } else {
            user[7] = "No";
            updateDate(user);
            System.out.println("User "+username+" Has Logged Out Successfully");
        }
    }

    public static String getLoggedInStatus(String username){
        String [] user = getUserData(username);
        return user[7];
    }

}
