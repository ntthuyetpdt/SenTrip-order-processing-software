package com.example.da_sentrip.service.Impl;

import com.example.da_sentrip.helper.MediaStorageService;
import com.example.da_sentrip.model.dto.MerchantDTO;
import com.example.da_sentrip.model.dto.reponse.MerchantReponseDTO;
import com.example.da_sentrip.model.dto.request.MerchantRequestDTO;
import com.example.da_sentrip.model.entity.Merchant;
import com.example.da_sentrip.repository.DataSourceRepository;
import com.example.da_sentrip.repository.MerchantRepository;
import com.example.da_sentrip.service.MerchantService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class MerchantServiceImpl implements MerchantService {

    private final MerchantRepository merchantRepository;
    private final DataSourceRepository dataSourceRepository;
    private final MediaStorageService mediaStorageService;
    private final ModelMapper modelMapper;

    @Override
    public MerchantDTO create(MerchantRequestDTO request, Long id) {
        merchantRepository.findById(id).orElseThrow(()-> new BadCredentialsException("ID not Found"));
        return new MerchantDTO(modelMapper.map(request, Merchant.class));
    }

    @Override
    public MerchantDTO update(Long id, MerchantRequestDTO request, MultipartFile img) {
        Merchant merchant =merchantRepository.findById(id).orElseThrow(()-> new BadCredentialsException("ID not Found"));
        modelMapper.map(request,Merchant.class);
        if (img!=null && img.isEmpty()){
            if(merchant.getImg()!=null && merchant.getImg().matches("\\d+")){
                mediaStorageService.deleteMedia(Long.valueOf(merchant.getImg()));
            }
            String imgID=mediaStorageService.uploadMedia(img);
            merchant.setImg(imgID);
        }
        merchantRepository.save(merchant);
        return new MerchantDTO(merchant);
    }

    @Override
    public MerchantDTO delete(Long id) {
        Merchant merchant=merchantRepository.findById(id).orElseThrow(()-> new BadCredentialsException("ID not found"));
        if(merchant.getImg()!=null && merchant.getImg().isBlank()){
            Long imgID =Long.parseLong(merchant.getImg());
            dataSourceRepository.findById(imgID).ifPresent(data -> mediaStorageService.deleteMedia(imgID));
        }
        return new MerchantDTO(merchant);
    }

    @Override
    public List<MerchantDTO> getdetailis(Long id) {
        Merchant merchant=merchantRepository.findById(id).orElseThrow(() -> new BadCredentialsException("ID not found"));
        return Collections.singletonList(new MerchantDTO(merchant));
    }

    @Override
    public List<MerchantDTO> search(String merchantName, String address, String businessLicense) {
        return merchantRepository.findBySearch(merchantName,address,businessLicense).stream().map(MerchantDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<MerchantReponseDTO> getAll() {
        return merchantRepository.findAll().stream().map(merchant -> {
            MerchantReponseDTO  dto = new MerchantReponseDTO(merchant);
            if (merchant.getImg() != null && merchant.getImg().matches("\\d+")) {
                dataSourceRepository.findById(Long.valueOf(merchant.getImg())).ifPresent(ds -> {
                    dto.setImg(ds.getImageUrl());
                });
            }
            return dto;
        }).toList();

    }
}
