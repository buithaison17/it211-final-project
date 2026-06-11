package com.example.project.service;

import com.example.project.exception.ExistException;
import com.example.project.exception.NotFoundException;
import com.example.project.model.dto.request.CreateUserRequest;
import com.example.project.model.dto.response.UserProjectionDTO;
import com.example.project.model.entity.User;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    // Tìm người dùng theo ID thành công
    @Test
    void findById_Success() {
        User user = User.builder()
                .id(1L)
                .build();
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        User result = userService.findById(1L);
        Assertions.assertEquals(user, result);
    }

    // Tìm người dùng theo ID thành công
    @Test
    void findById_NotFound() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }

    // Lấy danh sách người dùng
    @Test
    void getListUser() {
        UserProjectionDTO userProjectionDTO = UserProjectionDTO.builder()
                .fullName("Bùi Thái Sơn")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserProjectionDTO> page = new PageImpl<>(
                List.of(userProjectionDTO),
                pageable,
                1
        );
        Mockito.when(userRepository.findByKeyword("", pageable))
                .thenReturn(page);

        Page<UserProjectionDTO> result = userService.getListUser(1, 10, "");
        Assertions.assertEquals(page, result);
    }

    // Tạo tài khoản người dùng trùng email
    @Test
    void createUser_ExistEmail() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email("sonbui@gmail.com")
                .build();
        User existUser = User.builder().email("sonbui@gmail.com").build();
        Mockito.when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(existUser));
        // Nếu trùng ném ra ngoại lệ
        Assertions.assertThrows(ExistException.class, () -> userService.createUser(request));
        // Chắc chắn không save vào database
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    // Tạo tài khoản trùng số điện thoại
    @Test
    void createUser_ExistPhone() {
        CreateUserRequest request = CreateUserRequest.builder()
                .phone("0329967878")
                .build();
        User existUser = User.builder()
                .phone("0329967878")
                .build();
        Mockito.when(userRepository.findByPhone(request.getPhone()))
                .thenReturn(Optional.of(existUser));
        // Nếu trùng số điện thoại ném ra ngoaại lệ
        Assertions.assertThrows(ExistException.class, () -> userService.createUser(request));
        // Đảm bảo không lưu vào database
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    // Tạo tài khoản thành công
    @Test
    void createUser_Sucess() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email("sonbui@gmail.com")
                .password("123456")
                .build();

        Mockito.when(userRepository.findByEmail("sonbui@gmail.com"))
                .thenReturn(Optional.empty());

        Mockito.when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        User saveUser = User.builder()
                .email(request.getEmail())
                .password("encodedPassword")
                .build();

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(saveUser);

        User result = userService.createUser(request);
        Assertions.assertEquals(saveUser, result);

    }
}

