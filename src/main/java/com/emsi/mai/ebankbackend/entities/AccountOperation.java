package com.emsi.mai.ebankbackend.entities;



import com.emsi.mai.ebankbackend.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date operationDate;
    private  double amount;
    private OperationType type;
    @ManyToOne
    private  BankAccount bankAccount;
}
