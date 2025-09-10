package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.controller.validator.alumnoValidator;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.AlumnoYaExisteException;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AlumnoControllerTest {

    @InjectMocks
    private AlumnoController alumnoController;

    @Mock
    private AlumnoService alumnoService;

    @Mock
    private alumnoValidator alumValidator;
    @Mock
    private AsignaturaService asignaturaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearAlumno() throws AlumnoYaExisteException, AlumnoNoEncontradoException {
        // Configurar datos de prueba
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan Perez");
        alumnoDto.setDni(12345678);

        Alumno alumno = new Alumno();
        alumno.setNombre("Juan Perez");
        alumno.setApellido("Garcia");
        alumno.setDni(12345678);

        // Configurar comportamiento del validador
        doNothing().when(alumValidator).validarAlumno(alumnoDto);

        // Configurar comportamiento del servicio
        when(alumnoService.crearAlumno(alumnoDto)).thenReturn(alumno);


        ResponseEntity<Alumno> response = alumnoController.crearAlumno(alumnoDto);

        // Verificaciones
        assertNotNull(response);
        assertEquals(ResponseEntity.status(201).build().getStatusCode(), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan Perez", response.getBody().getNombre());
        assertEquals(12345678, response.getBody().getDni());

        // Verificar interacciones con el validador y el servicio
        verify(alumValidator, times(1)).validarAlumno(alumnoDto);
        verify(alumnoService, times(1)).crearAlumno(alumnoDto);
    }

    @Test
    public void testBuscarAlumnoId() throws AlumnoNoEncontradoException {
        Integer idAlumno = 1;
        Alumno alumno = new Alumno();

        when(alumnoService.buscarAlumnoId(idAlumno)).thenReturn(alumno);

        Alumno result = alumnoController.buscarAlumnoId(idAlumno);

        assertNotNull(result);
        assertEquals(alumno, result);
        verify(alumnoService, times(1)).buscarAlumnoId(idAlumno);
    }

    @Test
    public void testModificarAlumno() throws AlumnoNoEncontradoException, AlumnoYaExisteException {
        Integer idAlumno = 1;
        AlumnoDto alumnoDto = new AlumnoDto();
        Alumno alumno = new Alumno();

        when(alumnoService.modificarAlumno(idAlumno, alumnoDto)).thenReturn(alumno);

        Alumno result = alumnoController.modificarAlumno(idAlumno, alumnoDto);

        assertNotNull(result);
        assertEquals(alumno, result);
        verify(alumnoService, times(1)).modificarAlumno(idAlumno, alumnoDto);
    }

    @Test
    public void testModificarEstadoAsignatura() throws AsignaturaNoEncontradaException, EstadoIncorrectoException {
        Integer idAlumno = 1;
        Integer idAsignatura = 1;
        Asignatura asignatura = new Asignatura();

        when(asignaturaService.modificarEstadoAsignatura(idAlumno, idAsignatura)).thenReturn(asignatura);

        Asignatura result = alumnoController.modificarEstadoAsignatura(idAlumno, idAsignatura);

        assertNotNull(result);
        assertEquals(asignatura, result);
        verify(asignaturaService, times(1)).modificarEstadoAsignatura(idAlumno, idAsignatura);
    }

    @Test
    public void testEliminarAlumno() throws AlumnoNoEncontradoException {
        Integer idAlumno = 1;
        Alumno alumno = new Alumno();

        when(alumnoService.borraralumnoId(idAlumno)).thenReturn(alumno);

        Alumno result = alumnoController.eliminarAlumno(idAlumno);

        assertNotNull(result);
        assertEquals(alumno, result);
        verify(alumnoService, times(1)).borraralumnoId(idAlumno);
    }
}
