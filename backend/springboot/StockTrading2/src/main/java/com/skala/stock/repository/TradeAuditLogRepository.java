package com.skala.stock.repository;

import com.skala.stock.entity.TradeAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeAuditLogRepository extends JpaRepository<TradeAuditLog, Long> {
    List<TradeAuditLog> findByUserIdOrderByCreatedAtDesc(Long userId);
}

