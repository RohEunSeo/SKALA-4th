package com.sk.skala.shopapi.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sk.skala.shopapi.common.Response;
import com.sk.skala.shopapi.common.SessionHandler;
import com.sk.skala.shopapi.data.dto.CustomerRequest;
import com.sk.skala.shopapi.data.dto.CustomerSession;
import com.sk.skala.shopapi.data.dto.OrderItemDto;
import com.sk.skala.shopapi.data.dto.OrderListDto;
import com.sk.skala.shopapi.data.table.Customer;
import com.sk.skala.shopapi.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Customer", description = "회원가입/로그인/내 정보 및 주문(생성·취소·조회)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final SessionHandler sessionHandler;

    @Operation(summary = "회원가입", description = "가입 시 100만 포인트가 자동으로 지급됩니다. loginId가 이미 존재하면 409를 반환합니다.")
    @PostMapping("/signup")
    public Response<CustomerSession> signup(@Valid @RequestBody CustomerRequest request) {
        return Response.success(customerService.signup(request));
    }

    @Operation(summary = "로그인", description = "성공 시 JWT 토큰을 발급합니다. 이 토큰을 우측 상단 Authorize에 입력하면 인증이 필요한 API를 테스트할 수 있습니다.")
    @PostMapping("/login")
    public Response<CustomerSession> login(@Valid @RequestBody CustomerRequest request) {
        Customer customer = customerService.login(request);
        CustomerSession session = CustomerSession.builder()
                .customerId(customer.getId())
                .loginId(customer.getLoginId())
                .name(customer.getName())
                .build();
        String token = sessionHandler.generateToken(session);
        return Response.success(CustomerSession.builder()
                .customerId(session.getCustomerId())
                .loginId(session.getLoginId())
                .name(session.getName())
                .token(token)
                .build());
    }

    @Operation(summary = "내 정보 조회", description = "JWT로 식별된 로그인 사용자 본인의 정보를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public Response<CustomerSession> getMe(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        CustomerSession session = sessionHandler.resolve(token);
        return Response.success(customerService.getMe(session.getCustomerId()));
    }

    @Operation(summary = "주문 생성", description = "상품 ID와 수량으로 주문합니다. 상품 가격 × 수량만큼 포인트가 차감되며, 포인트가 부족하면 400(INSUFFICIENT_POINT)을 반환합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/orders")
    public Response<OrderItemDto> placeOrder(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @Valid @RequestBody OrderItemDto request) {
        CustomerSession session = sessionHandler.resolve(token);
        return Response.success(customerService.placeOrder(session.getCustomerId(), request));
    }

    @Operation(summary = "주문 취소", description = "본인의 주문만 취소할 수 있으며, 취소 시 차감했던 포인트가 환급됩니다. 이미 취소된 주문이면 400(ALREADY_CANCELED)을 반환합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/orders/{id}")
    public Response<OrderItemDto> cancelOrder(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @Parameter(description = "취소할 주문 ID", example = "1") @PathVariable Long id) {
        CustomerSession session = sessionHandler.resolve(token);
        return Response.success(customerService.cancelOrder(session.getCustomerId(), id));
    }

    @Operation(summary = "내 주문 목록 조회", description = "로그인한 본인의 주문 내역을 현재 포인트 잔액과 함께 페이지 단위로 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/orders")
    public Response<OrderListDto> getMyOrders(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        CustomerSession session = sessionHandler.resolve(token);
        return Response.success(customerService.getMyOrders(session.getCustomerId(), pageable));
    }
}
