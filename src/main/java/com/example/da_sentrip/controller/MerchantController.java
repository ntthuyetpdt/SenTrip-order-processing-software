package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.MerchantDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.MerchantRequestDTO;
import com.example.da_sentrip.service.MerchantService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class MerchantController  {
    private final MerchantService merchantService;

    @GetMapping("/getAll")
    public SuccessResponse<?> getAll(){
        List<MerchantReponseDTO> user =merchantService.getAll();
        return new SuccessResponse<>(
                200,
                "get all list employee success",
                user
        );
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<ResponseDTO> create(@PathVariable Long id, @RequestBody MerchantRequestDTO request) {
        merchantService.create(request, id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Add employee success")
                .build());
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id, @ModelAttribute MerchantRequestDTO request, MultipartFile img) {
        merchantService.update(id, request,img);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Update employee success")
                .build());
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        merchantService.delete(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Delete employee success")
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<MerchantDTO>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String mnv) {
        return ResponseEntity.ok(new SuccessResponse<>(
                200,
                "Search employees success",
                merchantService.search(fullName, address, mnv))
        );
    }
}
