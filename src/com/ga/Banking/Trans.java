package com.ga.Banking;


public class Trans implements ITrans{
    @Override
    public void deposit(Users user, String accountType,double amount) {
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
            dbHelper.updateData(dbHelper.userTOoArray(user));
            TransDB.addNew(user,"saving","deposit own", String.valueOf(amount));
            System.out.println(amount+" deposit successfully to " + user.getName() +"in saving account");

        }else if (accountType.equalsIgnoreCase("Checking")){
            if (amount>user.getCheckCard().getDailyDepositLocalLimit()){
                return;
            }
            user.getCheckCard().setDailyDepositLocalLimit(user.getCheckCard().getDailyDepositLocalLimit()-amount);
            user.setCheckingAmount(user.getCheckingAmount()+amount);
            dbHelper.updateData(dbHelper.userTOoArray(user));
            TransDB.addNew(user,"checking","deposit own", String.valueOf(amount));
            System.out.println(amount+" deposit successfully to " + user.getName() +"in checking account");

        }else {
            System.out.println("Please provide a valid account type, saving or checking");
        }
    }

    @Override
    public void depositGlobal(Users user, String toUsername, String accountType, double amount) {
        if (!user.getIsLoggedIn()||amount<=0){
            System.out.println("Can't deposit");
            return;
        }

        if (accountType.equalsIgnoreCase("Checking")||accountType.equalsIgnoreCase("Saving")){
            Users toUser = new Users(dbHelper.getUserData(toUsername));

            if (accountType.equalsIgnoreCase("Saving")){
                if (amount>user.getSaveCard().getDailyDepositLimit()||amount>toUser.getSaveCard().getDailyDepositLimit()){
                    return;
                }

                user.getSaveCard().setDailyDepositLimit(user.getSaveCard().getDailyDepositLimit()-amount);
                toUser.getSaveCard().setDailyDepositLimit(toUser.getSaveCard().getDailyDepositLimit()-amount);
                toUser.setSavingAmount(toUser.getSavingAmount()+amount);
            }else {
                if (amount>user.getCheckCard().getDailyDepositLimit()||amount>toUser.getCheckCard().getDailyDepositLimit()){
                    return;
                }

                user.getCheckCard().setDailyDepositLimit(user.getCheckCard().getDailyDepositLimit()-amount);
                toUser.getCheckCard().setDailyDepositLimit(toUser.getCheckCard().getDailyDepositLimit()-amount);
                toUser.setCheckingAmount(toUser.getCheckingAmount()+amount);
            }

            TransDB.addNew(toUser,accountType,"deposit from other", String.valueOf(amount));
            TransDB.addNew(user,accountType,"deposit to other", String.valueOf(amount));
            dbHelper.updateData(dbHelper.userTOoArray(toUser));
            System.out.println(amount+" deposit successfully to " + toUser.getName() +"in "+accountType+" account");

        }else {
            System.out.println("Please provide a valid account type, saving or checking");
        }

    }

    @Override
    public void withdraw(Users user, String accountType, double amount) {
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

                }
                user.getSaveCard().setDailyWithdrawLimit(user.getSaveCard().getDailyWithdrawLimit()-amount);

            }else {
                user.setCheckingAmount(user.getCheckingAmount()-amount);
                if (user.getCheckingAmount()<0){
                    user.setOverdraftAttempts(user.getOverdraftAttempts()+1);
                    user.setCheckingAmount(user.getCheckingAmount()-35);
                    TransDB.addNew(user,accountType,"overdraft charge", "35");

                }
                user.getCheckCard().setDailyWithdrawLimit(user.getCheckCard().getDailyWithdrawLimit()-amount);
            }
            TransDB.addNew(user,accountType,"withdraw", String.valueOf(amount));
            dbHelper.updateData(dbHelper.userTOoArray(user));
            System.out.println(amount+" deposit successfully to " + user.getName() +"in "+accountType+" account");
        }else {
            System.out.println("Please provide a valid account type, saving or checking");
        }
    }

    @Override
    public void transferLocal(Users user, String fromAccountType, String toAccountType, double amount) {
        if (!user.getIsLoggedIn()||user.getOverdraftAttempts()>=2||amount<=0){
            System.out.println("Can't withdraw");
            return;
        }

        if (user.getOverdraftAttempts()== 1 && amount>100){
            System.out.println("Can't withdraw");
            return;
        }
    }

    @Override
    public void transfer(Users user, String username, String fromAccountType, String toAccountType) {

    }

    public void payOverdraft(Users user, String accountType,double amount){

    }
}
