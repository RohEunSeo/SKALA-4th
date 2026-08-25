package com.skala.stock.service;

import com.skala.stock.dto.PortfolioDto;
import com.skala.stock.dto.TradeAuditLogDto;
import com.skala.stock.dto.TransactionDto;
import com.skala.stock.dto.UserRankingDto;
import com.skala.stock.entity.Stock;
import com.skala.stock.entity.TradeAuditLog;
import com.skala.stock.entity.User;
import com.skala.stock.repository.PortfolioRepository;
import com.skala.stock.repository.StockRepository;
import com.skala.stock.repository.TradeAuditLogRepository;
import com.skala.stock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockAnalysisService {

    private final PortfolioService portfolioService;
    private final TransactionService transactionService;
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final TradeAuditLogRepository tradeAuditLogRepository;

    public List<PortfolioDto> getPortfolioWithProfitLoss(Long userId) {
        return portfolioService.getUserPortfolio(userId);
    }

    public List<TransactionDto> getTransactionsWithDetails(Long userId) {
        return transactionService.getUserTransactions(userId);
    }

    public List<TransactionDto> getStockTransactionsWithDetails(Long userId, Long stockId) {
        return transactionService.getUserStockTransactions(userId, stockId);
    }

    public Long getTotalAssets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        Long portfolioValue = portfolioRepository.sumCurrentValueByUserId(userId);
        return user.getBalance() + portfolioValue;
    }

    public Double getTotalReturnRate(Long userId) {
        Long costBasis = portfolioRepository.sumCostBasisByUserId(userId);
        Long currentValue = portfolioRepository.sumCurrentValueByUserId(userId);

        if (costBasis == 0) {
            return 0.0;
        }
        return (currentValue - costBasis) * 100.0 / costBasis;
    }

    public List<TradeAuditLogDto> getAuditLogs(Long userId) {
        List<TradeAuditLog> logs = tradeAuditLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return logs.stream()
                .map(log -> TradeAuditLogDto.builder()
                        .id(log.getId())
                        .userId(log.getUserId())
                        .stockId(log.getStockId())
                        .type(log.getType())
                        .message(log.getMessage())
                        .totalAssets(log.getTotalAssets())
                        .totalReturnRate(log.getTotalReturnRate())
                        .createdAt(log.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<UserRankingDto> getAssetRanking() {
        List<User> users = userRepository.findAll();

        List<UserRankingDto> ranked = users.stream()
                .map(user -> UserRankingDto.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .totalAssets(getTotalAssets(user.getId()))
                        .build())
                .sorted(Comparator.comparing(UserRankingDto::getTotalAssets).reversed())
                .collect(Collectors.toList());

        for (int i = 0; i < ranked.size(); i++) {
            ranked.get(i).setRank(i + 1);
        }
        return ranked;
    }

    public Long getMaxBuyableQuantity(Long userId, Long stockId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("주식을 찾을 수 없습니다: " + stockId));

        return user.getBalance() / stock.getCurrentPrice();
    }
}
