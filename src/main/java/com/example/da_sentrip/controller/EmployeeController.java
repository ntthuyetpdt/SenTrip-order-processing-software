package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.reponse.EmployeeReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.service.EmployeeService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping("/getAll")
    public SuccessResponse<?> getAll(){
        List<EmployeeReponseDTO> user =employeeService.getAll();
        return new SuccessResponse<>(
                Constants.HTTP_STATUS.SUCCESS,
                "view list employee success",
                user
        );
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<ResponseDTO> create(@PathVariable String gmail, @RequestBody EmployeeRequestDTO request) {
        employeeService.create(request, gmail);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("Add  success")
                    .build());
        }catch (Exception ex ){
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("Add failed")
                    .build());
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id, @RequestBody EmployeeRequestDTO request,@RequestParam(value = "img",required = false) MultipartFile img) {
        employeeService.update(id, request,img);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("Update  success")
                    .build());
        }catch (Exception ex) {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("Update  failed")
                    .build());

        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        employeeService.delete(id);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("Delete  success")
                    .build());
        }catch (Exception ex) {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("Delete  failed")
                    .build());
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<SuccessResponse<List<EmployeeDTO>>> getDetails(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new SuccessResponse<>(
                    Constants.HTTP_STATUS.SUCCESS,
                    "Get  details success",
                    employeeService.getdetailis(id))
            );
        }catch (Exception ex) {
            return ResponseEntity.ok(new SuccessResponse<>(
                    Constants.HTTP_STATUS.UNAUTHORIZED,
                    "Get  details failed",
                    null
            ));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<EmployeeDTO>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String mnv) {
        try {
            return ResponseEntity.ok(new SuccessResponse<>(
                    Constants.HTTP_STATUS.SUCCESS,
                    "Search  success",
                    employeeService.search(fullName, address, mnv))
            );
        }catch (Exception ex) {
            return ResponseEntity.ok(new SuccessResponse<>(
                    Constants.HTTP_STATUS.BAD_REQUEST,
                    "Search failed",
                    null
            ));
        }
    }
}