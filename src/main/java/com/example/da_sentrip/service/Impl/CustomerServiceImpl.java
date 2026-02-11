package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.CustomerDTO;
import com.example.da_sentrip.model.dto.reponse.CustomerReponseDTO;
import com.example.da_sentrip.model.dto.request.CustomerRequestDTO;
import com.example.da_sentrip.model.entity.Customer;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.repository.CustomerRepository;
import com.example.da_sentrip.repository.DataSourceRepository;
import com.example.da_sentrip.repository.UserRepository;
import com.example.da_sentrip.service.CustomerService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final DataSourceRepository dataSourceRepository;
    private final UserRepository userRepository;
    private final MediaStorageService mediaStorageService;
    private final ModelMapper modelMapper;

    @Override
    public CustomerDTO create(CustomerRequestDTO request, Long id) {
       customerRepository.findById(id).orElseThrow(() ->new BadCredentialsException("ID not Found"));
       return new CustomerDTO(modelMapper.map(request, Customer.class));
    }

    @Override
    public CustomerDTO update(Long id, CustomerRequestDTO request, MultipartFile img) {
        Customer customer =customerRepository.findById(id).orElseThrow(() ->new BadCredentialsException("Customer not found"));
        modelMapper.map(request,customer);
        if (img != null && !img.isEmpty()) {

            if (customer.getImg() != null && customer.getImg().matches("\\d+")) {
                mediaStorageService.deleteMedia(Long.valueOf(customer.getImg()));
            }

            String newDsId = mediaStorageService.uploadMedia(img);
            customer.setImg(newDsId);
        }
        customerRepository.save(customer);
        return new CustomerDTO(customer);
    }

    @Override
    public CustomerDTO delete(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new BadCredentialsException("ID not found"));

        if (customer.getImg() != null && !customer.getImg().isBlank()) {
            Long imgId = Long.parseLong(customer.getImg());
            dataSourceRepository.findById(imgId).ifPresent(data -> mediaStorageService.deleteMedia(imgId));
        }
        return new CustomerDTO(customer);
    }



    @Override
    public List<CustomerDTO> search(String fullName, String address, String phone) {
        return customerRepository.findBySearch(fullName, address, phone).stream().map(CustomerDTO :: new).collect(Collectors.toList());
    }

    @Override
    public List<CustomerReponseDTO> getAll() {
        return customerRepository.findAll().stream().map(customer -> {
            User user =customer.getUser();
            CustomerReponseDTO dto = new CustomerReponseDTO(customer,user);
            if (customer.getImg() != null && customer.getImg().matches("\\d+")) {
                dataSourceRepository
                        .findById(Long.valueOf(customer.getImg()))
                        .ifPresent(ds -> {
                            dto.setImg(ds.getImageUrl());
                        });
            }
            return dto;
        }).toList();
    }
}
