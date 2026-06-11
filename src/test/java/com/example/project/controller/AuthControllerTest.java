package com.example.project.controller;

import com.example.project.exception.ExistException;
import com.example.project.model.dto.request.UserLogin;
import com.example.project.model.dto.request.UserRegister;
import com.example.project.model.dto.response.JwtResponse;
import com.example.project.model.entity.User;
import com.example.project.service.AuthService;
import com.example.project.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private RefreshTokenService refreshTokenService;

    // Đăng kí thành công
    @Test
    void register_Success() throws Exception {
        UserRegister register = UserRegister.builder()
                .fullName("Bui Son")
                .password("12345678")
                .email("sonbui@gmail.com")
                .phone("0123456789")
                .build();

        User user = User.builder()
                .id(1L)
                .email("sonbui@gmail.com")
                .build();

        Mockito.when(authService.register(Mockito.any(UserRegister.class)))
                .thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(user.getId()));
        Mockito.verify(authService).register(Mockito.any(UserRegister.class));
    }

    // Đăng kí với email đã tồn tại
    @Test
    void register_ExistEmail() throws Exception {
        UserRegister register = UserRegister.builder()
                .fullName("Bui Son")
                .password("12345678")
                .email("sonbui@gmail.com")
                .phone("0123456789")
                .build();

        Mockito.when(authService.register(register))
                .thenThrow(new ExistException("Email đã tồn tại"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email đã tồn tại"))
        ;
    }

    // Đăng kí với số điện thoại đã tồn tại
    @Test
    void register_ExistPhone() throws Exception {
        UserRegister register = UserRegister.builder()
                .fullName("Bui Son")
                .password("12345678")
                .email("sonbui@gmail.com")
                .phone("0123456789")
                .build();

        Mockito.when(authService.register(register))
                .thenThrow(new ExistException("Số điện thoại đã tồn tại"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Số điện thoại đã tồn tại"));
    }

    // Đăng kí để trống 1 trường bất kì
    @Test
    void register_NotFullName() throws Exception {
        UserRegister register = UserRegister.builder()
                .password("12345678")
                .email("sonbui@gmail.com")
                .phone("0123456789")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
    }

    // Đăng nhập thành công
    @Test
    void login_Success() throws Exception {
        UserLogin login = UserLogin.builder()
                .email("sonbui@gmail.com")
                .password("12345678")
                .build();

        JwtResponse response = JwtResponse.builder()
                .accessToken("token")
                .refreshToken("token")
                .build();

        Mockito.when(authService.login(login))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value("token"))
        ;
    }

    // Đăng nhập với email không tồn tại
    @Test
    void login_NotExistEmail() throws Exception {
        UserLogin login = UserLogin.builder()
                .email("sonbui@gmail.com")
                .password("12345678")
                .build();

        Mockito.when(authService.login(login))
                .thenThrow(new UsernameNotFoundException("Thông tin đăng nhập không chính xác"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Thông tin đăng nhập không chính xác"));
    }

}
