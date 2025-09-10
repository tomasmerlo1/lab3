package ar.edu.utn.frbb.tup.controller.validator;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.NombreProfesorAbreviadoException;
import ar.edu.utn.frbb.tup.model.exception.NombreProfesorInvalidoException;
import ar.edu.utn.frbb.tup.model.exception.NombreProfesorLargoException;
import ar.edu.utn.frbb.tup.model.exception.TituloProfesorInvalidoException;
import org.springframework.stereotype.Component;

@Component
public class profesorValidator {

    public void profesorValidation(ProfesorDto profesorDto) {
        // Validación del nombre
        if (profesorDto.getNombre() == null || profesorDto.getNombre().trim().isEmpty()) {
            throw new NombreProfesorInvalidoException("Debe ingresar el nombre del profesor.");
        }

        if (!profesorDto.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new NombreProfesorInvalidoException("El nombre solo puede contener letras, tildes y espacios.");
        }

        if (profesorDto.getNombre().length() <= 2) {
            throw new NombreProfesorAbreviadoException("No abrevie el nombre del profesor.");
        }

        if (profesorDto.getNombre().length() > 50) {
            throw new NombreProfesorLargoException("El nombre del profesor no puede tener más de 50 caracteres.");
        }

        // Validación del apellido
        if (profesorDto.getApellido() == null || profesorDto.getApellido().trim().isEmpty()) {
            throw new NombreProfesorInvalidoException("Debe ingresar el apellido del profesor.");
        }

        if (!profesorDto.getApellido().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new NombreProfesorInvalidoException("El apellido solo puede contener letras, tildes y espacios.");
        }

        if (profesorDto.getApellido().length() <= 2) {
            throw new NombreProfesorAbreviadoException("No abrevie el apellido del profesor.");
        }

        if (profesorDto.getApellido().length() > 50) {
            throw new NombreProfesorLargoException("El apellido del profesor no puede tener más de 50 caracteres.");
        }

        // Validación del título
        if (profesorDto.getTitulo() == null || profesorDto.getTitulo().trim().isEmpty()) {
            throw new TituloProfesorInvalidoException("Debe ingresar el título del profesor.");
        }

        if (!profesorDto.getTitulo().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new TituloProfesorInvalidoException("El título solo puede contener letras, tildes y espacios.");
        }

        if (profesorDto.getTitulo().length() <= 3) {
            throw new NombreProfesorAbreviadoException("No abrevie el título del profesor.");
        }

        if (profesorDto.getTitulo().length() > 50) {
            throw new NombreProfesorLargoException("El título del profesor no puede tener más de 50 caracteres.");
        }

        // Normalización de nombre, apellido y título
        profesorDto.setNombre(normalizarNombre(profesorDto.getNombre()));
        profesorDto.setApellido(normalizarNombre(profesorDto.getApellido()));
        profesorDto.setTitulo(normalizarNombre(profesorDto.getTitulo()));
    }

    // se usa para poner mayusculas al principio y que no tenga espacios
    private String normalizarNombre(String nombre) {
        StringBuilder resultado = new StringBuilder();
        String[] palabras = nombre.trim().split("\\s+");

        for (String palabra : palabras) {
            String palabraNormalizada = palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase();
            resultado.append(palabraNormalizada).append(" ");
        }

        return resultado.toString().trim();
    }
}