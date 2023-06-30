package com.emsi.mai.ebankbackend.repositories;

import com.emsi.mai.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
