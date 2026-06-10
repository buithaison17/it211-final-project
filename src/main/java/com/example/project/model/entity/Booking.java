package com.example.project.model.entity;

import com.example.project.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalPrice;
    private Boolean enabled;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
