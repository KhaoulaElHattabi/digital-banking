package com.emsi.mai.ebankbackend.services;

import com.emsi.mai.ebankbackend.entities.*;
import com.emsi.mai.ebankbackend.enums.OperationType;
import com.emsi.mai.ebankbackend.exceptions.BalanceNotSufficientException;
import com.emsi.mai.ebankbackend.exceptions.BankAccountNotFoundException;
import com.emsi.mai.ebankbackend.exceptions.CustomerNotFoundException;
import com.emsi.mai.ebankbackend.repositories.AccountOperationRepository;
import com.emsi.mai.ebankbackend.repositories.BankAccountRepository;
import com.emsi.mai.ebankbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@AllArgsConstructor
@Slf4j


public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    //Logger = interface pour login,  Loggerfactory =class pour creation de logger instance, getLogger= method to get logger instance, this....= get fully qualified name for class
    //Logger log =  LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new customer ");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer   customer =  customerRepository.findById(customerId).orElse(null);
            if(customer==null)
                throw new CustomerNotFoundException("Customer Not Found");
            CurrentAccount currentAccount = new CurrentAccount();
            currentAccount.setId(UUID.randomUUID().toString());
            currentAccount.setCreatedAt(Date.valueOf(LocalDate.now()));
            currentAccount.setBalance(initialBalance);
            currentAccount.setCustomer(customer);
            currentAccount.setOverDraft(overDraft);
            CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
            return savedBankAccount;

    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer   customer =  customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer Not Found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(Date.valueOf(LocalDate.now()));
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return savedBankAccount;
    }


    @Override
    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount Not Found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = getBankAccount(accountId);
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(Date.valueOf(LocalDate.now()));
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(Date.valueOf(LocalDate.now()));
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }


    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to " +accountIdDestination);
        credit(accountIdDestination, amount,"Transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }
}
