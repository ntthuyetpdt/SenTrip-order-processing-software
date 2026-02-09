package com.example.da_sentrip.handler;


import com.example.da_sentrip.config.MessageTemplate;
import com.example.da_sentrip.exception.EntityValidationException;
import com.example.da_sentrip.exception.PartialUpdateException;
import com.example.da_sentrip.exception.ResourceNotFoundException;
import com.example.da_sentrip.model.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.util.Date;

@ControllerAdvice//Xử lý exception toàn cục cho tất cả Controller
@Slf4j //là annotation của Lombok, tự động tạo logger:
public class GlobalExceptionHandler {

    @Autowired
    private MessageTemplate messageTemplate;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail= new ErrorDetail( new Date() ,e.getMessage(),"",request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);//Server không tìm thấy tài nguyên (resource) tương ứng với URL mà client yêu cầu.
    }
    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<?> EntityValidationException(EntityValidationException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail= new ErrorDetail( new Date() ,e.getMessage(),"",request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(PartialUpdateException.class)
    public ResponseEntity<?> PartialUpdateException(PartialUpdateException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail= new ErrorDetail( new Date() ,messageTemplate.message("error validation"),"",request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);//Server không hiểu hoặc không thể xử lý request do dữ liệu client gửi lên không hợp lệ.
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail= new ErrorDetail( new Date() ,messageTemplate.message("error.system"),"",request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);// Server đã nhận request đúng, nhưng bị lỗi trong quá trình xử lý nên không trả về kết quả được.

    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> HttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail( new Date() ,messageTemplate.message("error.validation"),"",request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.METHOD_NOT_ALLOWED);// Server có endpoint đó, nhưng không cho phép HTTP method mà client đang dùng.
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity <?> httpRequestMethodNotSupportedException (HttpRequestMethodNotSupportedException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail( new Date() ,messageTemplate.message("error.system"),"",request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e, WebRequest request) {
        log.error(e.toString());
        ErrorDetail errorDetail = new ErrorDetail( new Date(), e.getMessage(), "", request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.UNAUTHORIZED);
    }

}

