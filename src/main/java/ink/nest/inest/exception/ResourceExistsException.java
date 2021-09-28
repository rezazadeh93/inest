package ink.nest.inest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceExistsException extends RuntimeException {
    public ResourceExistsException() {
    }

    public ResourceExistsException(String message) {
        super(message);
    }

    public ResourceExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
