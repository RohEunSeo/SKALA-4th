package com.sk.skala.shopapi.data.table;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "상품 정보. 생성/수정 요청과 조회 응답에 공통으로 사용됩니다.")
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseTimeEntity {

    @Schema(description = "상품 ID (응답 전용, 생성 요청 시에는 무시됩니다)", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "상품명", example = "SKALA 후드집업")
    @NotBlank
    @Column(nullable = false)
    private String productName;

    @Schema(description = "상품 가격 (원)", example = "45000")
    @NotNull
    @Positive
    @Column(nullable = false)
    private Double productPrice;
}
