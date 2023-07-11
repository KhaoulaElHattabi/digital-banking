package com.emsi.mai.ebankbackend.mappers;

import com.emsi.mai.ebankbackend.dtos.CustomerDTO;
import com.emsi.mai.ebankbackend.dtos.SavingBankAccountDTO;
import com.emsi.mai.ebankbackend.entities.Customer;
import com.emsi.mai.ebankbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

//MapStruct
//Jmappers
@Service
public class BankAccountMapperImpl {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO =  new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        //customerDTO.setId(customer.getId());
        //customerDTO.setName(customer.getName());
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){
        SavingBankAccountDTO savingBankAccountDTO =  new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        return savingBankAccountDTO;
    }

    public SavingAccount fromSavingBankAccount(SavingBankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount=new SavingAccount();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        return savingAccount;
    }
}
