package com.skala.stock.service;

import com.skala.stock.entity.Transaction;
import com.skala.stock.entity.TradeAuditLog;
import com.skala.stock.entity.User;
import com.skala.stock.repository.PortfolioRepository;
import com.skala.stock.repository.TradeAuditLogRepository;
import com.skala.stock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 거래 트랜잭션과 분리된 별도 트랜잭션(REQUIRES_NEW)으로 감사 로그를 남긴다.
 * executeTrade()가 예외를 던지고 롤백되더라도, 이 로그는 독립적으로 커밋된다.
 */
@Service
@RequiredArgsConstructor
public class TradeAuditService {

    private final TradeAuditLogRepository tradeAuditLogRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordWarning(Long userId, Long stockId, Transaction.TransactionType type, String message) {
        User user = userRepository.findById(userId).orElse(null);

        Long totalAssets = null;
        Double totalReturnRate = null;
        if (user != null) {
            Long portfolioValue = portfolioRepository.sumCurrentValueByUserId(userId);
            Long costBasis = portfolioRepository.sumCostBasisByUserId(userId);
            totalAssets = user.getBalance() + portfolioValue;
            totalReturnRate = costBasis == 0 ? 0.0 : (portfolioValue - costBasis) * 100.0 / costBasis;
        }

        TradeAuditLog log = TradeAuditLog.builder()
                .userId(userId)
                .stockId(stockId)
                .type(type)
                .message(message)
                .totalAssets(totalAssets)
                .totalReturnRate(totalReturnRate)
                .build();

        tradeAuditLogRepository.save(log);
    }
}
