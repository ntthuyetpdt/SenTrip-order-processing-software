package com.example.da_sentrip.helper;

import com.example.da_sentrip.model.entity.DataSource;
import com.example.da_sentrip.repository.DataSourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MediaStorageService {

    private final DataSourceRepository dataSourceRepository;
    private final RestTemplate restTemplate;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    @Transactional
    public String uploadMedia(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null) {
                throw new RuntimeException("Cannot detect file type");
            }

            // Xác định loại media
            String mediaType;
            if (contentType.startsWith("image")) {
                mediaType = "IMAGE";
            } else if (contentType.startsWith("video")) {
                mediaType = "VIDEO";
            } else if (contentType.equals("application/pdf")) {
                mediaType = "PDF";
            } else {
                mediaType = "OTHER";
            }

            // Lưu DataSource trước để lấy ID
            DataSource dataSource = new DataSource();
            dataSource.setMediaType(mediaType);
            dataSource = dataSourceRepository.save(dataSource);

            // Tạo tên file unique
            String extension = getExtension(file.getOriginalFilename());
            String fileName = mediaType.toLowerCase() + "_" + dataSource.getId() + extension;
            String filePath = fileName;

            // Upload lên Supabase Storage
            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + filePath;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            headers.setContentType(MediaType.parseMediaType(contentType));

            HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
            restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);

            // URL public để xem trực tiếp — PDF xem được luôn, không lỗi
            String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + filePath;

            dataSource.setPublicId(filePath);
            dataSource.setImageUrl(publicUrl);
            dataSourceRepository.save(dataSource);

            return String.valueOf(dataSource.getId());

        } catch (Exception e) {
            throw new RuntimeException("Upload media failed: " + e.getMessage(), e);
        }
    }

    /**
     * Trả về URL xem file trực tiếp trên browser
     * PDF, ảnh, video đều xem được, không bị lỗi
     */
    public String getViewUrl(Long dataSourceId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> new RuntimeException("DataSource not found"));
        return dataSource.getImageUrl();
    }

    @Transactional
    public void deleteMedia(Long dataSourceId) {
        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> new RuntimeException("DataSource not found"));

        try {
            if (dataSource.getPublicId() != null) {
                String deleteUrl = supabaseUrl + "/storage/v1/object/" + bucket
                        + "/" + dataSource.getPublicId();

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + supabaseKey);
                headers.set("apikey", supabaseKey);

                restTemplate.exchange(
                        deleteUrl,
                        HttpMethod.DELETE,
                        new HttpEntity<>(headers),
                        String.class
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Delete media failed: " + e.getMessage(), e);
        }

        dataSourceRepository.delete(dataSource);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}