package com.sk.skala.shopapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.common.PagedList;
import com.sk.skala.shopapi.data.dto.CustomerRequest;
import com.sk.skala.shopapi.data.dto.CustomerSession;
import com.sk.skala.shopapi.data.dto.OrderItemDto;
import com.sk.skala.shopapi.data.dto.OrderListDto;
import com.sk.skala.shopapi.data.table.Customer;
import com.sk.skala.shopapi.data.table.OrderItem;
import com.sk.skala.shopapi.data.table.Product;
import com.sk.skala.shopapi.exception.Error;
import com.sk.skala.shopapi.exception.ParameterException;
import com.sk.skala.shopapi.exception.ResponseException;
import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.repository.OrderItemRepository;
import com.sk.skala.shopapi.repository.ProductRepository;
import com.sk.skala.shopapi.tools.StringUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private static final long DEFAULT_POINT = 1_000_000L;

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public CustomerSession signup(CustomerRequest request) {
        if (StringUtil.isBlank(request.getName())) {
            throw new ParameterException("이름은 필수 입력값입니다.");
        }
        if (customerRepository.existsByLoginId(request.getLoginId())) {
            throw new ResponseException(Error.DUPLICATE_LOGIN_ID);
        }

        Customer customer = Customer.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .point(DEFAULT_POINT)
                .build();
        customerRepository.save(customer);

        return CustomerSession.builder()
                .customerId(customer.getId())
                .loginId(customer.getLoginId())
                .name(customer.getName())
                .build();
    }

    public Customer login(CustomerRequest request) {
        Customer customer = customerRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new ResponseException(Error.INVALID_CREDENTIALS));
        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new ResponseException(Error.INVALID_CREDENTIALS);
        }
        return customer;
    }

    public CustomerSession getMe(Long customerId) {
        Customer customer = getCustomer(customerId);
        return CustomerSession.builder()
                .customerId(customer.getId())
                .loginId(customer.getLoginId())
                .name(customer.getName())
                .build();
    }

    @Transactional
    public OrderItemDto placeOrder(Long customerId, OrderItemDto request) {
        Customer customer = getCustomer(customerId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseException(Error.PRODUCT_NOT_FOUND));

        double totalPrice = product.getProductPrice() * request.getQuantity();
        if (customer.getPoint() < totalPrice) {
            throw new ResponseException(Error.INSUFFICIENT_POINT);
        }
        customer.deductPoint(Math.round(totalPrice));

        OrderItem orderItem = OrderItem.builder()
                .customer(customer)
                .product(product)
                .quantity(request.getQuantity())
                .orderPrice(totalPrice)
                .status(OrderItem.OrderStatus.ORDERED)
                .build();
        orderItemRepository.save(orderItem);

        return OrderItemDto.from(orderItem);
    }

    @Transactional
    public OrderItemDto cancelOrder(Long customerId, Long orderId) {
        OrderItem orderItem = orderItemRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new ResponseException(Error.ORDER_NOT_FOUND));
        if (orderItem.getStatus() == OrderItem.OrderStatus.CANCELED) {
            throw new ResponseException(Error.ALREADY_CANCELED);
        }

        orderItem.cancel();
        orderItem.getCustomer().restorePoint(Math.round(orderItem.getOrderPrice()));

        return OrderItemDto.from(orderItem);
    }

    public OrderListDto getMyOrders(Long customerId, Pageable pageable) {
        Customer customer = getCustomer(customerId);
        Page<OrderItemDto> page = orderItemRepository.findByCustomerId(customerId, pageable)
                .map(OrderItemDto::from);

        return OrderListDto.builder()
                .customerId(customer.getId())
                .currentPoint(customer.getPoint())
                .orders(PagedList.of(page))
                .build();
    }

    private Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseException(Error.CUSTOMER_NOT_FOUND));
    }
}
