package com.example.project.controller;

import com.example.project.model.entity.Booking;
import com.example.project.security.jwt.JwtProvider;
import com.example.project.security.principal.UserPrincipalService;
import com.example.project.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private BookingService bookingService;
    @MockitoBean
    private JwtProvider jwtProvider;
    @MockitoBean
    private UserPrincipalService userPrincipalService;

    // Test xem lịch sử đặt sân của người dùng
    @Test
    @WithMockUser(username = "sonbui@gmail.com", roles = "USER")
    void getBookingOfUser_Success() throws Exception {
        Booking booking = Booking.builder()
                .id(1L)
                .build();
        Page<Booking> page = new PageImpl<>(
                List.of(booking),
                PageRequest.of(0, 10),
                1
        );
        Mockito.when(bookingService.getBookingOfUser(1, 10))
                .thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/bookings")
                        .param("currentPage", "1")
                        .param("pageSize", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
        Mockito.verify(bookingService)
                .getBookingOfUser(1, 10);
    }
}
