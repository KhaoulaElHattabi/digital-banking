package com.emsi.mai.ebankbackend.repositories;

import com.emsi.mai.ebankbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
