package com.sk.skala.shopapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.shopapi.data.table.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
}
