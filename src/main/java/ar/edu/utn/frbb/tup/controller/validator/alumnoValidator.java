package ar.edu.utn.frbb.tup.controller.validator;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import org.springframework.stereotype.Component;

@Component
public class alumnoValidator {


    public void validarAlumno(AlumnoDto alumnoDto) {
        // Validar que el DNI exista y sea positivo
        if (alumnoDto.getDni() <= 0) {
            throw new IllegalArgumentException("El DNI debe existir y ser positivo");
        }

        // Validar longitud del DNI
        String dniString = String.valueOf(alumnoDto.getDni());
        if (dniString.length() < 7 || dniString.length() > 9) {
            throw new IllegalArgumentException("El DNI debe tener entre 7 y 9 dígitos");
        }

        // Validar nombre no vacío y correctamente formateado
        if (alumnoDto.getNombre() == null || alumnoDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un nombre");
        }
        alumnoDto.setNombre(normalizarNombre(alumnoDto.getNombre()));

        // Validar formato del nombre
        if (!alumnoDto.getNombre().matches("^[A-ZÁÉÍÓÚÑ][a-záéíóúñ ]+$")) {
            throw new IllegalArgumentException("El nombre debe comenzar con mayúscula y contener solo letras y espacios.");
        }

        // Validar longitud del nombre
        if (alumnoDto.getNombre().length() <= 2) {
            throw new IllegalArgumentException("No abrevies el nombre");
        }
        if (alumnoDto.getNombre().length() > 25) {
            throw new IllegalArgumentException("No se permiten nombres tan largos");
        }

        // Validar apellido no vacío y correctamente formateado
        if (alumnoDto.getApellido() == null || alumnoDto.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un apellido");
        }
        alumnoDto.setApellido(normalizarNombre(alumnoDto.getApellido()));

        // Validar formato del apellido
        if (!alumnoDto.getApellido().matches("^[A-ZÁÉÍÓÚÑ][a-záéíóúñ ]+$")) {
            throw new IllegalArgumentException("El apellido debe comenzar con mayúscula y contener solo letras y espacios.");
        }

        // Validar longitud del apellido
        if (alumnoDto.getApellido().length() <= 2) {
            throw new IllegalArgumentException("No abrevies el apellido");
        }
        if (alumnoDto.getApellido().length() > 50) {
            throw new IllegalArgumentException("No se permiten apellidos tan largos");
        }
    }
    // se usa para poner mayusculas al principio y que no tenga espacios
    private String normalizarNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }

        String[] palabras = texto.trim().toLowerCase().split("\\s+");
        StringBuilder textoFormateado = new StringBuilder();

        for (String palabra : palabras) {
            textoFormateado.append(Character.toUpperCase(palabra.charAt(0)))
                    .append(palabra.substring(1))
                    .append(" ");
        }

        return textoFormateado.toString().trim();
    }
}
