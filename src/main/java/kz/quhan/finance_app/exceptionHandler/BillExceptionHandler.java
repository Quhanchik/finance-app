package kz.quhan.finance_app.exceptionHandler;

import kz.quhan.finance_app.exception.BillException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BillExceptionHandler {
    @ExceptionHandler(BillException.class)
    public ResponseEntity<ProblemDetail> handler(BillException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }
}
