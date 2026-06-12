package com.example.project.controller;

import com.example.project.model.dto.request.*;
import com.example.project.model.dto.response.ApiDataResponse;
import com.example.project.model.dto.response.JwtResponse;
import com.example.project.model.entity.User;
import com.example.project.service.AuthService;
import com.example.project.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<User>> register(@Valid @RequestBody UserRegister userRegister) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Tạo tài khoản thành công",
                authService.register(userRegister),
                null,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody UserLogin userLogin) {
        return new ResponseEntity<>(authService.login(userLogin), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return new ResponseEntity<>(refreshTokenService.refresh(request.getToken()), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponse<?>> logout(@RequestBody LogoutRequest request) {
        authService.logout(request.getToken());
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Đăng xuất thành công",
                null,
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<ApiDataResponse<User>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Đổi mật khẩu thành công",
                authService.changePassword(request),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<ApiDataResponse<?>> forgetPassword(@RequestBody ForgetPasswordRequest request) {
        authService.forgetPassword(request);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Đã gửi OTP về email",
                null,
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiDataResponse<?>> verifyOtp(@RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Xác nhận OTP thành công",
                null,
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}
