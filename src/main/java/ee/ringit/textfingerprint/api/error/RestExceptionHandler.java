package ee.ringit.textfingerprint.api.error;


import ee.ringit.textfingerprint.exception.EncodingException;
import ee.ringit.textfingerprint.exception.InvalidFileException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidFileException.class, EncodingException.class})
    public ResponseEntity<Object> handleBusinessLogicException(RuntimeException exception) {
        BusinessLogicErrorResponse businessLogicErrorResponse = new BusinessLogicErrorResponse(BAD_REQUEST);
        businessLogicErrorResponse.setMessage(exception.getMessage());
        return buildResponseEntity(businessLogicErrorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}