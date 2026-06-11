package com.example.project.service;

import com.example.project.exception.ExistException;
import com.example.project.exception.NotFoundException;
import com.example.project.model.dto.request.CreateUserRequest;
import com.example.project.model.dto.request.UpdateUserRequest;
import com.example.project.model.dto.response.UserProjectionDTO;
import com.example.project.model.entity.User;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Lấy danh sách người dùng phân trang + tìm kiếm
    public Page<User> getListUser(Integer currentPage, Integer pageSize, String keyword) {
        Sort sort = Sort.by(keyword);
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        return userRepository.findAll(pageable);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Người dùng không tồn tại"));
    }

    // Xem chi tiết
    public UserProjectionDTO getUserById(Long id) {
        User user = findById(id);
        return UserProjectionDTO.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }


    // Vô hiệu hoá tài khoản của người dùng
    public User disableUser(Long id) {
        User user = findById(id);
        user.setEnabled(false);
        return userRepository.save(user);
    }

    // Tạo tài khoản
    public User createUser(CreateUserRequest request) {
        // Kiểm tra email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ExistException("Email đã tồn tại");
        }
        // Kiểm tra số điện thoại
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new ExistException("Số điện thoại đã tồn tại");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .roles(request.getRoles())
                .enabled(true)
                .build();
        return userRepository.save(user);
    }

    // Cập nhật tài khoản
    public User updateUser(Long id, UpdateUserRequest request) {
        // Kiểm tra email
        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ExistException("Email đã tồn tại");
        }
        // Kiểm tra số điện thoại
        if (userRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new ExistException("Số điện thoại đã tồn tại");
        }
        User user = findById(id);
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            user.setPhone(request.getPhone());
        }
        return userRepository.save(user);
    }
}
