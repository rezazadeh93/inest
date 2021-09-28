package ink.nest.inest.controller;

import ink.nest.inest.exception.ResourceExistsException;
import ink.nest.inest.exception.InvalidOldPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler {
    private final MessageSource messages;

    public RestResponseEntityExceptionHandler(MessageSource messages) {
        this.messages = messages;
    }


    // 400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Map<String, String> handleBindException(final BindException ex) {
        log.error("400 Status Code", ex);
        return getResponseBody(ex.getAllErrors());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Handling handle validation exception", ex);
        return getResponseBody(ex.getAllErrors());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidOldPasswordException.class})
    public Map<String, String> handleInvalidOldPassword(final InvalidOldPasswordException ex, final WebRequest request) {
        log.error("400 Status Code", ex);
        return getResponseBody(
                "InvalidOldPassword",
                messages.getMessage("message.invalidOldPassword",
                        null,
                        request.getLocale()
                )
        );
    }

    // 404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UsernameNotFoundException.class})
    public Map<String, String> handleUserNotFound(final UsernameNotFoundException ex, final WebRequest request) {
        log.error("404 Status Code", ex);
        return getResponseBody(
                "Email",
                messages.getMessage("message.emailNotFound",
                        null,
                        request.getLocale()
                )
        );
    }

    // 409
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ResourceExistsException.class})
    public Map<String, String> handleUserAlreadyExist(final ResourceExistsException ex, final WebRequest request) {
        log.error("409 Status Code", ex);
        return getResponseBody(
                "Email",
                messages.getMessage("message.emailExist",
                        null,
                        request.getLocale()
                )
        );
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<Object> handleResponseStatusException(final ResponseStatusException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(getResponseBody("message", ex.getReason()), ex.getStatus());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public Map<String, String> handleInternalServerException(final Exception ex) {
        log.error(ex.getMessage());
        return getResponseBody("message", ex.getMessage());
    }


    private Map<String, String> getResponseBody(final String key, final String messages) {
        Map<String, String> errors = new HashMap<>();

        errors.put(key, messages);

        return errors;
    }

    private Map<String, String> getResponseBody(final List<ObjectError> allErrors) {
        Map<String, String> errors = new HashMap<>();

        allErrors.forEach(e -> {
            String fieldName;
            if (e instanceof FieldError) {
                fieldName = ((FieldError) e).getField();
            } else {
                fieldName = e.getObjectName();
            }

            errors.put(fieldName, e.getDefaultMessage());
        });

        return Collections.unmodifiableMap(errors);
    }
}