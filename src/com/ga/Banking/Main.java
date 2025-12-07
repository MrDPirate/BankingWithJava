package com.ga.Banking;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
    Users khalil = new Users(dbHelper.getUserData("khalil"));
    Users hasan = new Users(dbHelper.getUserData("hasan"));
        dbHelper.updateData(dbHelper.userTOoArray(khalil));
//        System.out.println(Arrays.toString(dbHelper.userTOoArray(khalil)));
        System.out.println(Users.getUsername());
    }

}
