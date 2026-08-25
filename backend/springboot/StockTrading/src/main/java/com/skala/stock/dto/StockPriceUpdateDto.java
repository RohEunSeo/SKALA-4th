package com.skala.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockPriceUpdateDto {

    @NotNull(message = "변경할 가격은 필수입니다")
    @Positive(message = "가격은 0보다 커야 합니다")
    private Long currentPrice;
}
