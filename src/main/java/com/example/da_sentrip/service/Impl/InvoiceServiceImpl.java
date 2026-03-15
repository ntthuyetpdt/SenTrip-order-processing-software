package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.CloudinaryPDF;
import com.example.da_sentrip.model.dto.reponse.view.InvoiceDetailProjection;
import com.example.da_sentrip.model.dto.reponse.view.InvoiceProjection;
import com.example.da_sentrip.model.entity.Invoices;
import com.example.da_sentrip.repository.InvoiceRepository;
import com.example.da_sentrip.repository.OrderRepository;
import com.example.da_sentrip.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final CloudinaryPDF cloudinaryPDF;

    @Override
    public List<InvoiceDetailProjection> getAllInvoices() {
        return invoiceRepository.findAllInvoiceDetail();
    }

    @Override
    @Transactional
    public String uploadInvoicePdf(MultipartFile file, String orderCode) {
        if (file == null || file.isEmpty())
            throw new RuntimeException("File không được để trống");
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".pdf"))
            throw new RuntimeException("Chỉ chấp nhận file PDF");

        InvoiceProjection invoice = orderRepository.findInvoiceByOrderCode(orderCode);
        if (invoice == null)
            throw new RuntimeException("Không tìm thấy đơn hàng: " + orderCode);

        try {
            String invoiceCode = "INV" + System.currentTimeMillis();
            String fileName = invoiceCode + ".pdf";
            String fileUrl = cloudinaryPDF.uploadPdf(file.getBytes(), fileName);

            Invoices entity = new Invoices();
            entity.setInvoiceCode(invoiceCode);
            entity.setOderId(invoice.getOrderId());
            entity.setMerchantId(invoice.getMerchantId());
            entity.setAmount(invoice.getTotalAmount());
            entity.setStatus("GENERATED");
            entity.setPdfPart("CLOUDINARY");
            entity.setFileName(fileName);
            entity.setFileUrl(fileUrl);
            entity.setGeneratedAt(LocalDateTime.now());
            entity.setCreatedAt(LocalDateTime.now());
            invoiceRepository.save(entity);

            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file PDF: " + e.getMessage(), e);
        }
    }
}