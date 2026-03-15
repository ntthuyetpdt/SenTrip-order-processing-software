package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.reponse.EmployeeReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.service.EmployeeService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN_VIEW_EPLOYEE')")
    public ResponseEntity<SuccessResponse<List<EmployeeReponseDTO>>> getAll() {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Get all success", employeeService.getAll()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN_CREATE_EMPLOYEE')")
    public ResponseEntity<ResponseDTO> create(@RequestParam String gmail, @RequestBody EmployeeRequestDTO request) {
        employeeService.create(request, gmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.CREATED).message("Create success").build());
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_UPDATE_EMPLOYEE')")
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id, @RequestBody EmployeeRequestDTO request,
                                              @RequestParam(required = false) MultipartFile img) {
        employeeService.update(id, request, img);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS).message("Update success").build());
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_DELETE_EMPLOYEE')")
    public ResponseEntity<ResponseDTO> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS).message("Delete success").build());
    }

    @GetMapping("getDetails/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_DETAILS_EMLOYEE')")
    public ResponseEntity<SuccessResponse<List<EmployeeDTO>>> getDetails(@PathVariable Long id) {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Get details success", employeeService.getdetailis(id)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN_SEARCH_EMPLOYEE')")
    public ResponseEntity<SuccessResponse<List<EmployeeDTO>>> search(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String mnv) {
        return ResponseEntity.ok(new SuccessResponse<>(Constants.HTTP_STATUS.SUCCESS, "Search success", employeeService.search(fullName, address, mnv)));
    }
}