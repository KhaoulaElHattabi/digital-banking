package com.emsi.mai.ebankbackend.dtos;

import com.emsi.mai.ebankbackend.entities.AccountOperation;
import com.emsi.mai.ebankbackend.entities.Customer;
import com.emsi.mai.ebankbackend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}