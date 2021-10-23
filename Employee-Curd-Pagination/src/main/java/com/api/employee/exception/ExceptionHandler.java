package com.api.employee.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.api.employee.dto.ErrorDto;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationException(MethodArgumentNotValidException exception)
	{
		
		 Map<String, String> errors = new HashMap<>();
		
		exception.getBindingResult().getAllErrors().forEach(error ->{
			
			String filedName = ((FieldError)error).getField();
			String errorName = error.getDefaultMessage();
			
			errors.put(filedName, errorName);
		});
		
		
		return errors;
		
	}
	
	
	@org.springframework.web.bind.annotation.ExceptionHandler(NoSuchEmployeeFoundException.class)
	public ResponseEntity<ErrorDto> handleValidation(NoSuchEmployeeFoundException exe)
	{
		ErrorDto error =new ErrorDto(HttpStatus.NOT_FOUND.value(),exe.getMessage());
		
		return new ResponseEntity<ErrorDto>(error,HttpStatus.NOT_FOUND);
		
		
	}
	
	
	
	
}
