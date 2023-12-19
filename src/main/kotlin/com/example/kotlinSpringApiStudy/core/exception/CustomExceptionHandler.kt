package com.example.kotlinSpringApiStudy.core.exception

import com.example.kotlinSpringApiStudy.core.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(e: InvalidInputException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(e.message ?: "Not Exception Message")
        return ResponseEntity.badRequest().body(error)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors = mutableMapOf<String, String>()
        e.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "NULL"
        }
        
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }
}
