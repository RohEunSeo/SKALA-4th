package com.sk.skala.shopapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.skala.shopapi.common.PagedList;
import com.sk.skala.shopapi.data.table.Product;
import com.sk.skala.shopapi.exception.Error;
import com.sk.skala.shopapi.exception.ResponseException;
import com.sk.skala.shopapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public PagedList<Product> getProducts(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        return PagedList.of(page);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseException(Error.PRODUCT_NOT_FOUND));
    }

    @Transactional
    public Product createProduct(Product request) {
        Product product = Product.builder()
                .productName(request.getProductName())
                .productPrice(request.getProductPrice())
                .build();
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product request) {
        Product product = getProduct(id);
        product.setProductName(request.getProductName());
        product.setProductPrice(request.getProductPrice());
        return product;
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProduct(id);
        productRepository.delete(product);
    }
}
