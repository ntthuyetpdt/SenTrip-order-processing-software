package com.example.da_sentrip.controller;


import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.service.EmployeeService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/create/{id}")
    public ResponseEntity<?> create(@RequestBody EmployeeRequestDTO request, @PathVariable("id") Long id) {
        try {
            employeeService.create(request, id);
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("add employee success")
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.builder()
                            .status("error")
                            .code(Constants.HTTP_STATUS.INTERNAL_SERVER_ERROR)
                            .message(e.getMessage())
                            .build());
        }
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> update (@PathVariable Long id, @RequestBody EmployeeRequestDTO request){
        try {
            employeeService.update(id, request);
            return ResponseEntity.ok("update success");
        }catch (Exception ex){
            return ResponseEntity.internalServerError().body("update not success");
        }
    }
}

