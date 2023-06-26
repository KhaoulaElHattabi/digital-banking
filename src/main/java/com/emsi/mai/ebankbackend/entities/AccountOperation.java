package com.emsi.mai.ebankbackend.entities;



import com.emsi.mai.ebankbackend.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
@Data @NoArgsConstructor @AllArgsConstructor
public class AccountOperation {
    private long id;
    private Date operationDate;
    private  double amount;
    private OperationType type;
}
