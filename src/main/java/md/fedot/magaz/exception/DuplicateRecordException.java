package md.fedot.magaz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateRecordException extends RuntimeException {

    public DuplicateRecordException() {
        super();
    }

    public DuplicateRecordException(String message) {
        super(message);
    }
}