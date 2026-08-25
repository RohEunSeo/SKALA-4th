package com.skala.stock.controller;

import com.skala.stock.dto.PortfolioDto;
import com.skala.stock.dto.TradeAuditLogDto;
import com.skala.stock.dto.TransactionDto;
import com.skala.stock.dto.UserRankingDto;
import com.skala.stock.service.StockAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@Tag(name = "분석/고급 기능", description = "포트폴리오/거래/자산 분석 API")
public class StockAnalysisController {

    private final StockAnalysisService stockAnalysisService;

    @GetMapping("/portfolio-profit-loss/{userId}")
    @Operation(summary = "포트폴리오 평가 손익 조회", description = "사용자의 보유 종목별 평가 손익을 조회합니다")
    public ResponseEntity<List<PortfolioDto>> getPortfolioWithProfitLoss(@PathVariable Long userId) {
        List<PortfolioDto> result = stockAnalysisService.getPortfolioWithProfitLoss(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/transactions/{userId}")
    @Operation(summary = "거래 내역 상세 조회", description = "사용자의 전체 거래 내역을 상세 정보와 함께 조회합니다")
    public ResponseEntity<List<TransactionDto>> getTransactionsWithDetails(@PathVariable Long userId) {
        List<TransactionDto> result = stockAnalysisService.getTransactionsWithDetails(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/transactions/{userId}/stock/{stockId}")
    @Operation(summary = "특정 주식 거래 내역 조회", description = "사용자의 특정 종목에 대한 거래 내역을 조회합니다")
    public ResponseEntity<List<TransactionDto>> getStockTransactionsWithDetails(
            @PathVariable Long userId,
            @PathVariable Long stockId) {
        List<TransactionDto> result = stockAnalysisService.getStockTransactionsWithDetails(userId, stockId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/total-assets/{userId}")
    @Operation(summary = "총 자산 조회", description = "사용자의 보유 현금 + 보유 주식 평가금액 합계를 조회합니다")
    public ResponseEntity<Long> getTotalAssets(@PathVariable Long userId) {
        Long totalAssets = stockAnalysisService.getTotalAssets(userId);
        return ResponseEntity.ok(totalAssets);
    }

    @GetMapping("/total-return-rate/{userId}")
    @Operation(summary = "총 수익률 조회", description = "보유 종목의 매수원금 대비 평가손익 비율(%)을 조회합니다")
    public ResponseEntity<Double> getTotalReturnRate(@PathVariable Long userId) {
        Double totalReturnRate = stockAnalysisService.getTotalReturnRate(userId);
        return ResponseEntity.ok(totalReturnRate);
    }

    @GetMapping("/audit-logs/{userId}")
    @Operation(summary = "거래 실패 감사로그 조회", description = "REQUIRES_NEW로 별도 기록된 거래 실패(잔액/수량 부족) 경고 로그를 조회합니다")
    public ResponseEntity<List<TradeAuditLogDto>> getAuditLogs(@PathVariable Long userId) {
        List<TradeAuditLogDto> result = stockAnalysisService.getAuditLogs(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/ranking")
    @Operation(summary = "총자산 기준 사용자 랭킹", description = "전체 사용자를 총자산 내림차순으로 정렬한 랭킹을 조회합니다")
    public ResponseEntity<List<UserRankingDto>> getAssetRanking() {
        List<UserRankingDto> result = stockAnalysisService.getAssetRanking();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/max-buyable/{userId}/stock/{stockId}")
    @Operation(summary = "최대 매수 가능 수량 조회", description = "사용자의 현재 잔액으로 해당 주식을 최대 몇 주 살 수 있는지 조회합니다")
    public ResponseEntity<Long> getMaxBuyableQuantity(
            @PathVariable Long userId,
            @PathVariable Long stockId) {
        Long maxQuantity = stockAnalysisService.getMaxBuyableQuantity(userId, stockId);
        return ResponseEntity.ok(maxQuantity);
    }
}
