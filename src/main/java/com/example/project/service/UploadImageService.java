package com.example.project.service;

import com.example.project.exception.ImageExtensionInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UploadImageService {
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

        File dir = new File(imageUploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String newFileName = UUID.randomUUID().toString() + file.getOriginalFilename();
        File dest = new File(imageUploadDir, newFileName);

        try {
            file.transferTo(dest);
            return "/images/" + newFileName;
        } catch (IOException e) {
            return null;
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
            File dir = new File(imageUploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String newFileName = UUID.randomUUID().toString() + file.getOriginalFilename();
            File dest = new File(imageUploadDir, newFileName);

            try {
                file.transferTo(dest);
                images.add("/images/" + newFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return images;
    }
}