package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.CustomerDTO;
import com.example.da_sentrip.model.dto.reponse.CustomerReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.CustomerRequestDTO;
import com.example.da_sentrip.service.CustomerService;
import com.example.da_sentrip.service.ProductService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final ProductService productService;

    @GetMapping("/getall")
    public ResponseEntity<SuccessResponse<List<CustomerReponseDTO>>> getAll() {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Get all success", customerService.getAll()));
    }

    @PostMapping(value = "update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('CUSTOMER_UPDATE_PROFILE')")
    public ResponseEntity<ResponseDTO> update(
            @PathVariable Long id,
            @RequestParam CustomerRequestDTO request,
            @RequestPart(required = false) MultipartFile img,
            Authentication authentication) {
        customerService.update(id, request, img, authentication);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS).message("Update success").build());
    }
//DMIN/
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<CustomerDTO>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone) {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Search success", customerService.search(fullName, address, phone)));
    }

    @GetMapping("/tickets")
    @PreAuthorize("hasAnyAuthority('CUSTOMER_VIEW_TICKET')")
    public ResponseEntity<ResponseDTO> getMyTickets(Authentication authentication) {
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Get tickets success")
                .data(customerService.getTicket(authentication))
                .build());
    }

}