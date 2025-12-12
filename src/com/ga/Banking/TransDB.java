package com.ga.Banking;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TransDB {

    static String path = "db/Trans/";

    public static void addNew(Users users, String accountType, String tranType,String amount){
        String postBalance;
        if (accountType.equalsIgnoreCase("saving")){
            postBalance= String.valueOf(users.getSavingAmount());

        }else{
            postBalance= String.valueOf(users.getCheckingAmount());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path+"Transaction for"+"-"+users.getUsername(), true))) {
            writer.write(users.getUsername() + "," + accountType + "," + tranType +","+ amount+"," + postBalance + "," + LocalDateTime.now()+"\n");
            System.out.println("Transaction done");
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
        Path path1 = Paths.get(path+userFile);

        //Guard
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
            System.out.println("Can't get trans data of user: "+ username+" empty");
            return new ArrayList<>();
        }

        return data;
    }


    //Filtering Transaction:

    //filter by a day
    public static List<String[]> filterByDay(String username,LocalDateTime dateTime){
        List<String[]> data = getUserData(username);
        LocalDate localDate = dateTime.toLocalDate();
        return data.stream().filter(t-> {
            String dataBaseDateTime = t[5];
            try {
                LocalDateTime transactionDateTime = LocalDateTime.parse(dataBaseDateTime);
                return transactionDateTime.toLocalDate().isEqual(localDate);

            }catch (DateTimeParseException | IndexOutOfBoundsException e){
                System.out.println("error while parsing date for: "+Arrays.toString(t));
                return false;
            }
                }
                ).toList();
    }

    //filter by last day
    public static List<String[]> filterByYesterday(String username){
        List<String[]> data = getUserData(username);
        LocalDate localDate = LocalDate.now();
        return data.stream().filter(t-> {
                    String dataBaseDateTime = t[5];
                    try {
                        LocalDateTime transactionDateTime = LocalDateTime.parse(dataBaseDateTime);
                        return transactionDateTime.toLocalDate().isEqual(localDate.minusDays(1));

                    }catch (DateTimeParseException | IndexOutOfBoundsException e){
                        System.out.println("error while parsing date for: "+Arrays.toString(t));
                        return false;
                    }
                }
        ).toList();
    }

    //filter by last week
    public static List<String[]> filterByLastWeek(String username){
        List<String[]> data = getUserData(username);
        LocalDate localDate = LocalDate.now().minusWeeks(1);
        return data.stream().filter(t-> {
                    String dataBaseDateTime = t[5];
                    try {
                        LocalDateTime transactionDateTime = LocalDateTime.parse(dataBaseDateTime);
                        return transactionDateTime.toLocalDate().isEqual(localDate);

                    }catch (DateTimeParseException | IndexOutOfBoundsException e){
                        System.out.println("error while parsing date for: "+Arrays.toString(t));
                        return false;
                    }
                }
        ).toList();
    }

    //filter custom days
    public static List<String[]> filterByCustomDays(String username,LocalDateTime fromDateTime, LocalDateTime toDateTime){
        List<String[]> data = getUserData(username);
        LocalDate FromLocalDate = fromDateTime.toLocalDate();
        LocalDate toLocalDate = toDateTime.toLocalDate();
        return data.stream().filter(t-> {
                    String dataBaseDateTime = t[5];
                    try {
                        LocalDateTime transactionDateTime = LocalDateTime.parse(dataBaseDateTime);
                        return transactionDateTime.toLocalDate().isAfter(FromLocalDate)&&transactionDateTime.toLocalDate().isBefore(toLocalDate);

                    }catch (DateTimeParseException | IndexOutOfBoundsException e){
                        System.out.println("error while parsing date for: "+Arrays.toString(t));
                        return false;
                    }
                }
        ).toList();
    }

    // filter by transaction type
    public static List<String[]> filterByTranType(String username, String tranType){
        List<String[]> data = getUserData(username);
        return data.stream()
                .filter(t->t[2].equalsIgnoreCase(tranType))
                .toList();
    }

    //filter by account type
    public static List<String[]> filterByAccountType(String username, String accountType){
        List<String[]> data = getUserData(username);
        return data.stream()
                .filter(t->t[1].equalsIgnoreCase(accountType))
                .toList();
    }

    //print transactions
    public static void printTrans(List<String[]> data){
        for (String[] transactionRecord : data) {
            System.out.println(Arrays.toString(transactionRecord));
        }
    }

    public static boolean matchesTranType(String actualType, String... tranTypes){
        if (tranTypes == null || tranTypes.length == 0) {
            return true;
        }
        for (String expected : tranTypes) {
            if (actualType.equalsIgnoreCase(expected)) {
                return true;
            }
        }
        return false;
    }


//     public static void main(String[] args) {
//         Users khalil = new Users(dbHelper.getUserData("khalil"));
//        addNew(khalil,"saving","withdraw","-100");

//     }

}
