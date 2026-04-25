//package com.example.da_sentrip.helper;
//
//import com.cloudinary.Cloudinary;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CloudinaryPDF {
//
//    private final Cloudinary cloudinary;
//
//    public String uploadPdf(byte[] fileBytes, String fileName) {
//        try {
//            Map<?, ?> result = cloudinary.uploader().upload(fileBytes, Map.of(
//                    "resource_type", "raw",
//                    "folder", "invoices",
//                    "public_id", fileName.replace(".pdf", ""),
//                    "format", "pdf"
//            ));
//
//            String url = (String) Optional.ofNullable(result.get("secure_url"))
//                    .orElseThrow(() -> new RuntimeException("Cloudinary không trả về secure_url"));
//
//            return url.endsWith(".pdf") ? url : url + ".pdf";
//
//        } catch (IOException e) {
//            throw new RuntimeException("Upload PDF to Cloudinary failed: " + e.getMessage(), e);
//        }
//    }
//}
