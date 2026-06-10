package com.example.project.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectionDTO {
    private String fullName;
    private String email;
    private String password;
    private String phone;
}
