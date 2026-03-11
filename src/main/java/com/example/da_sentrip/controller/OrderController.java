package com.example.da_sentrip.controller;

import com.example.da_sentrip.model.SuccessResponse;
import com.example.da_sentrip.model.dto.OrderDTO;
import com.example.da_sentrip.model.dto.reponse.OderdetailReponseDTO;
import com.example.da_sentrip.model.dto.reponse.OrderAllReponseDTO;
import com.example.da_sentrip.model.dto.reponse.ResponseDTO;
import com.example.da_sentrip.model.dto.request.OrderRequestDTO;
import com.example.da_sentrip.model.dto.reponse.OrderReponseDTO;
import com.example.da_sentrip.service.OrderService;
import com.example.da_sentrip.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/getall")
    public SuccessResponse<?> getAll(Authentication authentication) {
        List<OrderAllReponseDTO> orders = orderService.Getall(authentication);
        return new SuccessResponse<>(
                Constants.HTTP_STATUS.SUCCESS,
                "View success ",
                orders);
    }
    @GetMapping("/getuser")
    public SuccessResponse<?> getMyOrders(Authentication authentication) {
        List<OrderReponseDTO> oder= orderService.getMyOrders(authentication);
        return new SuccessResponse<>(
                Constants.HTTP_STATUS.SUCCESS,
                "View success "
                ,oder
        );

    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> create(@RequestBody OrderRequestDTO request, Authentication authentication) {
        String gmail = authentication.getName();
        OrderDTO order = orderService.Order(request, gmail);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.CREATED)
                    .message("get detail sucess")
                    .data(order)
                    .build());
        }catch (Exception ex){
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("get detail sucess")
                    .data(null)
                    .build());
        }

    }

    @GetMapping("/detail/{orderCode}")
    public ResponseEntity<ResponseDTO<Object>> detail(@PathVariable String orderCode) {
        List<OderdetailReponseDTO> detail = orderService.details(orderCode);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status("ok")
                .code(Constants.HTTP_STATUS.SUCCESS)
                .message("get detail sucess")
                .data(detail)
                .build());
    }

    @PostMapping("/cancel/{orderCode}")
    public ResponseEntity<ResponseDTO<Object>> cancel(@PathVariable String orderCode) {
        OrderDTO result = orderService.Cancel(orderCode);
        try {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.SUCCESS)
                    .message("get detail sucess")
                    .data(result)
                    .build());
        }catch (Exception ex) {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status("ok")
                    .code(Constants.HTTP_STATUS.BAD_REQUEST)
                    .message("get detail sucess")
                    .build());
        }
    }
}