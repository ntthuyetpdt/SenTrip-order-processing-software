package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.CustomerDTO;
import com.example.da_sentrip.model.dto.reponse.CustomerReponseDTO;
import com.example.da_sentrip.model.dto.reponse.TicketReponseDTO;
import com.example.da_sentrip.model.dto.request.CustomerRequestDTO;
import com.example.da_sentrip.model.entity.Customer;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.repository.CustomerRepository;
import com.example.da_sentrip.repository.DataSourceRepository;
import com.example.da_sentrip.repository.UserRepository;
import com.example.da_sentrip.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerDTO update(String gmail, CustomerRequestDTO request, MultipartFile img) {
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Customer customer = customerRepository.findByUserId(user.getId()).orElseThrow(() -> new BadCredentialsException("Customer not found"));
        if (request.getFullName() != null) customer.setFullName(request.getFullName());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getCccd() != null) customer.setCccd(request.getCccd());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
        }
        if (img != null && !img.isEmpty()) {
            if (customer.getImg() != null && customer.getImg().matches("\\d+")) {
                mediaStorageService.deleteMedia(Long.valueOf(customer.getImg()));
            }
            customer.setImg(mediaStorageService.uploadMedia(img));
        }

        customerRepository.save(customer);
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

    @Override
    public List<TicketReponseDTO> getTicket(Authentication authentication) {
        String gmail = authentication.getName();
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        return customerRepository.findTicketsByUserId(user.getId()).stream()
                .map(view -> new TicketReponseDTO(
                        view.getFullName(),
                        view.getPhone(),
                        view.getGmail(),
                        view.getCccd(),
                        view.getProductName(),
                        view.getServiceType(),
                        view.getType(),
                        view.getCreateTime(),
                        view.getStatus(),
                        view.getTotalAmount(),
                        view.getPaidAt(),
                        view.getPaymentCode(),
                        view.getImg(),
                        view.getQuantities()
                ))
                .toList();
    }


}
