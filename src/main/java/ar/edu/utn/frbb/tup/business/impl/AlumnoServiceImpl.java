package ar.edu.utn.frbb.tup.business.impl;
import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.AlumnoYaExisteException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Component
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoDaoMemoryImpl alumnoDaoMemoryImpl;

    @Override
    public Alumno crearAlumno(AlumnoDto alumnodto) throws AlumnoYaExisteException {
        // Validación de campos vacíos
        if (alumnodto.getNombre() == null || alumnodto.getNombre().trim().isEmpty()) {
            throw new AlumnoYaExisteException("El nombre no puede estar vacío.");
        }
        if (alumnodto.getApellido() == null || alumnodto.getApellido().trim().isEmpty()) {
            throw new AlumnoYaExisteException("El apellido no puede estar vacío.");
        }

        // Validación de caracteres en nombre y apellido
        if (!alumnodto.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new AlumnoYaExisteException("El nombre solo puede contener letras y espacios.");
        }
        if (!alumnodto.getApellido().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new AlumnoYaExisteException("El apellido solo puede contener letras y espacios.");
        }

        // Validación de DNI
        if (alumnodto.getDni() <= 0) {
            throw new AlumnoYaExisteException("El DNI debe ser un número positivo.");
        }
        if (String.valueOf(alumnodto.getDni()).length() > 9) {
            throw new AlumnoYaExisteException("El DNI no puede tener más de 9 dígitos.");
        }

        if (alumnoDaoMemoryImpl.buscarAlumnopordni(alumnodto.getDni()) != null) {
            throw new AlumnoYaExisteException("El alumno con el DNI " + alumnodto.getDni() + " ya existe.");
        }
        Alumno alumno = new Alumno(alumnodto.getNombre(), alumnodto.getApellido(), alumnodto.getDni());
        alumnoDaoMemoryImpl.guardarAlumno(alumno);
        return alumno;
    }

    @Override
    public Alumno borraralumnoId(long id) throws AlumnoNoEncontradoException {
        Alumno alumnoExistente = alumnoDaoMemoryImpl.buscarAlumnoporid(id);
        // Validación de existencia del alumno
        if (alumnoExistente == null) {
            throw new AlumnoNoEncontradoException("El alumno con ID " + id + " no existe en la base de datos.");
        }
        alumnoDaoMemoryImpl.borrarAlumnoporid(id);
        return alumnoExistente;
    }

    @Override
    public Alumno buscarAlumnoId(long idAlumno) throws AlumnoNoEncontradoException {
        Alumno alumno = alumnoDaoMemoryImpl.buscarAlumnoporid(idAlumno);
        if (alumno == null) {
            throw new AlumnoNoEncontradoException("El alumno con ID " + idAlumno + " no existe en la base de datos.");
        }
        return alumno;
    }

    @Override
    public Alumno modificarAlumno(long id, AlumnoDto alumnoModificado) throws AlumnoYaExisteException {
        Alumno alumnoExistente = alumnoDaoMemoryImpl.buscarAlumnoporid(id);

        // Validaciones
        if (alumnoModificado.getNombre() == null || alumnoModificado.getNombre().trim().isEmpty()) {
            throw new AlumnoYaExisteException("El nombre no puede estar vacío.");
        }
        if (alumnoModificado.getApellido() == null || alumnoModificado.getApellido().trim().isEmpty()) {
            throw new AlumnoYaExisteException("El apellido no puede estar vacío.");
        }

        // Validación de caracteres en nombre y apellido
        if (!alumnoModificado.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new AlumnoYaExisteException("El nombre solo puede contener letras y espacios.");
        }
        if (!alumnoModificado.getApellido().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new AlumnoYaExisteException("El apellido solo puede contener letras y espacios.");
        }

        // Validación de DNI
        if (alumnoModificado.getDni() <= 0) {
            throw new AlumnoYaExisteException("El DNI debe ser un número positivo.");
        }
        if (String.valueOf(alumnoModificado.getDni()).length() > 9) {
            throw new AlumnoYaExisteException("El DNI no puede tener más de 9 dígitos.");
        }

        if (alumnoDaoMemoryImpl.buscarAlumnopordni(alumnoModificado.getDni()) != null) {
            throw new AlumnoYaExisteException("El alumno con el DNI " + alumnoModificado.getDni() + " ya existe.");
        }
        // Actualizar los datos del alumno existente con los nuevos datos del DTO
        alumnoExistente.setDni(alumnoModificado.getDni());
        alumnoExistente.setApellido(alumnoModificado.getApellido());
        alumnoExistente.setNombre(alumnoModificado.getNombre());

        alumnoDaoMemoryImpl.modificarAlumno(alumnoExistente);
        return alumnoExistente;
    }

    @Override
    public List<Alumno> buscarAlumnos() {
        return alumnoDaoMemoryImpl.buscarAlumnos();
    }
}




