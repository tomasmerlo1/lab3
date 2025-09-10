package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.controller.validator.AsignaturaValidator;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaYaExisteException;
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

public class AsignaturaControllerTest {
    @InjectMocks
    private AsignaturaController asignaturaController;

    @Mock
    private AsignaturaService asignaturaService;

    @Mock
    private AsignaturaValidator asignaturaValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearAsignatura() throws AsignaturaYaExisteException {
        // Configurar datos de prueba
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setIdmateria(1);
        asignaturaDto.setIdalumno(1);
        asignaturaDto.setNota(8);
        asignaturaDto.setEstado(EstadoAsignatura.APROBADA);

        // Crear la asignatura de dominio (entidad)
        Asignatura asignatura = new Asignatura();
        asignatura.setIdmateria(1);
        asignatura.setIdalumno(1);
        asignatura.setNota(8);
        asignatura.setEstado(EstadoAsignatura.APROBADA);

        // Configurar comportamiento del validador (mocking)
        doNothing().when(asignaturaValidator).validarAsignatura(asignaturaDto);

        // Configurar comportamiento del servicio (mocking)
        when(asignaturaService.crearAsignatura(asignaturaDto)).thenReturn(asignatura);

        // Ejecutar el método del controlador
        ResponseEntity<Asignatura> response = asignaturaController.crearAsignatura(asignaturaDto);

        // Verificaciones
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Verifica que el código de respuesta sea 201 Created
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getIdmateria()); // Verifica que el idmateria sea 1
        assertEquals(1, response.getBody().getIdalumno()); // Verifica que el idalumno sea 1
        assertEquals(8, response.getBody().getNota()); // Verifica que la nota sea 8
        assertEquals(EstadoAsignatura.APROBADA, response.getBody().getEstado()); // Verifica que el estado sea APROBADA

        // Verificar interacciones con el validador y el servicio
        verify(asignaturaValidator, times(1)).validarAsignatura(asignaturaDto); // Verifica que el validador se haya llamado una vez
        verify(asignaturaService, times(1)).crearAsignatura(asignaturaDto); // Verifica que el servicio se haya llamado una vez
    }

    @Test
    public void testObtenerAsignatura() {
        Integer idAsignatura = 1;
        Asignatura asignatura = new Asignatura();
        asignatura.setNota(7);
        asignatura.setId(1);
        asignatura.setEstado(EstadoAsignatura.CURSADA);

        when(asignaturaService.buscarAsignaturaId(idAsignatura)).thenReturn(asignatura);

        ResponseEntity<Asignatura> response = asignaturaController.obtenerAsignatura(idAsignatura);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(asignatura, response.getBody());
        verify(asignaturaService, times(1)).buscarAsignaturaId(idAsignatura);
    }

    @Test
    public void testModificarAsignatura() {
        Integer idAsignatura = 1;
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setNota(8);
        Asignatura asignaturaModificada = new Asignatura();
        asignaturaModificada.setNota(8);

        when(asignaturaService.modificarAsignatura(idAsignatura, asignaturaDto)).thenReturn(asignaturaModificada);

        ResponseEntity<Asignatura> response = asignaturaController.modificarAsignatura(idAsignatura, asignaturaDto);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(asignaturaModificada, response.getBody());
        verify(asignaturaService, times(1)).modificarAsignatura(idAsignatura, asignaturaDto);
    }

    @Test
    public void testEliminarAsignatura() {
        Integer idAsignatura = 1;

        ResponseEntity<Void> response = asignaturaController.eliminarAsignatura(idAsignatura);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(asignaturaService, times(1)).borrarAsignaturaporid(idAsignatura);
    }
}
