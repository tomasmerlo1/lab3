package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.business.impl.AlumnoServiceImpl;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.AlumnoYaExisteException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AlumnoServiceImplTest {

    @Mock
    private AlumnoDaoMemoryImpl alumnoDaoMemoryImpl;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearAlumno() throws AlumnoYaExisteException {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan");
        alumnoDto.setApellido("Perez");
        alumnoDto.setDni(12345678);
        Alumno alumno = new Alumno("Juan", "Perez", 12345678);

        Alumno result = alumnoService.crearAlumno(alumnoDto);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("Perez", result.getApellido());
        assertEquals(12345678, result.getDni());
        verify(alumnoDaoMemoryImpl, times(1)).guardarAlumno(any(Alumno.class));
    }

    @Test
    public void testBorrarAlumnoId() throws AlumnoNoEncontradoException {
        long id = 4L;
        Alumno alumno = new Alumno("Juan", "Perez", 12345678);

        when(alumnoDaoMemoryImpl.buscarAlumnoporid(id)).thenReturn(alumno);
        when(alumnoDaoMemoryImpl.borrarAlumnoporid(id)).thenReturn(alumno);

        Alumno result = alumnoService.borraralumnoId(id);

        assertNotNull(result);
        assertEquals(alumno, result);
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnoporid(id);
        verify(alumnoDaoMemoryImpl, times(1)).borrarAlumnoporid(id);
    }

    @Test
    public void testBuscarAlumnoId() throws AlumnoNoEncontradoException {
        long id = 1L;
        Alumno alumno = new Alumno("Juan", "Perez", 12345678);

        when(alumnoDaoMemoryImpl.buscarAlumnoporid(id)).thenReturn(alumno);

        Alumno result = alumnoService.buscarAlumnoId(id);

        assertNotNull(result);
        assertEquals(alumno, result);
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnoporid(id);
    }

    @Test
    public void testModificarAlumno() throws AlumnoNoEncontradoException, AlumnoYaExisteException {
        long id = 1L;
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Carlos");
        alumnoDto.setApellido("Lopez");
        alumnoDto.setDni(87654321);
        Alumno alumnoExistente = new Alumno("Juan", "Perez", 12345678);

        when(alumnoDaoMemoryImpl.buscarAlumnoporid(id)).thenReturn(alumnoExistente);
        when(alumnoDaoMemoryImpl.modificarAlumno(any(Alumno.class))).thenReturn(alumnoExistente);

        Alumno result = alumnoService.modificarAlumno(id, alumnoDto);

        assertNotNull(result);
        assertEquals("Carlos", result.getNombre());
        assertEquals("Lopez", result.getApellido());
        assertEquals(87654321, result.getDni());
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnoporid(id);
        verify(alumnoDaoMemoryImpl, times(1)).modificarAlumno(any(Alumno.class));
    }

    @Test
    public void testBuscarAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        alumnos.add(new Alumno("Juan", "Perez", 12345678));
        alumnos.add(new Alumno("Ana", "Garcia", 87654321));

        when(alumnoDaoMemoryImpl.buscarAlumnos()).thenReturn(alumnos);

        List<Alumno> result = alumnoService.buscarAlumnos();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnos();
    }

    @Test
    public void testBorrarAlumnoIdAlumnoNoEncontrado() throws AlumnoNoEncontradoException {
        long id = 99L; // Un ID que sabemos que no existe

        when(alumnoDaoMemoryImpl.buscarAlumnoporid(id)).thenReturn(null);

        assertThrows(AlumnoNoEncontradoException.class, () -> alumnoService.borraralumnoId(id));
    }

    @Test
    public void testCrearAlumnoConDniYaExistente() throws AlumnoYaExisteException {
        AlumnoDto alumnoDto = new AlumnoDto();
        alumnoDto.setNombre("Juan");
        alumnoDto.setApellido("Perez");
        alumnoDto.setDni(12345678); // DNI ya existente

        when(alumnoDaoMemoryImpl.buscarAlumnopordni(12345678)).thenReturn(new Alumno("Juan", "Perez", 12345678));

        assertThrows(AlumnoYaExisteException.class, () -> alumnoService.crearAlumno(alumnoDto));
    }

    @Test
    public void testBuscarAlumnosListaVacia() {
        when(alumnoDaoMemoryImpl.buscarAlumnos()).thenReturn(new ArrayList<>());

        List<Alumno> result = alumnoService.buscarAlumnos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnos();
    }

    @Test
    public void testBuscarAlumnosNoContieneElementosNulos() {
        List<Alumno> alumnos = new ArrayList<>();
        alumnos.add(new Alumno("Juan", "Perez", 12345678));
        alumnos.add(new Alumno("Ana", "Garcia", 87654321));

        when(alumnoDaoMemoryImpl.buscarAlumnos()).thenReturn(alumnos);

        List<Alumno> result = alumnoService.buscarAlumnos();

        for (Alumno alumno : result) {
            assertNotNull(alumno);
        }

        verify(alumnoDaoMemoryImpl, times(1)).buscarAlumnos();
    }
}
