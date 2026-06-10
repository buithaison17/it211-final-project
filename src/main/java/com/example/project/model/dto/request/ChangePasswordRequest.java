package com.example.project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Mật khẩu cũ không được để trống")
    @Size(min = 8, message = "Mật khẩu cũ phải có độ dài từ 8 ký tự trở lên")
    private String currentPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 8, message = "Mật khẩu mới phải có độ dài từ 8 ký tự trở lên")
    private String newPassword;

    @NotBlank(message = "Mật khẩu xác nhận không được để trống")
    @Size(min = 8, message = "Mật khẩu xác nhận phải có độ dài từ 8 ký tự trở lên")
    private String confirmPassword;
}
