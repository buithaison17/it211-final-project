package com.example.project.service;

import com.example.project.exception.NotFoundException;
import com.example.project.exception.RefreshTokenInvalidException;
import com.example.project.model.dto.response.JwtResponse;
import com.example.project.model.entity.RefreshToken;
import com.example.project.model.entity.User;
import com.example.project.repository.RefreshTokenRepository;
import com.example.project.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    @Value("${jwt.refresh_expiration}")
    private Long expiration;

    public RefreshToken generateToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiresAt(new Date(System.currentTimeMillis() + expiration))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Token not found"));
    }

    public Boolean verifyToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        if (refreshToken.getRevoked() || refreshToken.getExpiresAt().before(new Date())) {
            throw new RefreshTokenInvalidException("Refresh token đã hết hạn hoặc bị thu hồi");
        }
        return true;
    }

    public JwtResponse refresh(String token) {
        if (verifyToken(token)) {
            RefreshToken refreshToken = findByToken(token);
            String accessToken = jwtProvider.generateToken(refreshToken.getUser().getEmail());
            return new JwtResponse(
                    accessToken,
                    token
            );
        }
        return null;
    }
}
