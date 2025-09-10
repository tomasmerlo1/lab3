package ar.edu.utn.frbb.tup.controller.handler;

import ar.edu.utn.frbb.tup.model.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UtnResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {AlumnoNoEncontradoException.class,ProfesorYaExisteException.class, IllegalArgumentException.class, CarreraYaExisteEstaException.class})
    protected ResponseEntity<Object> handleBadRequestExceptions(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { ProfesorNoEncontradoException.class, MateriaNoEncontradaException.class, CarreraNoEncontradaException.class,NoseEncontroAsignatura.class,CarreraNotFoundException.class,AsignaturaNoEncontradaException.class,NombreDeLaMateriaException.class,IllegalStateException.class,
    NombreProfesorAbreviadoException.class,NombreProfesorInvalidoException.class,TituloProfesorInvalidoException.class,NombreProfesorLargoException.class, EstadoIncorrectoException.class, EstadoInvalidoException.class,AlumnoYaExisteException.class})
    protected ResponseEntity<Object> handleNotFoundExceptions(Exception ex, WebRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        CustomApiError error = new CustomApiError();
        error.setErrorCode(status.value());
        error.setErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            CustomApiError error = new CustomApiError();
            error.setErrorMessage(ex.getMessage());
            body = error;
        }
        return new ResponseEntity<>(body, headers, status);
    }
}

