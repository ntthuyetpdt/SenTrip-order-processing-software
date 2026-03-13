package com.example.da_sentrip.helper;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Map;
@Service
@RequiredArgsConstructor


public class CloudinaryPDF {
    private final Cloudinary cloudinary;


    public String uploadPdf(byte[] fileBytes, String fileName) {
        try {
            String publicId = fileName.replace(".pdf", "");

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    fileBytes,
                    Map.of(
                            "resource_type", "raw",
                            "folder", "invoices",
                            "public_id", publicId,
                            "format", "pdf"
                    )
            );

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new RuntimeException("Cloudinary không trả về secure_url");
            }

            String url = secureUrl.toString();
            if (!url.endsWith(".pdf")) {
                url = url + ".pdf";
            }

            return url;

        } catch (IOException e) {
            throw new RuntimeException("Upload PDF to Cloudinary failed: " + e.getMessage(), e);
        }
    }
}
