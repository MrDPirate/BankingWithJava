package com.ga.Banking;

import java.util.List;

public interface ITrans {
public void deposit(Users user, Card card, double amount);
//public void depositGlobal(Users user,String toUsername, Card card,double amount);
public void withdraw(Users user, Card card, double amount);
public void transferLocal(Users user, Card card,String toAccountType, double amount);
public void transfer(Users user, String username, Card card, String toAccountType, double amount);

}
