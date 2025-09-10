package ar.edu.utn.frbb.tup.business;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraYaExisteEstaException;

import java.util.List;

public interface CarreraService {
    Carrera crearCarrera(CarreraDto carrera)throws CarreraYaExisteEstaException;

    Carrera buscarCarreraId(long id);

    List<Carrera> buscarCarreras();

    Carrera modificarCarrera(long id, CarreraDto carrera) throws CarreraYaExisteEstaException;

    Carrera borrarCarreraporid(long id);

   Carrera buscarCarreraPornombre(String nombre);
}
