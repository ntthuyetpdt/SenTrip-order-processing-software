package com.example.da_sentrip.helper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.da_sentrip.model.entity.DataSource;
import com.example.da_sentrip.repository.DataSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class MediaStorageService {// hàm chuyển đổi anh dạng base 64 lên cloudinary sau đó chuyển sang dạng url

    private final DataSourceRepository dataSourceRepository;
    private final Cloudinary cloudinary;

    public String saveBase64ImageAndReturnUrl(String base64Image) {
        try {
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            DataSource dataSource = new DataSource();
            dataSource.setData(imageBytes);
            dataSource = dataSourceRepository.save(dataSource);
            Long dataSourceId = dataSource.getId();
            Map uploadResult = cloudinary.uploader().upload(
                    imageBytes,
                    ObjectUtils.asMap(
                            "folder", "user-images",
                            "public_id", "user_" + dataSourceId,
                            "resource_type", "image"
                    )
            );
            String cloudinaryUrl =
                    uploadResult.get("secure_url").toString();
            dataSource.setImageUrl(cloudinaryUrl);
            dataSourceRepository.save(dataSource);
            return cloudinaryUrl;

        } catch (Exception e) {
            throw new RuntimeException("Upload image failed", e);
        }
    }
}