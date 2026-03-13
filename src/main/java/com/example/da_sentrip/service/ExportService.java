package com.example.da_sentrip.service;

public interface ExportService {
    byte[] exportOrdersExcel();
    String exportInvoicePdf(String orderCode);
}
