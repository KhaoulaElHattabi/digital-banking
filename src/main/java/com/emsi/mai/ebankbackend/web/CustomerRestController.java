package com.emsi.mai.ebankbackend.web;

import com.emsi.mai.ebankbackend.dtos.CustomerDTO;
import com.emsi.mai.ebankbackend.entities.BankAccount;
import com.emsi.mai.ebankbackend.entities.Customer;
import com.emsi.mai.ebankbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j


public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDTO> customerList(){
        return bankAccountService.listCustomers();
    }
}
