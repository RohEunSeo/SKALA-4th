package com.skala.stock.repository;

import com.skala.stock.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUserId(Long userId);
    Optional<Portfolio> findByUserIdAndStockId(Long userId, Long stockId);
    boolean existsByUserIdAndStockId(Long userId, Long stockId);
    boolean existsByUserId(Long userId);
    boolean existsByStockId(Long stockId);

    @Query("SELECT COALESCE(SUM(p.quantity * s.currentPrice), 0) FROM Portfolio p JOIN p.stock s WHERE p.user.id = :userId")
    Long sumCurrentValueByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(p.quantity * p.averagePrice), 0) FROM Portfolio p WHERE p.user.id = :userId")
    Long sumCostBasisByUserId(@Param("userId") Long userId);
}
