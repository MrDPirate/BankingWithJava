package com.ga.Banking;

import java.util.List;

public interface ITrans {
public void deposit(Users user, String accountType, double amount);
public void depositGlobal(Users user,String toUsername, String accountType,double amount);
public void withdraw(Users user, String accountType, double amount);
public void transferLocal(Users user, String fromAccountType,String toAccountType, double amount);
public void transfer(Users user, String username, String fromAccountType, String toAccountType);

}
