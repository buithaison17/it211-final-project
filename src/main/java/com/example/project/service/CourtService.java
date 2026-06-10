package com.example.project.service;

import com.example.project.enums.BookingStatus;
import com.example.project.exception.NotFoundException;
import com.example.project.model.dto.request.BookingRequest;
import com.example.project.model.dto.request.CourtRequest;
import com.example.project.model.dto.request.UploadImageCourtRequest;
import com.example.project.model.entity.Booking;
import com.example.project.model.entity.Court;
import com.example.project.model.entity.CourtImage;
import com.example.project.repository.BookingRepository;
import com.example.project.repository.CourtImageRepository;
import com.example.project.repository.CourtRepository;
import com.example.project.security.principal.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourtService {
    private final CourtRepository courtRepository;
    private final UploadImageService uploadImageService;
    private final CourtImageRepository courtImageRepository;

    public Court createCourt(CourtRequest request) {
        Court court = Court.builder()
                .name(request.getName())
                .location(request.getLocation())
                .description(request.getDescription())
                .price(request.getPrice())
                .enabled(true)
                .build();
        return courtRepository.save(court);
    }

    public Court findById(Long id) {
        return courtRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy sân"));
    }

    public Court uploadImages(Long courtId, List<MultipartFile> request) {
        Court court = findById(courtId);
        List<String> images = uploadImageService.uploadList(request);
        List<CourtImage> courtImages = images.stream().map(image -> CourtImage.builder().imageUrl(image).court(court).build()).toList();
        courtImageRepository.saveAll(courtImages);
        return court;
    }
}
