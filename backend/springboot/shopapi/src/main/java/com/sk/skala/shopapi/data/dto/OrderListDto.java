package com.sk.skala.shopapi.data.dto;

import com.sk.skala.shopapi.common.PagedList;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "내 주문 목록 조회 응답 (현재 포인트 잔액 + 페이징된 주문 목록)")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDto {

    @Schema(description = "고객 ID", example = "1")
    private Long customerId;

    @Schema(description = "현재 포인트 잔액", example = "910000")
    private Long currentPoint;

    @Schema(description = "페이징된 주문 목록")
    private PagedList<OrderItemDto> orders;
}
