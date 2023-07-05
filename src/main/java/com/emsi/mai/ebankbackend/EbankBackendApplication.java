package com.emsi.mai.ebankbackend;

import com.emsi.mai.ebankbackend.entities.*;
import com.emsi.mai.ebankbackend.enums.AccountStatus;
import com.emsi.mai.ebankbackend.enums.OperationType;
import com.emsi.mai.ebankbackend.repositories.AccountOperationRepository;
import com.emsi.mai.ebankbackend.repositories.BankAccountRepository;
import com.emsi.mai.ebankbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

    @Bean
    @Transactional
    CommandLineRunner commandLineRunner(BankAccountRepository bankAccountRepository){
        return args -> {
            //affichage d'un compte bancaire avec l'id+
            BankAccount bankAccount=
                    bankAccountRepository.findById("47e3b578-b477-4f7c-a1cd-c0b2ed24538a").orElse(null);
            if(bankAccount!=null) {
                System.out.println("****************************************");
                System.out.println(bankAccount.getId());
                System.out.println(bankAccount.getBalance());
                System.out.println(bankAccount.getStatus());
                System.out.println(bankAccount.getCreatedAt());
                System.out.println(bankAccount.getCustomer().getName());
                System.out.println(bankAccount.getClass().getSimpleName());
                if (bankAccount instanceof CurrentAccount) {
                    System.out.println("Over Draft: " + ((CurrentAccount) bankAccount).getOverDraft());
                } else if (bankAccount instanceof SavingAccount) {
                    System.out.println("Rate: " + ((SavingAccount) bankAccount).getInterestRate());
                }
                bankAccount.getAccountOperations().forEach(op -> {
                    System.out.println("==========================");
                    System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount() + "\t");
                });
            }
        };
    }
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan","Yassine","Aicha").forEach(name->{
                Customer customer= new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            //utilisation d'une expressions lamda pour la creation un CC et un CE pour chaque customer
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount= new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreatedAt(Date.valueOf(LocalDate.now()));
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount= new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreatedAt(Date.valueOf(LocalDate.now()));
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            //utilisation d'une expressions lamda pour la creation des comptes pour chaque customer
            bankAccountRepository.findAll().forEach(acc->{
                for (int i=0; i<10; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(Date.valueOf(LocalDate.now()));
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }

}
