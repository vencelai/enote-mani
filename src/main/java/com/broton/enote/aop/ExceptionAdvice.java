package com.broton.enote.aop;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.exception.ErrorCodeException;

import lombok.extern.log4j.Log4j2;

/**
 * @title 共通使用例外攔截
 * @description 共通使用例外攔截
 * @author ivanchen
 */
@Log4j2
@ControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseDto<Object>> catchMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		log.debug("catchMethodArgumentNotValidException = {}", exception);
		String msg = exception.getBindingResult().getFieldError().getDefaultMessage();
		log.debug("msg={}", msg);
		ResponseDto<Object> responseBo = ResponseDto.builder().code(ResultCode.ERR_1001.getCode()).msg(msg).data("必填資料不可為空!").build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBo);
	}
	
	@ExceptionHandler(ErrorCodeException.class)
	public ResponseEntity<ResponseDto<Object>> catchErrorCodeException(ErrorCodeException exception) {
		//log.error("occur ErrorCodeException = {}", exception);
		String code = exception.getErrorCode();
		String msg = exception.getMessage();
		log.debug("code = {}", code);
		log.debug("msg = {}", msg);
		ResponseDto<Object> responseBo = ResponseDto.builder().code(code).msg(msg).build();
		return ResponseEntity.ok(responseBo);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseDto<Object>> catchException(Exception exception) {
		log.error("occur Exception = {}", exception);
		String code = ResultCode.ERR_9999.getCode();
		String msg = ResultCode.ERR_9999.getDesc();
		ResponseDto<Object> responseBo = ResponseDto.builder().code(code).msg(msg).build();
		return ResponseEntity.ok(responseBo);
	}
	
	@ExceptionHandler({DataAccessException.class})
    public ResponseEntity<ResponseDto<Object>> handleDataAccessException(DataAccessException exception) {
        String message = "access database error";
        String code = ResultCode.ERR_9999.getCode();
        ResponseDto<Object> responseBo = ResponseDto.builder().code(code).msg(message).build();
		return ResponseEntity.ok(responseBo);
    }
}
