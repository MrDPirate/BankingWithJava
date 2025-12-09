package com.ga.Banking;

import java.util.Arrays;

public class Main {


    public static void main(String[] args) {
//    Users khalil = new Users(dbHelper.getUserData("khalil"));
//    Users hasan = new Users(dbHelper.getUserData("hasan"));
//        dbHelper.updateData(dbHelper.userTOoArray(khalil));
//        System.out.println(Arrays.toString(dbHelper.userTOoArray(khalil)));


//        password is always: 123
        Auth.register("hasan","123","Banker", "Mr Khalil","true","false","2000","-100","Mastercard","Mastercard Platinum","0");
        Auth.register("khalil","123","Banker", "Mr Khalil","yes","no","0","20","Mastercard titanium","Mastercard","0");
//        Auth.login("hasan","123");
//        Auth.logout("hasan");
        Auth khalil = new Auth(dbHelper.getUserData("khalil"));
        Auth hasan = new Auth(dbHelper.getUserData("hasan"));

        hasan.setIsLoggedIn();

        System.out.println(khalil);
        System.out.println(hasan.getUsername());
    }

}
