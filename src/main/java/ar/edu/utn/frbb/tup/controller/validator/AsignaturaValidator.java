package ar.edu.utn.frbb.tup.controller.validator;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import org.springframework.stereotype.Component;

@Component
public class AsignaturaValidator {

    public void validarAsignatura(AsignaturaDto asignaturaDto) {

        // Validación del estado
        if (asignaturaDto.getEstado() == null) {
            throw new IllegalArgumentException("El estado de la asignatura no puede ser nulo");
        }

        // Validación de la nota
        if (asignaturaDto.getNota() == null) {
            throw new IllegalArgumentException("La nota no puede ser nula");
        }

        // Validación de que la nota esté entre 0 y 10
        if (asignaturaDto.getNota() < 0 || asignaturaDto.getNota() > 10) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10");
        }

        // Validación del ID de la materia
        if (asignaturaDto.getIdmateria() <= 0) {
            throw new IllegalArgumentException("El ID de la materia debe ser un número positivo y no cero");
        }

        // Validación del ID del alumno
        if (asignaturaDto.getIdalumno() <= 0) {
            throw new IllegalArgumentException("El ID del alumno debe ser un número positivo y no cero");
        }
    }

}

