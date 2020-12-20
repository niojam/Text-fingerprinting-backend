package ee.ringit.textfingerprint.api.error;

import org.springframework.http.HttpStatus;

public class BusinessLogicErrorResponse extends ErrorResponse{

    public BusinessLogicErrorResponse(HttpStatus status) {
        super(status);
    }
}
