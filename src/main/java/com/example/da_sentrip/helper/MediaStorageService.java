package com.example.da_sentrip.helper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.da_sentrip.model.entity.DataSource;
import com.example.da_sentrip.repository.DataSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaStorageService {

    private final Cloudinary cloudinary;
    private final DataSourceRepository dataSourceRepository;

    public String uploadMedia(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null) {
                throw new RuntimeException("Invalid file type");
            }

            boolean isImage = contentType.startsWith("image");
            boolean isVideo = contentType.startsWith("video");

            if (!isImage && !isVideo) {
                throw new RuntimeException("Only image or video is allowed");
            }
            DataSource dataSource = new DataSource();
            dataSource.setMediaType(isImage ? "IMAGE" : "VIDEO");
            if (isImage) {
                dataSource.setData(file.getBytes());
            }
            dataSource = dataSourceRepository.save(dataSource);
            String publicId = (isImage ? "image_" : "video_") + dataSource.getId();
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", isImage ? "images" : "videos",
                            "public_id", publicId,
                            "resource_type", isImage ? "image" : "video"
                    )
            );
            dataSource.setImageUrl(uploadResult.get("secure_url").toString());
            dataSource.setPublicId(publicId);
            dataSourceRepository.save(dataSource);
            return dataSource.getImageUrl();

        } catch (Exception e) {
            throw new RuntimeException("Upload media failed", e);
        }
    }
    public void deleteMedia(Long dataSourceId) {
        try {
            DataSource dataSource = dataSourceRepository.findById(dataSourceId).orElseThrow(() -> new RuntimeException("DataSource not found"));
            if (dataSource.getPublicId() != null) {
                cloudinary.uploader().destroy(
                        dataSource.getPublicId(),
                        ObjectUtils.asMap(
                                "resource_type",
                                "VIDEO".equals(dataSource.getMediaType()) ? "video" : "image"
                        )
                );
            }

            dataSourceRepository.delete(dataSource);

        } catch (Exception e) {
            throw new RuntimeException("Delete media failed", e);
        }
    }
}
