package com.example.da_sentrip.helper;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.da_sentrip.model.entity.DataSource;
import com.example.da_sentrip.repository.DataSourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaStorageService {

    private final Cloudinary cloudinary;
    private final DataSourceRepository dataSourceRepository;

    @Transactional
    public String uploadMedia(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null ||
                    (!contentType.startsWith("image") && !contentType.startsWith("video"))) {
                throw new RuntimeException("Invalid media type");
            }

            boolean isImage = contentType.startsWith("image");

            DataSource dataSource = new DataSource();
            dataSource.setMediaType(isImage ? "IMAGE" : "VIDEO");
            dataSource.setData(isImage ? file.getBytes() : null);

            dataSource = dataSourceRepository.save(dataSource);

            String publicId = (isImage ? "image_" : "video_") + dataSource.getId();

            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", isImage ? "images" : "videos",
                            "public_id", publicId,
                            "resource_type", isImage ? "image" : "video"
                    )
            );

            dataSource.setPublicId(publicId);
            dataSource.setImageUrl(result.get("secure_url").toString());
            dataSourceRepository.save(dataSource);

            return String.valueOf(dataSource.getId());

        } catch (Exception e) {
            throw new RuntimeException("Upload media failed", e);
        }
    }

    @Transactional
    public void deleteMedia(Long dataSourceId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> new RuntimeException("DataSource not found"));

        try {
            if (dataSource.getPublicId() != null) {
                cloudinary.uploader().destroy(
                        dataSource.getPublicId(),
                        ObjectUtils.asMap(
                                "resource_type",
                                "VIDEO".equals(dataSource.getMediaType()) ? "video" : "image"
                        )
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Delete media failed", e);
        }

        dataSourceRepository.delete(dataSource);
    }
}
