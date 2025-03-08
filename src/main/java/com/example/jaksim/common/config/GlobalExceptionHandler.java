package com.example.jaksim.common.config;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.jaksim.common.ResponseDto;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        ResponseDto errorResponse = new ResponseDto(400, e.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResponseDto> handleNullPointerException(NullPointerException e) {
        ResponseDto errorResponse = new ResponseDto(404, e.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException e) {
        ResponseDto errorResponse = new ResponseDto(400, e.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto> handleNotFoundException(NotFoundException e) {
        ResponseDto errorResponse = new ResponseDto(404, e.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleAllExceptions(Exception e) {
        ResponseDto errorResponse = new ResponseDto(500, "서버 오류가 발생했습니다: " + e.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
