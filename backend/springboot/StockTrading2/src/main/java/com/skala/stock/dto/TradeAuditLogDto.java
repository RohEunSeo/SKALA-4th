package com.skala.stock.dto;

import com.skala.stock.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeAuditLogDto {

    private Long id;
    private Long userId;
    private Long stockId;
    private Transaction.TransactionType type;
    private String message;
    private Long totalAssets;
    private Double totalReturnRate;
    private LocalDateTime createdAt;
}
