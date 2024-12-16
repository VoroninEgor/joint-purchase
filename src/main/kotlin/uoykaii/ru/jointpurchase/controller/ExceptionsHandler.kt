package uoykaii.ru.jointpurchase.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import uoykaii.ru.jointpurchase.dto.ExceptionResponse
import uoykaii.ru.jointpurchase.exception.AuthenticationException
import uoykaii.ru.jointpurchase.exception.EntityNotFoundException

@RestControllerAdvice
class ExceptionsHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler
    fun handleWrongPasswordException(ex: AuthenticationException) = makeResponse(HttpStatus.FORBIDDEN, ex)

    @ExceptionHandler
    fun handleEntityNotFoundException(ex: EntityNotFoundException) = makeResponse(HttpStatus.NOT_FOUND, ex)

    private fun makeResponse(status: HttpStatus, ex: Exception) = ResponseEntity(
        ExceptionResponse(
            status.toString(),
            ex.message ?: "An unexpected error occurred"
        ),
        status
    )
}