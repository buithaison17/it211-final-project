package com.example.project.service;

import com.cloudinary.api.exceptions.NotFound;
import com.example.project.exception.ExistException;
import com.example.project.exception.NotFoundException;
import com.example.project.exception.NotMatchOTPException;
import com.example.project.exception.PasswordIncorrectException;
import com.example.project.model.dto.request.*;
import com.example.project.model.dto.response.JwtResponse;
import com.example.project.model.entity.RefreshToken;
import com.example.project.model.entity.Role;
import com.example.project.model.entity.User;
import com.example.project.repository.RefreshTokenRepository;
import com.example.project.repository.UserRepository;
import com.example.project.security.jwt.JwtProvider;
import com.example.project.security.principal.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest httpServletRequest;
    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final EmailService emailService;

    // Đăng kí (user tự đăng ký)
    public User register(UserRegister userRegister) {
        // Kiểm tra email
        if (userRepository.findByEmail(userRegister.getEmail()).isPresent()) {
            throw new ExistException("Email đã tồn tại");
        }
        // Kiểm tra số điện thoại
        if (userRepository.findByPhone(userRegister.getPhone()).isPresent()) {
            throw new ExistException("Số điện thoại đã tồn tại");
        }
        User user = User.builder().fullName(userRegister.getFullName()).email(userRegister.getEmail()).password(passwordEncoder.encode(userRegister.getPassword())).phone(userRegister.getPhone()).roles(Set.of(Role.builder().id(1L).build())).enabled(true).build();
        return userRepository.save(user);
    }

    // Đăng nhập
    public JwtResponse login(UserLogin userLogin) {
        // Xác thực thông tin đăng nhập
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Tạo access token và refresh token khi đăng nhập thành công
        String accessToken = jwtProvider.generateToken(userPrincipal.getUsername());
        RefreshToken refreshToken = refreshTokenService.generateToken(userPrincipal.getUser());

        // Trả về
        return JwtResponse.builder().accessToken(accessToken).refreshToken(refreshToken.getToken()).build();
    }

    public void logout(String token) {
        // Lấy refresh token
        RefreshToken refreshToken = refreshTokenService.findByToken(token);
        // Lấy access token từ header ném lỗi nếu không tìm thấy
        String accessTokenHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = accessTokenHeader.substring(7);
        // Lấy username tù access token làm value
        String username = jwtProvider.getUsernameFromToken(accessToken);
        // Thu hồi refresh token và lưu access token vào redis
        refreshToken.setRevoked(true);
        redisTemplate.opsForValue().set(accessToken, username, Duration.ofMillis(300000));
        // Lưu lại
        refreshTokenRepository.save(refreshToken);
    }

    public User changePassword(ChangePasswordRequest request) {
        // Lấy mật khẩu dùng hiện tại
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userPrincipal.getUser();
        String oldPassword = userPrincipal.getPassword();
        // Mật khẩu cũ phải khớp
        if (!passwordEncoder.matches(request.getCurrentPassword(), oldPassword)) {
            throw new PasswordIncorrectException("Mật khẩu cũ không chính xác");
        }
        // Mật khẩu mới phải khớp
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordIncorrectException("Mật khẩu xác nhận không trùng mật khẩu mới");
        }
        // Đổi mật khẩu
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Lưu lại
        return userRepository.save(user);
    }

    // Quên mật khẩu
    public void forgetPassword(ForgetPasswordRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new NotFoundException("Email không tồn tại");
        }
        // Tạo mã OTP lưu trong redis và gửi mail cho người dùng
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        // OTP có hiệu lực 10p
        redisTemplate.opsForValue().set(request.getEmail() + "otp", otp, Duration.ofMinutes(10));
        // Gửi otp cho người dùng
        emailService.sendEmail(request.getEmail(), "Quên mật khẩu", "Mã OTP của bạn là: " + otp);
    }

    // Xác nhận khi email khi quên mật khẩu
    public void verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        // Kiểm tra email
        if (user == null) {
            throw new NotFoundException("Email không tồn tại");
        }
        // Lấy otp có trong redis
        String otp = redisTemplate.opsForValue().get(request.getEmail() + "otp");
        // Kiểm tra
        if (!request.getOtp().equals(otp)) {
            throw new NotMatchOTPException("Mã OTP không chính xác");
        }
        // Xác thực thành công cấp lại mật khẩu mới và gửi về mail
        String password = "12345678";
        user.setPassword(passwordEncoder.encode(password));
        // Lưu lại
        userRepository.save(user);
        // Gửi mail
        emailService.sendEmail(request.getEmail(), "Cấp lại mật khẩu", "Mật khẩu mới của bạn là: " + password);
    }
}
