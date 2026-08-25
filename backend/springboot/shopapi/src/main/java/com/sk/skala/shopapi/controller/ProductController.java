package com.sk.skala.shopapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sk.skala.shopapi.common.PagedList;
import com.sk.skala.shopapi.common.Response;
import com.sk.skala.shopapi.data.table.Product;
import com.sk.skala.shopapi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Product", description = "상품 조회/등록/수정/삭제 (인증 불필요)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록 조회", description = "등록된 상품을 페이지 단위로 조회합니다. 예: `page=0&size=10`")
    @GetMapping
    public Response<PagedList<Product>> getProducts(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return Response.success(productService.getProducts(pageable));
    }

    @Operation(summary = "상품 단건 조회", description = "존재하지 않는 ID면 404(PRODUCT_NOT_FOUND)를 반환합니다.")
    @GetMapping("/{id}")
    public Response<Product> getProduct(
            @Parameter(description = "상품 ID", example = "1") @PathVariable Long id) {
        return Response.success(productService.getProduct(id));
    }

    @Operation(summary = "상품 생성", description = "productName, productPrice를 받아 새 상품을 등록합니다. id/createdAt/updatedAt은 무시됩니다.")
    @PostMapping
    public Response<Product> createProduct(@Valid @RequestBody Product request) {
        return Response.success(productService.createProduct(request));
    }

    @Operation(summary = "상품 수정", description = "상품명/가격을 수정합니다. 존재하지 않는 ID면 404를 반환합니다.")
    @PutMapping("/{id}")
    public Response<Product> updateProduct(
            @Parameter(description = "상품 ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody Product request) {
        return Response.success(productService.updateProduct(id, request));
    }

    @Operation(summary = "상품 삭제", description = "존재하지 않는 ID면 404를 반환합니다.")
    @DeleteMapping("/{id}")
    public Response<Void> deleteProduct(
            @Parameter(description = "상품 ID", example = "1") @PathVariable Long id) {
        productService.deleteProduct(id);
        return Response.success();
    }
}
