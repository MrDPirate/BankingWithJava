package com.ga.Banking;

public class Card{

    private String cardType;
    private double dailyWithdrawLimit;
    private double dailyTransLimit;
    private double dailyTransLocalLimit;
    private double dailyDepositLimit;
    private double dailyDepositLocalLimit;

    public Card(String cardType) {
        this.cardType = cardType;
        if (cardType.equalsIgnoreCase("Mastercard Platinum")){
            this.dailyWithdrawLimit = 20000;
            this.dailyTransLimit = 40000;
            this.dailyTransLocalLimit = 80000;
            this.dailyDepositLimit = 100_000;
            this.dailyDepositLocalLimit = 200_000;

        } else if (cardType.equalsIgnoreCase("Mastercard Titanium")) {
            this.dailyWithdrawLimit = 10000;
            this.dailyTransLimit = 20000;
            this.dailyTransLocalLimit = 40000;
            this.dailyDepositLimit = 100_000;
            this.dailyDepositLocalLimit = 200_000;

        } else if (cardType.equalsIgnoreCase("Mastercard")) {
            this.dailyWithdrawLimit = 5000;
            this.dailyTransLimit = 10000;
            this.dailyTransLocalLimit = 20000;
            this.dailyDepositLimit = 100_000;
            this.dailyDepositLocalLimit = 200_000;

        }else {
            System.out.println("Please provide a valid card type");
        }
    }

    public void setDailyWithdrawLimit(double dailyWithdrawLimit) {
        this.dailyWithdrawLimit = dailyWithdrawLimit;
    }

    public void setDailyTransLimit(double dailyTransLimit) {
        this.dailyTransLimit = dailyTransLimit;
    }

    public void setDailyTransLocalLimit(double dailyTransLocalLimit) {
        this.dailyTransLocalLimit = dailyTransLocalLimit;
    }

    public void setDailyDepositLimit(double dailyDepositLimit) {
        this.dailyDepositLimit = dailyDepositLimit;
    }

    public void setDailyDepositLocalLimit(double dailyDepositLocalLimit) {
        this.dailyDepositLocalLimit = dailyDepositLocalLimit;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public double getDailyWithdrawLimit() {
        return dailyWithdrawLimit;
    }

    public double getDailyTransLimit() {
        return dailyTransLimit;
    }

    public double getDailyTransLocalLimit() {
        return dailyTransLocalLimit;
    }

    public double getDailyDepositLimit() {
        return dailyDepositLimit;
    }

    public double getDailyDepositLocalLimit() {
        return dailyDepositLocalLimit;
    }
}