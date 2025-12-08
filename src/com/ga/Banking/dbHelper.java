package com.ga.Banking;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class dbHelper {

    static String path = "db/";

    public static void addNew(String username, String password, String role, String name, String savingAccount, String checkingAccount,
                              String savingAmount, String checkingAmount){

        if (role.equals("Banker")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Banker-"+name+"-"+username, true))) {
                writer.write(username + "," + password + "," + role +","+ "0"+"," + LocalDateTime.now() + "," + LocalDateTime.now() +","+ LocalDateTime.now()
                        +","+"No"+","+name+","+savingAccount+","+checkingAccount+","+savingAmount+","+checkingAmount);
            } catch (IOException e) {
                System.out.println("Error Creating new User: " + e.getMessage());
            }
        }
        else if (role.equals("Customer")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Customer-"+name+"-"+username, true))) {
                writer.write(username + "," + password + "," + role +","+ "0"+"," + LocalDateTime.now() + "," + LocalDateTime.now() +","+ LocalDateTime.now()
                        +","+"No"+","+name+","+savingAccount+","+checkingAccount+","+savingAmount+","+checkingAmount);
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

    public static void updateData(String[] user){

        String updatedUserData = String.join(",", user);
        String fileName = getUserFile(user[0]);

        try {
        Files.writeString(Paths.get(path+fileName), updatedUserData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Cant update user data: "+user[0]);
        }
    }

    public static String[] userTOoArray(Users users){
        return new String[]{
                users.getUsername(),
                users.getPassword(),
                users.getRole(),
                String.valueOf(users.getFailed_login_attempts()),
                String.valueOf(users.getAccount_locked_until()),
                String.valueOf(users.getCreated_at()),
                String.valueOf(users.getUpdated_at()),
                String.valueOf(users.getIsLoggedIn()),
                users.getName(),
                String.valueOf(users.getHasSavingAccount()),
                String.valueOf(users.getHasCheckingAccount()),
                String.valueOf(users.getSavingAmount()),
                String.valueOf(users.getCheckingAmount()),
                users.getSavingCard(),
                users.getCheckingCard(),
                String.valueOf(users.getOverdraftAttempts())
        };
    }

//    public static void setLoggedIn(String username){
//        String [] user = getUserData(username);
//
//        if (user[7].equals("Yes")){
//            System.out.println("User already logged in");
//        } else {
//            user[7] = "Yes";
//            updateData(user);
//            System.out.println("Logged In Successfully");
//        }
//    }
//
//    public static void setLoggedOut(String username){
//        String [] user = getUserData(username);
//
//        if (user[7].equals("No")){
//            System.out.println("User already logged out");
//        } else {
//            user[7] = "No";
//            updateData(user);
//            System.out.println("User "+username+" Has Logged Out Successfully");
//        }
//    }
//
//    public static String getLoggedInStatus(String username){
//        String [] user = getUserData(username);
//        return user[7];
//    }
//
//
//    public static String getPass(String username){
//        String [] user = getUserData(username);
//        return user[1];
//    }
//
//    public static String getRole(String username){
//        String [] user = getUserData(username);
//        return user[2];
//    }
//
//    public static String getName(String username){
//        String [] user = getUserData(username);
//        return user[8];
//    }
//
//    public static void setPass(String username, String newPass){
//        String [] user = getUserData(username);
//            user[1] = newPass;
//            updateData(user);
//    }
//
//    public static void setRole(String username, String role){
//        String [] user = getUserData(username);
//        user[2] = role;
//        updateData(user);
//        System.out.println("Updated "+username+" Role to: "+role);
//    }
//
//    public static void setName(String username, String name){
//        String [] user = getUserData(username);
//        user[8] = name;
//        updateData(user);
//        System.out.println("Updated "+username+" name to: "+name);
//    }
//
//
//
//
//
//    //TODO: implement setters and getters.
//    public static LocalDateTime getCreatedAt(String username) {
//        String[] user = getUserData(username);
//        return LocalDateTime.parse(user[5]);
//    }
//
//    public static void setUpdated_at(String username) {
//        String [] user = getUserData(username);
//        user[6] = String.valueOf(LocalDateTime.now());
//        updateData(user);
//    }
//
//    public static LocalDateTime getUpdated_at(String username) {
//        String[] user = getUserData(username);
//        return LocalDateTime.parse(user[6]);
//    }
//
//
//    public static void setAccount_locked_for_one_minute(String username) {
//        String [] user = getUserData(username);
//        user[4] = String.valueOf(LocalDateTime.now().plusMinutes(1));
//        updateData(user);
//    }
//
////    @Override
////    public void setAccount_locked_until(String username, LocalDateTime time) {
////
////    }
//
//    public static LocalDateTime getAccount_locked_until(String username) {
//        String[] user = getUserData(username);
//        return LocalDateTime.parse(user[4]);
//    }
//
//    public static void setFailed_login_attempts(String username,int attempts) {
//        String [] user = getUserData(username);
//        user[3] = String.valueOf(Integer.parseInt(user[3])+attempts);
//    }
//
//    public static void incrementFailed_login_attempts(String username) {
//        String [] user = getUserData(username);
//        user[3] = String.valueOf(Integer.parseInt(user[3])+1);
//    }
//
//    public static int getFailed_login_attempts(String username) {
//        String[]user = getUserData(username);
//        return Integer.parseInt(user[3]);
//    }





}
