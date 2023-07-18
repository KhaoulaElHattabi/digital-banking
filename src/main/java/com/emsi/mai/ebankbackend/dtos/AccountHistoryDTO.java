package com.emsi.mai.ebankbackend.dtos;

import com.emsi.mai.ebankbackend.entities.AccountOperation;
import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOs;


}
