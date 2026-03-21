package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.MerchantDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantReponseDTO;
import com.example.da_sentrip.model.dto.request.MerchantRequestDTO;
import com.example.da_sentrip.model.entity.Merchant;
import com.example.da_sentrip.model.entity.User;
import com.example.da_sentrip.repository.DataSourceRepository;
import com.example.da_sentrip.repository.MerchantRepository;
import com.example.da_sentrip.repository.UserRepository;
import com.example.da_sentrip.service.MerchantService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final DataSourceRepository dataSourceRepository;
    private final MediaStorageService mediaStorageService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void update(String gmail, MerchantRequestDTO request, MultipartFile img) {
        User user = userRepository.findByGmail(gmail).orElseThrow(() -> new RuntimeException("User not found"));
        Merchant merchant = merchantRepository.findByUser_Id(user.getId()).orElseThrow(() -> new BadCredentialsException("Merchant not found"));
        if (request.getMerchantName() != null) merchant.setMerchantName(request.getMerchantName());
        if (request.getPhone() != null) merchant.setPhone(request.getPhone());
        if (request.getCccd() != null) merchant.setCccd(request.getCccd());
        if (request.getBankName() != null) merchant.setBankName(request.getBankName());
        if (request.getBankAccount() != null) merchant.setBankAccount(request.getBankAccount());
        if (request.getAddress() != null) merchant.setAddress(request.getAddress());
        if (request.getBusinessLicense() != null) merchant.setBusinessLicense(request.getBusinessLicense());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
        }

        if (img != null && !img.isEmpty()) {
            if (merchant.getImg() != null && merchant.getImg().matches("\\d+"))
                mediaStorageService.deleteMedia(Long.valueOf(merchant.getImg()));
            merchant.setImg(mediaStorageService.uploadMedia(img));
        }

        merchantRepository.save(merchant);
    }
    @Override
    public MerchantDTO delete(Long id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("ID not found"));
        if (merchant.getImg() != null && merchant.getImg().matches("\\d+")) {
            Long imgId = Long.parseLong(merchant.getImg());
            dataSourceRepository.findById(imgId).ifPresent(d -> mediaStorageService.deleteMedia(imgId));
        }
        return new MerchantDTO(merchant);
    }

    @Override
    public List<MerchantDTO> getdetailis(Long id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("ID not found"));
        return Collections.singletonList(new MerchantDTO(merchant));
    }

    @Override
    public List<MerchantDTO> search(String merchantName, String address, String businessLicense) {
        return merchantRepository.findBySearch(merchantName, address, businessLicense)
                .stream().map(MerchantDTO::new).toList();
    }

    @Override
    public List<MerchantReponseDTO> getAll() {
        return merchantRepository.findAll().stream().map(merchant -> {
            MerchantReponseDTO dto = new MerchantReponseDTO(merchant);
            if (merchant.getImg() != null && merchant.getImg().matches("\\d+"))
                dataSourceRepository.findById(Long.valueOf(merchant.getImg()))
                        .ifPresent(ds -> dto.setImg(ds.getImageUrl()));
            return dto;
        }).toList();
    }
}
