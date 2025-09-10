package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CarreraYaExisteEstaException;
import ar.edu.utn.frbb.tup.persistence.CarreraDaoMemoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarreraServiceImpl implements CarreraService {

    @Autowired
    private CarreraDaoMemoryImpl carreraDaoMemoryImpl;

    @Override
    public Carrera crearCarrera(CarreraDto carreraDto) throws CarreraYaExisteEstaException {
        if (carreraDto.getNombre() == null || carreraDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la carrera no puede estar vacío.");
        }

        // Validar que el nombre no contenga números
        if (carreraDto.getNombre().matches(".*\\d.*")) {
            throw new IllegalArgumentException("El nombre de la carrera no puede contener números.");
        }

        Carrera carreraExistente = carreraDaoMemoryImpl.buscarCarrerasPorNombre(carreraDto.getNombre());
        if (carreraExistente != null) {
            throw new CarreraYaExisteEstaException("Ya existe una carrera con el nombre especificado.");
        }

        Carrera carrera = new Carrera(carreraDto.getNombre());
        carreraDaoMemoryImpl.guardarCarrera(carrera);
        return carrera;
    }

    @Override
    public List<Carrera> buscarCarreras() {
        return carreraDaoMemoryImpl.buscarCarrera();
    }

    @Override
    public Carrera buscarCarreraId(long id) {
        Carrera carreraExistente = carreraDaoMemoryImpl.buscarCarreraporId(id);
        if (carreraExistente == null) {
            throw new CarreraNotFoundException("No se encontró la carrera con el ID: " + id);
        }
        return carreraExistente;
    }

    @Override
    public Carrera modificarCarrera(long id, CarreraDto carreraDto) throws CarreraYaExisteEstaException {
        Carrera carreraExistente = carreraDaoMemoryImpl.buscarCarreraporId(id);
        if (carreraExistente == null) {
            throw new CarreraNotFoundException("No se encontró la carrera con el ID: " + id);
        }
        if (carreraDto.getNombre() == null || carreraDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la carrera no puede estar vacío.");
        }

        // Validar que el nombre no contenga números
        if (carreraDto.getNombre().matches(".*\\d.*")) {
            throw new IllegalArgumentException("El nombre de la carrera no puede contener números.");
        }


        Carrera carrera = carreraDaoMemoryImpl.buscarCarrerasPorNombre(carreraDto.getNombre());
        if (carrera != null) {
            throw new CarreraYaExisteEstaException("Ya existe una carrera con el nombre especificado.");
        }
        carreraExistente.setNombre(carreraDto.getNombre());
        carreraDaoMemoryImpl.modificarCarrera(carreraExistente);
        return carreraExistente;
    }

    @Override
    public Carrera borrarCarreraporid(long id) {
        Carrera carreraExistente = carreraDaoMemoryImpl.buscarCarreraporId(id);
        if (carreraExistente == null) {
            throw new CarreraNotFoundException("No se encontró la carrera con el ID: " + id);
        }

        carreraDaoMemoryImpl.borrarCarreraporid(id);
        return carreraExistente;
    }

    @Override
    public Carrera buscarCarreraPornombre(String nombre) {
        Carrera carrera = carreraDaoMemoryImpl.buscarCarrerasPorNombre(nombre);
        if (carrera == null) {
            throw new CarreraNotFoundException("No se encontró la carrera con el nombre: " + nombre);
        }
        return carrera;
    }
}

