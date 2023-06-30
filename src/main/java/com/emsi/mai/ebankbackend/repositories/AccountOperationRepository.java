package com.emsi.mai.ebankbackend.repositories;

import com.emsi.mai.ebankbackend.entities.AccountOperation;
import com.emsi.mai.ebankbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
