package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDaoMemoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    @Autowired
    private AsignaturaDaoMemoryImpl asignaturaDaoMemoryImpl;
    @Autowired
    private AlumnoDaoMemoryImpl alumnoDaoMemoryImpl;
    @Autowired
    private MateriaServiceImpl materiaServiceImpl;

    @Override
    public Asignatura crearAsignatura(AsignaturaDto asignaturaDto) throws AsignaturaYaExisteException {
        // Verificar que los datos del DTO sean válidos
        if (asignaturaDto.getNota() == null) {
            throw new IllegalArgumentException("La nota no puede ser nula");
        }

        if (asignaturaDto.getIdalumno() <= 0) {
            throw new IllegalArgumentException("El ID del alumno es inválido");
        }
        if (asignaturaDto.getNota() < 1) {
            throw new IllegalArgumentException("El ID del alumno es inválido");
        }


        if (asignaturaDto.getIdmateria() <= 0) {
            throw new IllegalArgumentException("El ID de la materia es inválido");
        }

        // Verificar si el alumno y la materia existen
        if (alumnoDaoMemoryImpl.buscarAlumnoporid(asignaturaDto.getIdalumno()) == null) {
            throw new IllegalStateException("El alumno con ID " + asignaturaDto.getIdalumno() + " no existe");
        }

        if (materiaServiceImpl.buscarmateriaId(asignaturaDto.getIdmateria()) == null) {
            throw new IllegalStateException("La materia con ID " + asignaturaDto.getIdmateria() + " no existe");
        }

        // Crear una nueva Asignatura a partir del DTO
        Asignatura asignatura = new Asignatura(
                asignaturaDto.getEstado(),
                asignaturaDto.getNota(),
                asignaturaDto.getIdalumno(),
                asignaturaDto.getIdmateria()
        );

        // Guardar la asignatura en el DAO
        asignaturaDaoMemoryImpl.guardarAsignatura(asignatura);

        return asignatura;
    }

    @Override
    public Asignatura buscarAsignaturaId(long id) {
        Asignatura asignatura= asignaturaDaoMemoryImpl.buscarAsignaturaporId(id);
        if (asignatura == null) {
            throw new IllegalArgumentException("No se encontró el asignatura con ese ID: " + id);
        }
        return asignaturaDaoMemoryImpl.buscarAsignaturaporId(id);
    }


    @Override
    public List<Asignatura> buscarAsignaturas() {
        return asignaturaDaoMemoryImpl.buscarAsignaturas();
    }

    @Override
    public Asignatura borrarAsignaturaporid(long id) {
        Asignatura asignaturaExistente = asignaturaDaoMemoryImpl.borrarAsignaturaporid(id);
        if (asignaturaExistente != null) {
            asignaturaDaoMemoryImpl.borrarAsignaturaporid(id);
        }
        if(asignaturaExistente==null){
            throw new NoseEncontroAsignatura("no se encontro el id de esa asignatura");
        }
        return asignaturaExistente;
    }

    @Override
    public Asignatura modificarAsignatura(long id, AsignaturaDto asignaturaDto) {
        Asignatura asignaturaExistente = asignaturaDaoMemoryImpl.buscarAsignaturaporId(id);

        if (asignaturaExistente == null) {
            // Lanzar excepción personalizada si no se encuentra la asignatura
            throw new NoseEncontroAsignatura("No se encontró la asignatura con el ID proporcionado: " + id);
        }

        // Validaciones de los campos del DTO
        if (asignaturaDto.getEstado() == null) {
            throw new IllegalArgumentException("El estado de la asignatura no puede ser nulo.");
        }

        if (asignaturaDto.getNota() == null) {
            throw new IllegalArgumentException("La nota no puede ser nula.");
        }

        if (asignaturaDto.getNota() < 1 || asignaturaDto.getNota() > 10) {
            throw new IllegalArgumentException("La nota debe estar entre 1 y 10.");
        }

        if (asignaturaDto.getIdalumno() <= 0) {
            throw new IllegalArgumentException("El ID del alumno es inválido.");
        }

        if (asignaturaDto.getIdmateria() <= 0) {
            throw new IllegalArgumentException("El ID de la materia es inválido.");
        }

        // Verificar si el alumno existe
        if (alumnoDaoMemoryImpl.buscarAlumnoporid(asignaturaDto.getIdalumno()) == null) {
            throw new IllegalStateException("El alumno con ID " + asignaturaDto.getIdalumno() + " no existe.");
        }

        // Verificar si la materia existe
        if (materiaServiceImpl.buscarmateriaId(asignaturaDto.getIdmateria()) == null) {
            throw new IllegalStateException("La materia con ID " + asignaturaDto.getIdmateria() + " no existe.");
        }

        // Actualizar los datos de la asignatura existente con los nuevos datos del DTO
        asignaturaExistente.setEstado(asignaturaDto.getEstado());
        asignaturaExistente.setNota(asignaturaDto.getNota());
        asignaturaExistente.setIdmateria(asignaturaDto.getIdmateria());
        asignaturaExistente.setIdalumno(asignaturaDto.getIdalumno());

        // Guardar los cambios
        asignaturaDaoMemoryImpl.modificarAsignatura(asignaturaExistente);

        return asignaturaExistente;
    }

    @Override
    public Asignatura modificarEstadoAsignatura(long idAlumno, long idAsignatura) throws AsignaturaNoEncontradaException, EstadoInvalidoException {
        // Buscar la asignatura asociada a este alumno
        Asignatura asignaturaExistente = asignaturaDaoMemoryImpl.buscarAsignaturaporIdAsignaturaIdAlumno(idAsignatura, idAlumno);

        if (asignaturaExistente == null) {
            throw new AsignaturaNoEncontradaException("No se encontró una asignatura para este alumno con id " + idAsignatura + " y alumno id " + idAlumno);
        }

        EstadoAsignatura estadoActual = asignaturaExistente.getEstado();
        int numeroPosicion = estadoActual.ordinal();

        // Verificar si ya es el último estado permitido
        if (numeroPosicion >= EstadoAsignatura.values().length - 1) {
            throw new EstadoInvalidoException("El estado actual no permite avanzar más.");
        }

        // Verificar si la nota es menor o igual a 4
        if (asignaturaExistente.getNota() <= 4) {
            throw new EstadoInvalidoException("No se puede avanzar más debido a que la nota es de 4 o menos.");
        }

        // Si la nota es 6, el estado tampoco debe avanzar
        if (asignaturaExistente.getNota() == 6) {
            throw new EstadoInvalidoException("No se puede avanzar más debido a que la nota es de 6.");
        }

        // Si la nota es mayor a 6, avanzar al siguiente estado
        if (numeroPosicion < EstadoAsignatura.values().length - 1) {
            if (asignaturaExistente.getNota() >= 7) {
                asignaturaExistente.setEstado(EstadoAsignatura.APROBADA);
            } else if (asignaturaExistente.getNota() <= 6) {
                asignaturaExistente.setEstado(EstadoAsignatura.CURSADA);
            }

            // Guardar la asignatura con su nuevo estado
            asignaturaDaoMemoryImpl.modificarAsignatura(asignaturaExistente);
            return asignaturaExistente;
        } else {
            throw new EstadoInvalidoException("El estado actual no permite avanzar más.");
        }
    }


}
