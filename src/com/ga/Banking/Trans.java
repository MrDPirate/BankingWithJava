package com.ga.Banking;


public class Trans implements ITrans{
    @Override
    public void deposit(Users user, Card card,double amount) {
        boolean locked = false;

        String accountType = card.getAccountType();
        if (user.getOverdraftAttempts()>=2){
            locked = true;
        }

        if (!user.getIsLoggedIn()||amount<=0){
            System.out.println("Can't deposit");
            return;
        }

        if (accountType.equalsIgnoreCase("Saving")){
            if (amount>user.getSaveCard().getDailyDepositLocalLimit()){
                return;
            }
            user.getSaveCard().setDailyDepositLocalLimit(user.getSaveCard().getDailyDepositLocalLimit()-amount);
            user.setSavingAmount(user.getSavingAmount()+amount);
            if (locked && user.getSavingAmount()>=0){
                user.setOverdraftAttempts(0);
            }
            dbHelper.updateData(dbHelper.userTOoArray(user));
            TransDB.addNew(user,"saving","deposit own", String.valueOf(amount));
            System.out.println(amount+" deposit successfully to " + user.getName() +"in saving account");

        }else if (accountType.equalsIgnoreCase("Checking")){
            if (amount>user.getCheckCard().getDailyDepositLocalLimit()){
                return;
            }
            user.getCheckCard().setDailyDepositLocalLimit(user.getCheckCard().getDailyDepositLocalLimit()-amount);
            user.setCheckingAmount(user.getCheckingAmount()+amount);
            if (locked && user.getSavingAmount()>=0){
                user.setOverdraftAttempts(0);
            }
            dbHelper.updateData(dbHelper.userTOoArray(user));
            TransDB.addNew(user,"checking","deposit own", String.valueOf(amount));
            System.out.println(amount+" deposit successfully to " + user.getName() +"in checking account");

        }else {
            System.out.println("Please provide a valid account type, saving or checking");
        }
    }

//    @Override
//    public void depositGlobal(Users user, String toUsername, Card card, double amount) {
//        String accountType = card.getAccountType();
//
//        if (!user.getIsLoggedIn()||amount<=0){
//            System.out.println("Can't deposit");
//            return;
//        }
//
//        if (accountType.equalsIgnoreCase("Checking")||accountType.equalsIgnoreCase("Saving")){
//            Users toUser = new Users(dbHelper.getUserData(toUsername));
//
//            if (accountType.equalsIgnoreCase("Saving")){
//                if (amount>user.getSaveCard().getDailyDepositLimit()||amount>toUser.getSaveCard().getDailyDepositLimit()){
//                    return;
//                }
//
//                user.getSaveCard().setDailyDepositLimit(user.getSaveCard().getDailyDepositLimit()-amount);
//                toUser.getSaveCard().setDailyDepositLimit(toUser.getSaveCard().getDailyDepositLimit()-amount);
//                toUser.setSavingAmount(toUser.getSavingAmount()+amount);
//            }else {
//                if (amount>user.getCheckCard().getDailyDepositLimit()||amount>toUser.getCheckCard().getDailyDepositLimit()){
//                    return;
//                }
//
//                user.getCheckCard().setDailyDepositLimit(user.getCheckCard().getDailyDepositLimit()-amount);
//                toUser.getCheckCard().setDailyDepositLimit(toUser.getCheckCard().getDailyDepositLimit()-amount);
//                toUser.setCheckingAmount(toUser.getCheckingAmount()+amount);
//            }
//
//            TransDB.addNew(toUser,accountType,"deposit from other", String.valueOf(amount));
//            TransDB.addNew(user,accountType,"deposit to other", String.valueOf(amount));
//            dbHelper.updateData(dbHelper.userTOoArray(toUser));
//            System.out.println(amount+" deposit successfully to " + toUser.getName() +"in "+accountType+" account");
//
//        }else {
//            System.out.println("Please provide a valid account type, saving or checking");
//        }
//
//    }

    @Override
    public void withdraw(Users user, Card card, double amount) {
        String accountType = card.getAccountType();
        if (!user.getIsLoggedIn()||user.getOverdraftAttempts()>=2||amount<=0){
            System.out.println("Can't withdraw");
            return;
        }

        if (user.getOverdraftAttempts()== 1 && amount>100){
            System.out.println("Can't withdraw");
            return;
        }

        if (accountType.equalsIgnoreCase("Checking")||accountType.equalsIgnoreCase("Saving")){
            if (accountType.equalsIgnoreCase("Saving")){
                if (amount>user.getSaveCard().getDailyWithdrawLimit()){
                    return;
                }
                user.setSavingAmount(user.getSavingAmount()-amount);
                if (user.getSavingAmount()<0){
                    user.setOverdraftAttempts(user.getOverdraftAttempts()+1);
                    user.setSavingAmount(user.getSavingAmount()-35);
                    TransDB.addNew(user,accountType,"overdraft charge", "35");
                    System.out.println("Overdraft Triggered, you are charged 35 dollar, please pay your bill");

                }
                user.getSaveCard().setDailyWithdrawLimit(user.getSaveCard().getDailyWithdrawLimit()-amount);

            }else {
                if (amount>user.getCheckCard().getDailyWithdrawLimit()){
                    return;
                }
                user.setCheckingAmount(user.getCheckingAmount()-amount);
                if (user.getCheckingAmount()<0){
                    user.setOverdraftAttempts(user.getOverdraftAttempts()+1);
                    user.setCheckingAmount(user.getCheckingAmount()-35);
                    TransDB.addNew(user,accountType,"overdraft charge", "35");
                    System.out.println("Overdraft Triggered, you are charged 35 dollar, please pay your bill");

                }
                user.getCheckCard().setDailyWithdrawLimit(user.getCheckCard().getDailyWithdrawLimit()-amount);
            }
            TransDB.addNew(user,accountType,"withdraw", String.valueOf(amount));
            dbHelper.updateData(dbHelper.userTOoArray(user));
            System.out.println(amount+" withdraw successfully from " + user.getName() +"in "+accountType+" account");
        }else {
            System.out.println("Please provide a valid account type, saving or checking");
        }
    }

