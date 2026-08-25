package com.skala.stock.controller;

import com.skala.stock.dto.DepositRequestDto;
import com.skala.stock.dto.UserDto;
import com.skala.stock.dto.UserUpdateDto;
import com.skala.stock.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "사용자 관리", description = "사용자 CRUD API")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 등록합니다")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자를 조회합니다")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "전체 사용자 조회", description = "모든 사용자를 조회합니다")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "ID로 사용자를 삭제합니다 (보유 주식/거래내역이 있으면 삭제할 수 없습니다)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deposit")
    @Operation(summary = "잔액 충전", description = "사용자의 잔액을 충전합니다")
    public ResponseEntity<UserDto> deposit(
            @PathVariable Long id,
            @Valid @RequestBody DepositRequestDto depositRequest) {
        UserDto user = userService.deposit(id, depositRequest.getAmount());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "사용자 정보 수정", description = "사용자의 비밀번호와 이메일을 수정합니다")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto userUpdateDto) {
        UserDto user = userService.updateUser(id, userUpdateDto);
        return ResponseEntity.ok(user);
    }
}
