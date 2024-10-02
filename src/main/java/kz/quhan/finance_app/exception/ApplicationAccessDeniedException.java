package kz.quhan.finance_app.exception;

public class ApplicationAccessDeniedException extends RuntimeException {
    public ApplicationAccessDeniedException(String msg) {
        super(msg);
    }
}
