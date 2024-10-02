package kz.quhan.finance_app.exceptionHandler;

import kz.quhan.finance_app.exception.ApplicationAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationAccessDeniedExceptionHandler {
    @ExceptionHandler(ApplicationAccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handler(ApplicationAccessDeniedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }
}
