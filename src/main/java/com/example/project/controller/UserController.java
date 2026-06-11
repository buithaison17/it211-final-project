package com.example.project.controller;

import com.example.project.model.dto.request.BookingRequest;
import com.example.project.model.dto.response.ApiDataResponse;
import com.example.project.model.entity.Booking;
import com.example.project.service.BookingService;
import com.example.project.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final BookingService bookingService;

    @PostMapping("/courts/{id}/booked")
    public ResponseEntity<ApiDataResponse<Booking>> bookCourt(@PathVariable Long id, @RequestBody BookingRequest request) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Đặt sân thành công",
                bookingService.bookCourt(id, request),
                null,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    // Xem lịch sử đặt hàng
    @GetMapping("/bookings")
    public ResponseEntity<ApiDataResponse<Page<Booking>>> getBookingOfUser(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Lấy danh sách sân đã đặt thành công",
                bookingService.getBookingOfUser(currentPage, pageSize),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}
