package com.sk.skala.shopapi.data.dto;

import java.time.LocalDateTime;

import com.sk.skala.shopapi.data.table.OrderItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "주문 생성 요청(productId, quantity)과 주문 응답을 겸하는 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    @Schema(description = "주문 ID (응답 전용)", example = "1")
    private Long id;

    @Schema(description = "주문할 상품 ID", example = "1")
    @NotNull
    private Long productId;

    @Schema(description = "상품명 (응답 전용)", example = "SKALA 후드집업")
    private String productName;

    @Schema(description = "주문 수량 (1 이상)", example = "2")
    @NotNull
    @Min(1)
    private Integer quantity;

    @Schema(description = "주문 금액 = 상품 가격 × 수량, 주문 시점 스냅샷 (응답 전용)", example = "90000")
    private Double orderPrice;

    @Schema(description = "주문 상태 (응답 전용)", example = "ORDERED")
    private OrderItem.OrderStatus status;

    @Schema(description = "주문 일시 (응답 전용)")
    private LocalDateTime orderedAt;

    public static OrderItemDto from(OrderItem entity) {
        return OrderItemDto.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getProductName())
                .quantity(entity.getQuantity())
                .orderPrice(entity.getOrderPrice())
                .status(entity.getStatus())
                .orderedAt(entity.getCreatedAt())
                .build();
    }
}
