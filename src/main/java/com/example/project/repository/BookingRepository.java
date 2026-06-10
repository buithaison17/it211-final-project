package com.example.project.repository;

import com.example.project.enums.BookingStatus;
import com.example.project.model.entity.Booking;
import com.example.project.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
                    select exists(select 1 from Booking b
                    where b.court.id = :courtId
                    and b.startTime < :endTime
                    and b.endTime > :startTime
                    and b.status <> :status)
            """)
    boolean isDuplicateBooking(
            @Param("courtId") Long courtId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") BookingStatus status
    );

    Page<Booking> findByUser(User user, Pageable pageable);
}
