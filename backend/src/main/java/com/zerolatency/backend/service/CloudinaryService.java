package com.zerolatency.backend.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


@Service
public class CloudinaryService {
    @Autowired 
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file){
        try{
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "products"));
            return uploadResult.get("secure_url").toString(); 
        } catch (IOException e){
            throw new RuntimeException("Image failed to upload" + e.getMessage());
        }
    }
    
}
