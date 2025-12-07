package com.ga.Banking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TransDB {

    static String path = "db/Trans/";

    public static void addNew(String username, String accountType, String tranType,String amount,String postBalance){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Transaction for"+"-"+username, true))) {
            writer.write(username + "," + accountType + "," + tranType +","+ amount+"," + postBalance + "," + LocalDateTime.now()+"\n");
        } catch (IOException e) {
            System.out.println("Error Creating new User: " + e.getMessage());
        }
    }

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


    public static String getUserFile(String username){
        List<String> users = getFileNamesIn_db();

        return users.stream()
                .filter(u -> u.endsWith("-"+username))
                .findFirst()
                .orElse("");
    }

    //Fetch user data in a list
    public static List<String[]> getUserData(String username){

        List<String[]> data = new ArrayList<>();
        String userFile = getUserFile(username);
        Path path1 = Paths.get(path);

        if (userFile == null) {
            System.out.println("No trans data assigned to user: " + username);
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(path1, StandardCharsets.UTF_8);
            for (String line : lines) {
                // Skip empty lines
                if (!line.trim().isEmpty()) {
                    String[] fields = line.split(",");
                    data.add(fields);
                }
            }

        } catch (IOException e) {
            System.out.println("Can't get trans data of user: "+ username);
            System.out.println(userFile);
        }

        //Guard
        if (data.isEmpty()) {
            System.out.println("Can't get trans data of user: "+ username);
            return new ArrayList<>();
        }

        return data;
    }

}
