package com.sk.skala.shopapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sk.skala.shopapi.data.table.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    Page<OrderItem> findByCustomerId(Long customerId, Pageable pageable);

    Optional<OrderItem> findByIdAndCustomerId(Long id, Long customerId);
}
