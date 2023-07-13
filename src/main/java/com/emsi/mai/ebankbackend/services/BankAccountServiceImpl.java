package com.emsi.mai.ebankbackend.services;

import com.emsi.mai.ebankbackend.dtos.BankAccountDTO;
import com.emsi.mai.ebankbackend.dtos.CurrentBankAccountDTO;
import com.emsi.mai.ebankbackend.dtos.CustomerDTO;
import com.emsi.mai.ebankbackend.dtos.SavingBankAccountDTO;
import com.emsi.mai.ebankbackend.entities.*;
import com.emsi.mai.ebankbackend.enums.OperationType;
import com.emsi.mai.ebankbackend.exceptions.BalanceNotSufficientException;
import com.emsi.mai.ebankbackend.exceptions.BankAccountNotFoundException;
import com.emsi.mai.ebankbackend.exceptions.CustomerNotFoundException;
import com.emsi.mai.ebankbackend.mappers.BankAccountMapperImpl;
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
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j


public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;
    //Logger = interface pour login,  Loggerfactory =class pour creation de logger instance, getLogger= method to get logger instance, this....= get fully qualified name for class
    //Logger log =  LoggerFactory.getLogger(this.getClass().getName());



    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer ");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer ");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
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
            return dtoMapper.fromCurrentBankAccount(savedBankAccount);

    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
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
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> collect = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return collect;

       /*List<CustomerDTO> customerDTOS= new ArrayList<>();
        for(Customer customer:customers){
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;*/
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount Not Found"));
       if(bankAccount instanceof SavingAccount){
           SavingAccount savingAccount = (SavingAccount) bankAccount;
           return dtoMapper.fromSavingBankAccount(savingAccount);
       }else {
           CurrentAccount currentAccount = (CurrentAccount) bankAccount;
           return dtoMapper.fromCurrentBankAccount(currentAccount);
       }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount Not Found"));
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
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount Not Found"));

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
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOs  = bankAccounts.stream().map(acc->{
            if(acc instanceof SavingAccount){
                SavingAccount savingAccount = (SavingAccount) acc;
                return dtoMapper.fromSavingBankAccount(savingAccount);
            }else {
                CurrentAccount currentAccount = (CurrentAccount) acc;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOs;
    }

    @Override
    public CustomerDTO getCustomer(Long customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));
        return  dtoMapper.fromCustomer(customer);
    }
}
