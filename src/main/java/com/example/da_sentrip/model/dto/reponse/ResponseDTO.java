package com.example.da_sentrip.model.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
    private String status;
    private Integer code;
    private String message;
    private T data;
    private Long total;
    private String details;

    public static ResponseDTO build(int code, String errors, Long total, Object data)
    {
        ResponseDTO apiResponse = new ResponseDTO();
        apiResponse.setCode(code);
        apiResponse.setData(data);
        apiResponse.setMessage(errors);
        apiResponse.setTotal(total);
        return apiResponse;
    }
}