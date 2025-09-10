package ar.edu.utn.frbb.tup.business;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.MateriaYaExisteException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNoEncontradoException;


import java.util.List;

public interface MateriaService {
    Materia crearMateria(MateriaDto materia) throws ProfesorNoEncontradoException, MateriaYaExisteException;

    Materia buscarmateriaId(long id);

    List<Materia> buscarMateria();

    Materia modificarMateria(long id, MateriaDto materia) throws MateriaNoEncontradaException, ProfesorNoEncontradoException, MateriaYaExisteException;

    Materia borrarmateriaId(long id) throws MateriaNoEncontradaException;

}
