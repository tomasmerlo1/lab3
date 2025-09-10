package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.business.impl.CarreraServiceImpl;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CarreraYaExisteEstaException;
import ar.edu.utn.frbb.tup.persistence.CarreraDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarreraServiceImplTest {

    @InjectMocks
    private CarreraServiceImpl carreraService;

    @Mock
    private CarreraDaoMemoryImpl carreraDaoMemoryImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCarreraConDatosValidos() throws CarreraYaExisteEstaException {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Ingenieria en Sistemas");

        Carrera carrera = new Carrera("Ingenieria en Sistemas");

        //when(carreraDaoMemoryImpl.guardarCarrera(any(Carrera.class))).thenReturn(carrera);

        Carrera resultado = carreraService.crearCarrera(carreraDto);

        assertNotNull(resultado);
        assertEquals(carreraDto.getNombre(), resultado.getNombre());
        verify(carreraDaoMemoryImpl, times(1)).guardarCarrera(any(Carrera.class));
    }

    @Test
    public void testCrearCarreraConNombreVacio() {
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("  ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carreraService.crearCarrera(carreraDto);
        });

        assertEquals("El nombre de la carrera no puede estar vacío.", exception.getMessage());
        verify(carreraDaoMemoryImpl, never()).guardarCarrera(any(Carrera.class));
    }

    @Test
    public void testBuscarCarreras() {
        List<Carrera> listaCarreras = new ArrayList<>();
        listaCarreras.add(new Carrera("Ingenieria en Sistemas"));
        listaCarreras.add(new Carrera("Licenciatura en Administracion"));

        when(carreraDaoMemoryImpl.buscarCarrera()).thenReturn(listaCarreras);

        List<Carrera> resultado = carreraService.buscarCarreras();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(carreraDaoMemoryImpl, times(1)).buscarCarrera();
    }

    @Test
    public void testBuscarCarreraPorId() {
        long id = 1;
        Carrera carrera = new Carrera("Ingenieria en Sistemas");
        when(carreraDaoMemoryImpl.buscarCarreraporId(id)).thenReturn(carrera);

        Carrera resultado = carreraService.buscarCarreraId(id);

        assertNotNull(resultado);
        assertEquals("Ingenieria en Sistemas", resultado.getNombre());
        verify(carreraDaoMemoryImpl, times(1)).buscarCarreraporId(id);
    }

    @Test
    public void testModificarCarreraExistente() throws CarreraYaExisteEstaException {
        long id = 1;
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Ingenieria en Software");

        Carrera carreraExistente = new Carrera("Ingenieria en Sistemas");
        when(carreraDaoMemoryImpl.buscarCarreraporId(id)).thenReturn(carreraExistente);

        Carrera resultado = carreraService.modificarCarrera(id, carreraDto);

        assertNotNull(resultado);
        assertEquals("Ingenieria en Software", resultado.getNombre());
        verify(carreraDaoMemoryImpl, times(1)).modificarCarrera(carreraExistente);
    }

    @Test
    public void testModificarCarreraNoExistente() {
        long id = 1;
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Ingenieria Quimica");

        when(carreraDaoMemoryImpl.buscarCarreraporId(id)).thenReturn(null);

        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> {
            carreraService.modificarCarrera(id, carreraDto);
        });

        assertEquals("No se encontró la carrera con el ID: " + id, exception.getMessage());
    }

    @Test
    public void testBorrarCarreraPorIdExistente() {
        long id = 1;
        Carrera carreraExistente = new Carrera("Ingenieria en Sistemas");
        when(carreraDaoMemoryImpl.buscarCarreraporId(id)).thenReturn(carreraExistente);

        Carrera resultado = carreraService.borrarCarreraporid(id);

        assertNotNull(resultado);
        assertEquals("Ingenieria en Sistemas", resultado.getNombre());
        verify(carreraDaoMemoryImpl, times(1)).borrarCarreraporid(id);
    }

    @Test
    public void testBorrarCarreraPorIdNoExistente() {
        long id = 3;
        when(carreraDaoMemoryImpl.buscarCarreraporId(id)).thenReturn(null);

        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> {
            carreraService.borrarCarreraporid(id);
        });

        assertEquals("No se encontró la carrera con el ID: " + id, exception.getMessage());
    }

    @Test
    public void testModificarCarreraConDatosIncompletos() {
        long id = 1;
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre(""); // Nombre vacío

        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> {
            carreraService.modificarCarrera(id, carreraDto);
        });

        assertEquals("No se encontró la carrera con el ID: 1", exception.getMessage());
    }

    @Test
    public void testBuscarCarrerasConListaVacia() {
        when(carreraDaoMemoryImpl.buscarCarrera()).thenReturn(new ArrayList<>());

        List<Carrera> resultado = carreraService.buscarCarreras();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testModificarCarreraSinNombre() {
        long id = 1;
        CarreraDto carreraDto = new CarreraDto();

        CarreraNotFoundException exception = assertThrows(CarreraNotFoundException.class, () -> {
            carreraService.modificarCarrera(id, carreraDto);
        });

        assertEquals("No se encontró la carrera con el ID: 1", exception.getMessage());
    }
}
