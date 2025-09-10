package ar.edu.utn.frbb.tup.controller.validator;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaAbreviadoException;
import ar.edu.utn.frbb.tup.model.exception.MateriaNombreInvalidoException;
import ar.edu.utn.frbb.tup.model.exception.NombreDeLaMateriaException;
import org.springframework.stereotype.Component;

@Component
public class materiaValidator {

    public void materiaValidation(MateriaDto materiaDto) {
        // Validación de que el nombre no sea nulo o vacío
        if (materiaDto.getNombre() == null || materiaDto.getNombre().trim().isEmpty()) {
            throw new MateriaNombreInvalidoException("Debe ingresar el nombre de la materia.");
        }

        // Validación del nombre: solo letras, tildes y espacios
        if (!materiaDto.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new MateriaNombreInvalidoException("El nombre de la materia solo puede contener letras, tildes y espacios.");
        }

        // Validación de la longitud del nombre (mínimo 3 caracteres)
        if (materiaDto.getNombre().length() <= 2) {
            throw new MateriaAbreviadoException("No abrevie el nombre de la materia.");
        }

        // Validación de la longitud del nombre (máximo 50 caracteres)
        if (materiaDto.getNombre().length() > 50) { // Ampliado a 50 caracteres por realismo
            throw new NombreDeLaMateriaException("El nombre de la materia no puede tener más de 50 caracteres.");
        }

        // Validación del año
        if (materiaDto.getAnio() <= 0) {
            throw new NombreDeLaMateriaException("El año debe ser mayor a 0.");
        }

        // Normalización del nombre (primera letra de cada palabra en mayúscula)
        materiaDto.setNombre(normalizarNombre(materiaDto.getNombre()));
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