package com.blogapp.blogsphere.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) { //MultipartFile file
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),  //Converts file to bytes so Cloudinary can read it
                    ObjectUtils.asMap("folder", "blogsphere")//Saves all uploads inside a folder called "blogsphere" in Cloudinary

            );
            return uploadResult.get("secure_url").toString(); //Cloudinary returns a URL after upload
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed!");
        }
    }
}