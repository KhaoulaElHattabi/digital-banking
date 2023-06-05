package com.emsi.mai.ebankbackend.entities;



import com.emsi.mai.ebankbackend.enums.OperationType;

import java.util.Date;

public class AccountOperation {
    private long id;
    private Date operationDate;
    private  double amount;
    private OperationType type;
}
