package ar.edu.utn.frbb.tup.business;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaYaExisteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

import java.util.List;

public interface AsignaturaService {
    Asignatura crearAsignatura(AsignaturaDto asignatura)throws AsignaturaYaExisteException;

    Asignatura buscarAsignaturaId(long id);

    List<Asignatura> buscarAsignaturas();

    Asignatura modificarAsignatura(long id, AsignaturaDto asignatura);

    Asignatura borrarAsignaturaporid(long id);

    Asignatura modificarEstadoAsignatura(long idAlumno, long idAsignatura) throws AsignaturaNoEncontradaException, EstadoIncorrectoException;
}
