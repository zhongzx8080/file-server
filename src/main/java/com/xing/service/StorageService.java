package com.xing.service;

import com.xing.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void store(MultipartFile file, Image image);

    String getGalleryPath();
}
