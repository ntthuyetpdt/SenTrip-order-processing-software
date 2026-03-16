package com.example.da_sentrip.controller;

import com.example.da_sentrip.service.PayoutRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/payout")
@RequiredArgsConstructor
public class PayoutController {

    private final PayoutRequestService payoutRequestService;

    @PostMapping("/request")
    @PreAuthorize("hasAnyAuthority('MERCHANT_REQUEST')")
    public ResponseEntity<?> createPayoutRequest(
            @RequestParam String orderCode,
            Authentication authentication) {
        String gmail = authentication.getName();
        payoutRequestService.createPayoutRequest(orderCode, gmail);
        return ResponseEntity.ok(Map.of("message", "Payment request has been submitted."));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ACCOUNTANT_VIEW_ALL')")
    public ResponseEntity<?> getAllPayoutRequests(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "message", "View success",
                "data", payoutRequestService.getAllPayoutRequests()
        ));
    }

    @PostMapping(value = "/process/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ACCOUNTANT_PROCESS')")
    public ResponseEntity<?> processPayout(
            @PathVariable Long id,
            @RequestParam("img") MultipartFile file,
            Authentication authentication) {
        String gmail = authentication.getName();
        payoutRequestService.processPayout(id, file, gmail);
        return ResponseEntity.ok(Map.of("message", "Payment confirmed successfully."));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('MERCHANT_VIEW_BANK')")
    public ResponseEntity<?> getMyPayoutRequests(Authentication authentication) {
        String gmail = authentication.getName();
        return ResponseEntity.ok(Map.of(
                "message", "View success",
                "data", payoutRequestService.getMyPayoutRequests(gmail)
        ));
    }
}