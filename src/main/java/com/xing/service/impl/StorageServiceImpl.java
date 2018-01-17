package com.xing.service.impl;

import com.xing.entity.Image;
import com.xing.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageServiceImpl implements StorageService {

    @Value("${win.gallery.dir}")
    private String winGallery;

    @Value("${linux.gallery.dir}")
    private String linuxGallery;

    private Path path = null;

    @Override
    public void store(MultipartFile file, Image image) {

        if (file.isEmpty()) {
            throw new RuntimeException("Fail to store empty file");
        }

        try {
            path = Paths.get(getGalleryPath(), image.getFilename());
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getGalleryPath() {
        String osname = System.getProperty("os.name");
        String galleryPath = null;
        if (osname.startsWith("Windows")) {
            // 在 Windows 操作系统上
            galleryPath = winGallery;
        } else if (osname.startsWith("Linux")) {
            // 在 Linux 操作系统上
            galleryPath = linuxGallery;
        }
        return galleryPath;
    }
}
