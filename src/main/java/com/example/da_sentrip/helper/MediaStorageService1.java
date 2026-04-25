//package com.example.da_sentrip.helper;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import com.example.da_sentrip.model.entity.DataSource;
//import com.example.da_sentrip.repository.DataSourceRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class MediaStorageService1 {
//
//    private final Cloudinary cloudinary;
//    private final DataSourceRepository dataSourceRepository;
//
//    @Transactional
//    public String uploadMedia(MultipartFile file) {
//        try {
//            if (file == null || file.isEmpty()) {
//                throw new RuntimeException("File is empty");
//            }
//
//            String contentType = file.getContentType();
//            if (contentType == null) {
//                throw new RuntimeException("Cannot detect file type");
//            }
//
//            String mediaType;
//            if (contentType.startsWith("image")) {
//                mediaType = "IMAGE";
//            } else if (contentType.startsWith("video")) {
//                mediaType = "VIDEO";
//            } else if (contentType.equals("application/pdf")) {
//                mediaType = "PDF";
//            } else {
//                mediaType = "OTHER";
//            }
//
//            DataSource dataSource = new DataSource();
//            dataSource.setMediaType(mediaType);
//            dataSource = dataSourceRepository.save(dataSource);
//
//            String publicId = "media_" + dataSource.getId();
//
//            Map<?, ?> result = cloudinary.uploader().upload(
//                    file.getBytes(),
//                    ObjectUtils.asMap(
//                            "folder", "media",
//                            "public_id", publicId,
//                            "resource_type", "auto"
//                    )
//            );
//
//            dataSource.setPublicId(publicId);
//            dataSource.setImageUrl(result.get("secure_url").toString());
//            dataSourceRepository.save(dataSource);
//
//            return String.valueOf(dataSource.getId());
//
//        } catch (Exception e) {
//            throw new RuntimeException("Upload media failed: " + e.getMessage(), e);
//        }
//    }
//
//    @Transactional
//    public void deleteMedia(Long dataSourceId) {
//        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
//                .orElseThrow(() -> new RuntimeException("DataSource not found"));
//
//        try {
//            if (dataSource.getPublicId() != null) {
//                cloudinary.uploader().destroy(
//                        dataSource.getPublicId(),
//                        ObjectUtils.asMap("resource_type", "auto")
//                );
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Delete media failed: " + e.getMessage(), e);
//        }
//
//        dataSourceRepository.delete(dataSource);
//    }
//}