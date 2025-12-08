package com.ga.Banking;


import java.time.LocalDateTime;

public class Users {
    private  String username;
    private  String password;
    private  String role;
    private  int failed_login_attempts;
    private  LocalDateTime account_locked_until;
    private  LocalDateTime created_at;
    private  LocalDateTime updated_at;
    private  boolean isLoggedIn = false;
    private  String name;
    private  boolean hasSavingAccount;
    private  boolean hasCheckingAccount;
    private  double savingAmount;
    private  double checkingAmount;
    private Card savingCard;
    private Card checkingCard;
    private int overdraftAttempts;

    public Users(String[] user) {
        username = user[0];
        password = user[1];
        role = user[2];
        failed_login_attempts = Integer.parseInt(user[3]);
        account_locked_until= LocalDateTime.parse(user[4]);
        created_at = LocalDateTime.parse(user[5]);
        updated_at = LocalDateTime.parse(user[6]);
        isLoggedIn = Boolean.parseBoolean(user[7]);
        name = user [8];
        hasSavingAccount= Boolean.parseBoolean(user[9]);
        hasCheckingAccount= Boolean.parseBoolean(user[10]);

        if (hasSavingAccount){
            savingAmount= Double.parseDouble(user[11]);
            savingCard=new Card(user[13]);
        }
        if (hasCheckingAccount) {
            checkingAmount= Double.parseDouble(user[12]);
            checkingCard = new Card(user[14]);
        }
        overdraftAttempts= Integer.parseInt(user[15]);
    }

    public int getOverdraftAttempts() {
        return overdraftAttempts;
    }

    public void setOverdraftAttempts(int overdraftAttempts) {
        this.overdraftAttempts = overdraftAttempts;
    }

    public String getCheckingCard() {
        return checkingCard.getCardType();
    }

    public void setCheckingCard(String card) {
        this.checkingCard = new Card(card);
    }

    public String getSavingCard() {
        return savingCard.getCardType();
    }

    public Card getCheckCard() {
        return checkingCard;
    }

    public Card getSaveCard() {
        return savingCard;
    }

    public void setSavingCard(String card) {
        this.savingCard = new Card(card);
    }

    public boolean getHasSavingAccount() {
        return hasSavingAccount;
    }

    public void setHasSavingAccount(boolean hasSavingAccount) {
        this.hasSavingAccount = hasSavingAccount;
    }

    public boolean getHasCheckingAccount() {
        return hasCheckingAccount;
    }

    public void setHasCheckingAccount(boolean hasCheckingAccount) {
        this.hasCheckingAccount = hasCheckingAccount;
    }

    public double getSavingAmount() {
        return savingAmount;
    }

    public void setSavingAmount(double savingAmount) {
        this.savingAmount = savingAmount;
    }

    public double getCheckingAmount() {
        return checkingAmount;
    }

    public void setCheckingAmount(double checkingAmount) {
        this.checkingAmount = checkingAmount;
    }



    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public  String getPassword() {
        return password;
    }

    public  void setPassword(String password) {
        this.password = password;
    }

    public  String getRole() {
        return role;
    }

    public  void setRole(String role) {
        this.role = role;
    }

    public  int getFailed_login_attempts() {
        return failed_login_attempts;
    }

    public  void setFailed_login_attempts(int failed_login_attempts) {
        this.failed_login_attempts = failed_login_attempts;
    }

    public  LocalDateTime getAccount_locked_until() {
        return account_locked_until;
    }

    public  void setAccount_locked_until(LocalDateTime account_locked_until) {
        this.account_locked_until = account_locked_until;
    }

    public  LocalDateTime getCreated_at() {
        return created_at;
    }

    public  void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public  LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public  void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn() {
        this.isLoggedIn = true;
    }

    public void unsetIsLoggedIn(){
        this.isLoggedIn=false;
    }

    public void setAccount_locked_for_one_minute(){
        account_locked_until=LocalDateTime.now().plusMinutes(1);
    }

    public void incrementFailed_login_attempts(){
        this.failed_login_attempts++;
    }

    @Override
    public String toString() {
        return "Users{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", failed_login_attempts=" + failed_login_attempts +
                ", account_locked_until=" + account_locked_until +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", isLoggedIn=" + isLoggedIn +
                ", name='" + name + '\'' +
                ", hasSavingAccount=" + hasSavingAccount +
                ", hasCheckingAccount=" + hasCheckingAccount +
                ", savingAmount=" + savingAmount +
                ", checkingAmount=" + checkingAmount +
                '}';
    }
}
