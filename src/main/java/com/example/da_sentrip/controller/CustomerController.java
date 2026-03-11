package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.CustomerDTO;
import com.example.da_sentrip.model.dto.reponse.CustomerReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.reponse.TicketReponseDTO;
import com.example.da_sentrip.model.dto.request.CustomerRequestDTO;
import com.example.da_sentrip.service.CustomerService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/getAll")
    public SuccessResponse<List<CustomerReponseDTO>> getAll(){
        List<CustomerReponseDTO> customers = customerService.getAll();
        return new SuccessResponse<>(
                Constants.HTTP_STATUS.SUCCESS,
                "Get all customers success",
                customers
        );
    }

    @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<?> update(
            @PathVariable Long id,
            @RequestParam CustomerRequestDTO request,
            @RequestPart(value = "img", required = false) MultipartFile img,
            Authentication authentication ) {

        try {
           customerService.update( id,request, img,authentication);
            return new  SuccessResponse<>(
                    Constants.HTTP_STATUS.SUCCESS,
            "update success",
                    null);

        } catch (Exception ex) {
            return new  SuccessResponse<>(
                    Constants.HTTP_STATUS.BAD_REQUEST,
            "update failed",
                    null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<?>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone) {
        try {
            List<CustomerDTO> result = customerService.search(fullName, address, phone);
            return ResponseEntity.ok(new SuccessResponse<>(
                    Constants.HTTP_STATUS.SUCCESS,
                    "Search success",
                    result
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(Constants.HTTP_STATUS.BAD_REQUEST)
                    .body(new SuccessResponse<>(
                            Constants.HTTP_STATUS.BAD_REQUEST,
                            "Search failed",
                            null
                    ));
        }
    }
    @GetMapping("/ticketsMy")
    public ResponseEntity<ResponseDTO<?>> getMyTickets(Authentication authentication) {
        try {
            List<TicketReponseDTO> tickets = customerService.getTicket(authentication);
            return ResponseEntity.ok(
                    ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("view ticket success")
                    .data(tickets)
                    .build()
            );
        } catch (Exception ex) {
            return ResponseEntity.status(Constants.HTTP_STATUS.BAD_REQUEST)
                    .body(
                    ResponseDTO.builder()
                    .status("fail")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("view ticket failed")
                    .data(null)
                    .build()
                    );
        }
    }
}
