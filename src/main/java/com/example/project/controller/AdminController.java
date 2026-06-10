package com.example.project.controller;

import com.example.project.model.dto.request.CourtRequest;
import com.example.project.model.dto.request.CreateUserRequest;
import com.example.project.model.dto.request.UpdateUserRequest;
import com.example.project.model.dto.request.UploadImageCourtRequest;
import com.example.project.model.dto.response.ApiDataResponse;
import com.example.project.model.dto.response.UserProjectionDTO;
import com.example.project.model.entity.Court;
import com.example.project.model.entity.User;
import com.example.project.service.CourtService;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final CourtService courtService;

    // Lấy danh sách
    @GetMapping("/users")
    public ResponseEntity<ApiDataResponse<Page<User>>> getListUser(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy danh sách thành công",
                userService.getListUser(currentPage, pageSize, keyword),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    // Xem chi tiết người dùng
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiDataResponse<UserProjectionDTO>> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy thông tin người dùng thành công",
                userService.getUserById(id),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    // Vô hiệu hoá
    @PatchMapping("/users/{id}/disabled")
    public ResponseEntity<ApiDataResponse<User>> disableUser(@PathVariable Long id) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Vô hiệu hoá tài khoản thành công",
                userService.disableUser(id),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    // Thêm tài khoản
    @PostMapping("/users")
    public ResponseEntity<ApiDataResponse<User>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Tạo tài khoản thành công",
                userService.createUser(request),
                null,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    // Cập nhật tài khoản
    @PatchMapping("/users/{id}/updated")
    public ResponseEntity<ApiDataResponse<User>> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Cập nhật tài khoản thành công",
                userService.updateUser(id, request),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    // Thêm sân
    @PostMapping(value = "/courts")
    public ResponseEntity<ApiDataResponse<Court>> createCourt(
            @Valid @RequestBody CourtRequest request
    ) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Tạo sân thành công",
                courtService.createCourt(request),
                null,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PostMapping("/courts/{id}/uploadImage")
    public ResponseEntity<ApiDataResponse<Court>> uploadImages(
            @PathVariable Long id,
            @RequestPart("images") List<MultipartFile> images
    ) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Tải ảnh sân thành công",
                courtService.uploadImages(id, images),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}
