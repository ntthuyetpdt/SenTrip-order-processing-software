package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.reponse.EmployeeReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import com.example.da_sentrip.model.dto.request.UpdateEmployees;
import com.example.da_sentrip.model.dto.request.UpdateRoleDTO;
import com.example.da_sentrip.service.EmployeeService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE_UPDATE_PROFILE')")
    public ResponseEntity<ResponseDTO> update(
            @PathVariable Long id,
            @RequestParam String gmail,
            @RequestBody UpdateEmployees request) {
        employeeService.update(request, gmail, id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(200)
                .message("Update success")
                .build());
    }

    @PostMapping("/update/{id}/role")
    @PreAuthorize("hasAnyAuthority('ADMIN_UPDATE_EMPLOYEE')")
    public ResponseEntity<ResponseDTO> updateRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleDTO request,
            Authentication authentication) {
        employeeService.updateRole(id, request.getRole());
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok").code(Constants.HTTP_STATUS.SUCCESS).message("Update role success").build());
    }

    @PostMapping("/update/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE_UPDATE_PROFILE','ACCOUNTANT_UPDATE_PROFILE')")
    public ResponseEntity<?> updateProfile(
            @RequestParam(required = false) MultipartFile img,
            @RequestPart(required = false)  EmployeeRequestDTO request,
            Authentication authentication) {
        String gmail = authentication.getName();
        employeeService.updateProfile(gmail, request, img);
        return ResponseEntity.ok("update success");
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