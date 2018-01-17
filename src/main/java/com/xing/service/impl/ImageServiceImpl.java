package com.xing.service.impl;

import com.xing.entity.Image;
import com.xing.mapper.ImageMapper;
import com.xing.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Override
    public void addImage(Image image) {
        imageMapper.addImage(image);
    }

    @Override
    public Image getImage(String id) {
        return imageMapper.getImage(id);
    }
}
