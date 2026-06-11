package com.example.project.service;

import com.example.project.enums.BookingStatus;
import com.example.project.exception.NotFoundException;
import com.example.project.exception.StartTimeAfterEndTimeException;
import com.example.project.exception.TimeDurationHaveBookingException;
import com.example.project.model.dto.request.BookingRequest;
import com.example.project.model.dto.request.BookingStatusRequest;
import com.example.project.model.entity.Booking;
import com.example.project.model.entity.Court;
import com.example.project.model.entity.User;
import com.example.project.repository.BookingRepository;
import com.example.project.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CourtService courtService;

    public Booking bookCourt(Long id, BookingRequest request) {
        // Kiểm tra ngày bắt đầu phải trước ngày kết thúc
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new StartTimeAfterEndTimeException("Thời gian bắt đầu phải trước thời gian kết thúc");
        }
        // Kiểm tra trong khoảng thời gian này đã có người đặt chưa
        if (bookingRepository.isDuplicateBooking(id, request.getStartTime(), request.getEndTime(), BookingStatus.CANCELLED)) {
            throw new TimeDurationHaveBookingException("Khoảng thời gian này đã có người đặt");
        }
        // Lấy thông tin sân
        Court court = courtService.findById(id);
        // Lấy người dùng hiện tại
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Tạo booking
        Booking booking = Booking.builder().court(court).user(userPrincipal.getUser()).startTime(request.getStartTime()).endTime(request.getEndTime()).totalPrice(court.getPrice()).status(BookingStatus.PENDING).enabled(true).build();
        return bookingRepository.save(booking);
    }

    public Page<Booking> getBookingOfUser(Integer currentPage, Integer pageSize) {
        // Lấy ngươi dùng hiện tại
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        return bookingRepository.findByUser(user, pageable);
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
    }

    public Booking updateStatus(Long id, BookingStatusRequest request) {
        Booking booking = findById(id);
        booking.setStatus(request.getStatus());
        return bookingRepository.save(booking);
    }
}
