package com.emsi.mai.ebankbackend.services;

import com.emsi.mai.ebankbackend.entities.BankAccount;
import com.emsi.mai.ebankbackend.entities.CurrentAccount;
import com.emsi.mai.ebankbackend.entities.Customer;
import com.emsi.mai.ebankbackend.entities.SavingAccount;
import com.emsi.mai.ebankbackend.exceptions.BankAccountNotFoundException;
import com.emsi.mai.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

     Customer saveCustomer(Customer customer);
     CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft , String type, Long customerId) throws CustomerNotFoundException;
     SavingAccount saveSavingBankAccount(double initialBalance, double interestRate , String type, Long customerId) throws CustomerNotFoundException;
     List<Customer> listCustomers();
     BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit(String accountId, double amount, String description) throws BankAccountNotFoundException;
     void credit(String accountId, double amount, String description);
     void transfer(String accountIdSource, String accountIdDestination);
}
