package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.controller.CarreraController;
import ar.edu.utn.frbb.tup.controller.validator.carreraValidator;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraYaExisteEstaException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CarreraControllerTest {
    @InjectMocks
    private CarreraController carreraController;

    @Mock
    private CarreraService carreraService;

    @Mock
    private ProfesorService profesorService;

    @Mock
    private carreraValidator carreraValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCarrera() throws CarreraYaExisteEstaException {
        // Configurar los datos de prueba
        CarreraDto carreraDto = new CarreraDto();
        carreraDto.setNombre("Abogacía");

        Carrera nuevaCarrera = new Carrera();
        nuevaCarrera.setNombre("Abogacía");

        // Configurar el comportamiento del validador
        doNothing().when(carreraValidator).carreraValidation(carreraDto);

        // Configurar el comportamiento del servicio
        when(carreraService.crearCarrera(carreraDto)).thenReturn(nuevaCarrera);


        ResponseEntity<Carrera> response = carreraController.crearCarrera(carreraDto);

        // Verificaciones
        assertNotNull(response); // Verificar que la respuesta no sea nula
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Verificar que el código de respuesta sea 201 (CREATED)
        assertEquals(nuevaCarrera, response.getBody()); // Verificar que el cuerpo de la respuesta sea la carrera esperada

        // Verificar las interacciones con el servicio
        verify(carreraValidator, times(1)).carreraValidation(carreraDto); // Verificar que se validó la carrera
        verify(carreraService, times(1)).crearCarrera(carreraDto); // Verificar que se llamó al servicio de creación de carrera
    }

    @Test
    public void testBuscarCarreraId() {
        Integer idCarrera = 1;
        Carrera carrera = new Carrera();
        when(carreraService.buscarCarreraId(idCarrera)).thenReturn(carrera);

        Carrera resultado = carreraController.buscarCarreraId(idCarrera);

        assertNotNull(resultado);
        assertEquals(carrera, resultado);
        verify(carreraService, times(1)).buscarCarreraId(idCarrera);
    }

    @Test
    public void testModificarCarrera() throws CarreraYaExisteEstaException {
        Integer idCarrera = 1;
        CarreraDto carreraDto = new CarreraDto();
        Carrera carreraModificada = new Carrera();
        when(carreraService.modificarCarrera(idCarrera, carreraDto)).thenReturn(carreraModificada);

        Carrera resultado = carreraController.modificarCarrera(idCarrera, carreraDto);

        assertNotNull(resultado);
        assertEquals(carreraModificada, resultado);
        verify(carreraService, times(1)).modificarCarrera(idCarrera, carreraDto);
    }

    @Test
    public void testEliminarProfesor() throws ProfesorNoEncontradoException {
        Integer idProfesor = 1;
        Profesor profesor = new Profesor("Felipe", "Garcia", "licenciado");
        when(profesorService.borrarProfesorporid(idProfesor)).thenReturn(profesor);

        Profesor resultado = carreraController.eliminarProfesor(idProfesor);

        assertNotNull(resultado);
        assertEquals(profesor, resultado);
        verify(profesorService, times(1)).borrarProfesorporid(idProfesor);
    }
}
