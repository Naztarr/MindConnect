package com.mindconnect.mindconnect.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mindconnect.mindconnect.services.CloudinaryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Resource
    private Cloudinary cloudinary;
    @Override
    public String uploadFile(MultipartFile file) {
        try{
            Map uploadedFile = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "public_id", file.getOriginalFilename(),
                            "use_filename", true,
                            "unique_filename", false,
                            "overwrite", true
                    )
            );
            log.info("uploadedFile response {}", uploadedFile );
            String secureUrl = (String) uploadedFile.get("secure_url");
            return cloudinary.url().secure(true).generate(secureUrl);

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
