package ar.edu.utn.frbb.tup.controller.validator;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import org.springframework.stereotype.Component;

@Component
public class carreraValidator {

    public void carreraValidation(CarreraDto carreraDto) {

        // Validación del nombre: no nulo o vacío
        if (carreraDto.getNombre() == null || carreraDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un nombre válido para la carrera.");
        }

        // Validar longitud del nombre
        if (carreraDto.getNombre().length() <= 2) {
            throw new IllegalArgumentException("El nombre de la carrera debe tener más de 2 caracteres.");
        }

        if (carreraDto.getNombre().length() > 50) { // Aumenté límite a 50, más razonable para nombres reales
            throw new IllegalArgumentException("El nombre de la carrera no puede tener más de 50 caracteres.");
        }

        // Validar caracteres (solo letras y espacios, con mayúsculas y tildes permitidos)
        if (!carreraDto.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new IllegalArgumentException("El nombre de la carrera solo puede contener letras, tildes y espacios.");
        }

        // Normalizar el nombre: Primera letra mayúscula, resto minúscula (en cada palabra)
        String nombreNormalizado = normalizarNombre(carreraDto.getNombre());
        carreraDto.setNombre(nombreNormalizado);
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
