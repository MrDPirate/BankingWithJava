package com.ga.Banking;

import java.time.LocalDateTime;
import java.util.List;

public class Card{
    private final String username;
    private final String cardType;
    private final String accountType;
    private double dailyWithdrawLimit;
    private double dailyTransLimit;
    private double dailyTransLocalLimit;
    private double dailyDepositLimit;
    private double dailyDepositLocalLimit;

    public Card(String cardType,String accountType,String username) {
        this.cardType = cardType;
        this.accountType = accountType;
        this.username = username;
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


    public String getAccountType() {
        return accountType;
    }

    public String getUsername() {
        return username;
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


    public double getDailyWithdrawLimit() {
        return calculateRemainingLimit(dailyWithdrawLimit, accountType, "withdraw");
    }

    public double getDailyTransLimit() {
        return calculateRemainingLimit(dailyTransLimit, accountType, "transfer out");
    }

    public double getDailyTransLocalLimit() {
        return calculateRemainingLimit(dailyTransLocalLimit, accountType, "transfer local");
    }

    public double getDailyDepositLimit() {
        return calculateRemainingLimit(dailyDepositLimit, accountType, "deposit own", "deposit to other");
    }

    public double getDailyDepositLocalLimit() {
        return calculateRemainingLimit(dailyDepositLocalLimit, accountType, "deposit own");
    }

    private double calculateRemainingLimit(double baseLimit, String accountType, String... tranTypes) {
        List<String[]> today = TransDB.filterByDay(username, LocalDateTime.now());
        if (today == null || today.isEmpty()) {
            return baseLimit;
        }

        double used = 0;
        for (String[] record : today) {
            if (record.length < 6) {
                continue;
            }
            String recordAccount = record[1];
            String recordType = record[2];
            String amountRaw = record[3];

            if (!recordAccount.equalsIgnoreCase(accountType)) {
                continue;
            }
            if (!TransDB.matchesTranType(recordType, tranTypes)) {
                continue;
            }

            used += Math.abs(parseAmount(amountRaw));
        }

        double remaining = baseLimit - used;
        return remaining > 0 ? remaining : 0;
    }

    private double parseAmount(String raw) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}