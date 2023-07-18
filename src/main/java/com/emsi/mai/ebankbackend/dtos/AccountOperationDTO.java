package com.emsi.mai.ebankbackend.dtos;



import com.emsi.mai.ebankbackend.entities.BankAccount;
import com.emsi.mai.ebankbackend.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
public class AccountOperationDTO {
    private long id;
    private Date operationDate;
    private  double amount;
    private OperationType type;
    private String Description;
}
