package com.example.da_sentrip.model.enums;

public enum OrderStatus {
    PENDING,// chờ xác nhận
    PENDING_PAYMENT,   // Chờ thanh toán
    PAID,              // Đã thanh toán
    COMPLETED,         // Hoàn thành
    CANCELLED,         // Hủy vé
    REFUND_REQUESTED,  // Yêu cầu hoàn vé
    REFUNDED// hoàn tiền
}
