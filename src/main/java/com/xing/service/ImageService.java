package com.xing.service;


import com.xing.entity.Image;

public interface ImageService {
    void addImage(Image image);

    Image getImage(String id);
}
