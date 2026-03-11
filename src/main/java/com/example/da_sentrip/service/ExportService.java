package com.example.da_sentrip.service;

public interface ExportService {
    public byte[] exportInvoicePdf(String orderCode);
    byte[] exportOrdersExcel();
}
