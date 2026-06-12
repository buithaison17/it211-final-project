package com.example.project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.project.exception.ImageExtensionInvalidException;
import io.netty.util.internal.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadImageService {
    private final Cloudinary cloudinary;
    @Value("${imageUploadDir}")
    private String imageUploadDir;

    public boolean verifyImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (!fileExtension.equals(".jpg") && !fileExtension.equals(".png") && !fileExtension.equals(".jpeg")) {
            throw new ImageExtensionInvalidException("Ảnh không đúng định dạng");
        }
        return true;
    }

    public String uploadImage(MultipartFile file) {
        // Kiểm tra ảnh
        if (!verifyImage(file)) {
            return null;
        }

        try {
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "images",
                    "resource_type", "auto"
            )).get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> uploadList(List<MultipartFile> files) {
        // Kiểm tra định dạng ảnh
        for (MultipartFile file : files) {
            verifyImage(file);
        }
        List<String> images = new ArrayList<>();
        // Tiến hảnh upload khi không có ảnh nào bị lỗi
        for (MultipartFile file : files) {
            String url = uploadImage(file);
            if (url != null) {
                images.add(url);
            }
        }
        return images;
    }
}