package com.example.project.controller;

import com.example.project.model.dto.request.BookingStatusRequest;
import com.example.project.model.dto.response.ApiDataResponse;
import com.example.project.model.entity.Booking;
import com.example.project.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PatchMapping("/{id}/status")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<ApiDataResponse<Booking>> updateStatus(
            @PathVariable Long id,
            @RequestBody BookingStatusRequest request
    ) {
        return new ResponseEntity<>(new ApiDataResponse<>(
                true,
                "Cập nhật trạng thái thành công",
                bookingService.updateStatus(id, request),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }
}