    @Override
    public void transferLocal(Users user, Card card, String toAccountType, double amount) {
        String accountType = card.getAccountType();
        if (!user.getIsLoggedIn()||user.getOverdraftAttempts()>=2||amount<=0){
            System.out.println("Can't transfer");
            return;
        }

        if (user.getOverdraftAttempts()== 1 && amount>100){
            System.out.println("Can't transfer");
            return;
        }

        if (accountType.equalsIgnoreCase("Checking")||accountType.equalsIgnoreCase("Saving")){
            if (accountType.equalsIgnoreCase("Saving")){
                if (amount>user.getSaveCard().getDailyTransLocalLimit()){
                    return;
                }
                user.setSavingAmount(user.getSavingAmount()-amount);
                user.setCheckingAmount(user.getCheckingAmount()+amount);

                if (user.getSavingAmount()<0){
                    user.setOverdraftAttempts(user.getOverdraftAttempts()+1);
                    user.setSavingAmount(user.getSavingAmount()-35);
                    TransDB.addNew(user,accountType,"overdraft charge", "35");
                    System.out.println("Overdraft Triggered, you are charged 35 dollar, please pay your bill");

                }
                user.getSaveCard().setDailyTransLocalLimit(user.getSaveCard().getDailyTransLocalLimit()-amount);


            } else {
                if (amount>user.getCheckCard().getDailyTransLocalLimit()){
                    return;
                }
                user.setSavingAmount(user.getSavingAmount()+amount);
                user.setCheckingAmount(user.getCheckingAmount()-amount);

                if (user.getCheckingAmount()<0){
                    user.setOverdraftAttempts(user.getOverdraftAttempts()+1);
                    user.setCheckingAmount(user.getCheckingAmount()-35);
                    TransDB.addNew(user,accountType,"overdraft charge", "35");
                    System.out.println("Overdraft Triggered, you are charged 35 dollar, please pay your bill");

                }
                user.getCheckCard().setDailyTransLocalLimit(user.getCheckCard().getDailyTransLocalLimit()-amount);

            }
            TransDB.addNew(user,accountType,"transfer local", String.valueOf(amount));
            dbHelper.updateData(dbHelper.userTOoArray(user));
            System.out.println(amount+" local transferred successfully for " + user.getName() +"from "+accountType+" to the other account" );
        }else {
            System.out.println("Please provide a valid account type, saving or checking");
        }
    }

    @Override
    public void transfer(Users user, String username, Card card, double amount) {
        String accountType = card.getAccountType();
        if (!user.getIsLoggedIn()||user.getOverdraftAttempts()>=2||amount<=0){
            System.out.println("Can't transfer");
            return;
        }

        if (user.getOverdraftAttempts()== 1 && amount>100){
            System.out.println("Can't transfer");
            return;
        }

        if (accountType.equalsIgnoreCase("Checking")||accountType.equalsIgnoreCase("Saving")) {
            Users toUser = new Users(dbHelper.getUserData(username));

            if (accountType.equalsIgnoreCase("Saving")) {
                if (amount > user.getSaveCard().getDailyTransLimit()) {
                    return;
                }
                user.setSavingAmount(user.getSavingAmount() - amount);
                toUser.setSavingAmount(toUser.getSavingAmount() + amount);

                if (user.getSavingAmount() < 0) {
                    user.setOverdraftAttempts(user.getOverdraftAttempts() + 1);
                    user.setSavingAmount(user.getSavingAmount() - 35);
                    TransDB.addNew(user, accountType, "overdraft charge", "35");
                    System.out.println("Overdraft Triggered, you are charged 35 dollar, please pay your bill");

                }
                user.getSaveCard().setDailyTransLimit(user.getSaveCard().getDailyTransLimit() - amount);
            } else {
                if (amount > user.getCheckCard().getDailyTransLimit()) {
                    return;
                }

                //transfer to the same account type
                user.setCheckingAmount(user.getCheckingAmount() - amount);
                toUser.setCheckingAmount(toUser.getCheckingAmount() + amount);

                if (user.getCheckingAmount() < 0) {
                    user.setOverdraftAttempts(user.getOverdraftAttempts() + 1);
                    user.setCheckingAmount(user.getCheckingAmount() - 35);
                    TransDB.addNew(user, accountType, "overdraft charge", "35");
                    System.out.println("Overdraft Triggered, you are charged 35 dollar, please pay your bill");

                }
                user.getCheckCard().setDailyTransLimit(user.getCheckCard().getDailyTransLimit() - amount);
            }            TransDB.addNew(user,accountType,"transfer", String.valueOf(amount));
            dbHelper.updateData(dbHelper.userTOoArray(user));
            System.out.println(amount+" transferred successfully from " + user.getName() +" account "+accountType+" to "+toUser.getName() +" account " +accountType);
        }else {
            System.out.println("Please provide a valid account type, saving or checking");
        }
    }

}
