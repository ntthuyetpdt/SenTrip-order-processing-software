package com.example.da_sentrip.service;

import com.example.da_sentrip.model.dto.EmployeeDTO;
import com.example.da_sentrip.model.dto.reponse.EmployeeReponseDTO;
import com.example.da_sentrip.model.dto.request.EmployeeRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public interface EmployeeService {
    EmployeeDTO create (EmployeeRequestDTO request ,Long id);
    EmployeeDTO update (Long id, EmployeeRequestDTO request, MultipartFile img);
    EmployeeDTO delete (Long id);
    List<EmployeeDTO>  getdetailis(Long id);
    List<EmployeeDTO> search(String fullName, String address, String mnv);
    List<EmployeeReponseDTO> getAll ( );


}
