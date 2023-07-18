package com.emsi.mai.ebankbackend.services;

import com.emsi.mai.ebankbackend.dtos.*;
import com.emsi.mai.ebankbackend.entities.BankAccount;
import com.emsi.mai.ebankbackend.entities.CurrentAccount;
import com.emsi.mai.ebankbackend.entities.Customer;
import com.emsi.mai.ebankbackend.entities.SavingAccount;
import com.emsi.mai.ebankbackend.exceptions.BalanceNotSufficientException;
import com.emsi.mai.ebankbackend.exceptions.BankAccountNotFoundException;
import com.emsi.mai.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {


     CustomerDTO saveCustomer(CustomerDTO customerDTO);

     CustomerDTO updateCustomer(CustomerDTO customerDTO);

     void deleteCustomer(Long customerId);

     CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
     SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

     List<CustomerDTO> listCustomers();
     BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
     void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
     void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
     List<BankAccountDTO> bankAccountList();

     CustomerDTO getCustomer(Long customerId);

     List<AccountOperationDTO> accountHistory(String accountId);

     AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
