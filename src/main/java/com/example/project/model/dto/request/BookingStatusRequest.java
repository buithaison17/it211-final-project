package com.example.project.model.dto.request;

import com.example.project.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingStatusRequest {
    private BookingStatus status;
}
