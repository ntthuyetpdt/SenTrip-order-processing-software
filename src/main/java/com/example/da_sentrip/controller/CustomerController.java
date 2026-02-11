package com.example.da_sentrip.controller;


import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.CustomerDTO;
import com.example.da_sentrip.model.dto.reponse.CustomerReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.CustomerRequestDTO;
import com.example.da_sentrip.service.CustomerService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/getAll")
    public SuccessResponse<?> getAll(){
        List<CustomerReponseDTO> user =customerService.getAll();
        return new SuccessResponse<>(
                200,
                "get all list employee success",
                user
        );
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<ResponseDTO> create(@PathVariable Long id, @RequestBody CustomerRequestDTO request) {
        customerService.create(request, id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Add employee success")
                .build());
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id, @ModelAttribute CustomerRequestDTO request, MultipartFile img) {
        customerService.update(id, request,img);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Update employee success")
                .build());
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Delete employee success")
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<CustomerDTO>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone) {
        return ResponseEntity.ok(new SuccessResponse<>(
                200,
                "Search employees success",
                customerService.search(fullName, address, phone))
        );
    }
}
