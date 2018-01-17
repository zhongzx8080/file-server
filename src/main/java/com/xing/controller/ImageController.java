package com.xing.controller;

import com.xing.entity.Image;
import com.xing.service.ImageService;
import com.xing.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImageService imageService;


    @PostMapping("/upload")
    public Image addImage(@RequestParam("image") MultipartFile file) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");

        int index = file.getOriginalFilename().lastIndexOf(".");
        String suffix = file.getOriginalFilename().substring(index);
        String filename = id + suffix;

        Image image = new Image();
        image.setId(id);
        image.setFilename(filename);

        storageService.store(file, image);

        imageService.addImage(image);

        return image;
    }

    @GetMapping(value = "/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {

        Image image = imageService.getImage(id);

        if (Objects.isNull(image)) {
            try {
                response.getWriter().write("无此id");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        response.setContentType("image/jpeg");

        String filename = image.getFilename();
        Path path = Paths.get(storageService.getGalleryPath(), filename);
        try {
            byte[] bytes = Files.readAllBytes(path);
            response.getOutputStream().write(bytes);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
