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
                200,
                "get all list employee success",
                user
        );
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<ResponseDTO> create(@PathVariable Long id, @RequestBody EmployeeRequestDTO request) {
        employeeService.create(request, id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Add employee success")
                .build());
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id, @RequestBody EmployeeRequestDTO request,@RequestParam(value = "img",required = false) MultipartFile img) {
        employeeService.update(id, request,img);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Update employee success")
                .build());
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("Delete employee success")
                .build());
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<SuccessResponse<List<EmployeeDTO>>> getDetails(@PathVariable Long id) {
        return ResponseEntity.ok(new SuccessResponse<>(
                200,
                "Get employee details success",
                employeeService.getdetailis(id))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<List<EmployeeDTO>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String mnv) {
        return ResponseEntity.ok(new SuccessResponse<>(
                200,
                "Search employees success",
                employeeService.search(fullName, address, mnv))
        );
    }
}