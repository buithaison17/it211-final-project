package com.example.project.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingRequest {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Thời gian bắt đầu không được để để trống")
    @Future(message = "Thời gian bắt đầu phải là trong tương lai")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Thời gian kết thúc không được để trống")
    @Future(message = "Thời gian kết thúc phải là ngày trong tương lai")
    private LocalDateTime endTime;
}
