package kz.quhan.finance_app.exceptionHandler;

import kz.quhan.finance_app.exception.CategoryException;
import kz.quhan.finance_app.exception.FinanceUnitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.awt.*;

@RestControllerAdvice
public class FinanceUnitExceptionHandler {
    @ExceptionHandler(FinanceUnitException.class)
    public ResponseEntity<ProblemDetail> handler(FinanceUnitException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(problemDetail);
    }
}
