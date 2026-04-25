package com.example.da_sentrip.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SupabasePDF {

    private final RestTemplate restTemplate;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    public String uploadPdf(byte[] fileBytes, String fileName) {
        try {
            String cleanFileName = removeVietnameseAccents(fileName)
                    .replaceAll("\\s+", "_")
                    .replaceAll("[^a-zA-Z0-9._-]", "")
                    .toLowerCase();

            if (!cleanFileName.endsWith(".pdf")) {
                cleanFileName = cleanFileName + ".pdf";
            }

            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + cleanFileName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set("x-upsert", "true");

            HttpEntity<byte[]> entity = new HttpEntity<>(fileBytes, headers);
            restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);

            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + cleanFileName;

        } catch (Exception e) {
            throw new RuntimeException("Upload PDF to Supabase failed: " + e.getMessage(), e);
        }
    }

    private String removeVietnameseAccents(String text) {
        if (text == null) return "file";
        String normalized = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("đ", "d")
                .replace("Đ", "D");
    }
}