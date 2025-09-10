package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.business.impl.ProfesorServiceImpl;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorYaExisteException;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.ProfesorDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfesorServiceImplTest {

    @InjectMocks
    private ProfesorServiceImpl profesorService;

    @Mock
    private ProfesorDaoMemoryImpl profesorDaoMemoryimpl;

    @Mock
    private MateriaDaoMemoryImpl materiaDaoMemoryimpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearProfesorValido() throws ProfesorYaExisteException {
        // Crear un ProfesorDto con datos válidos.
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Juan");
        profesorDto.setApellido("Pérez");
        profesorDto.setTitulo("Ingeniero");

        when(profesorDaoMemoryimpl.buscarProfesores()).thenReturn(new ArrayList<>()); // no hay profesores existentes
        Profesor profesorCreado = profesorService.crearProfesor(profesorDto);

        assertNotNull(profesorCreado);
        assertEquals("Juan", profesorCreado.getNombre());
        assertEquals("Pérez", profesorCreado.getApellido());
        assertEquals("Ingeniero", profesorCreado.getTitulo());
    }

    @Test
    public void testCrearProfesorProfesorExistente() {
        // Preparar un ProfesorDto
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Jose");
        profesorDto.setApellido("Luis");
        profesorDto.setTitulo("Tecnico");

        // Configurar el mock para retornar una lista de profesores ya existentes
        List<Profesor> listaProfesores = new ArrayList<>();
        listaProfesores.add(new Profesor("Jose", "Luis", "Tecnico"));
        when(profesorDaoMemoryimpl.buscarProfesores()).thenReturn(listaProfesores);

        // Intentar crear un profesor existente y verificar que se lanza la excepción esperada
        ProfesorYaExisteException exception = assertThrows(ProfesorYaExisteException.class, () -> {
            profesorService.crearProfesor(profesorDto);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Ya existe un profesor con el mismo nombre y apellido.", exception.getMessage());
    }

    @Test
    public void testBorrarProfesor() throws ProfesorNoEncontradoException {
        long id = 1L;
        Profesor profesor = new Profesor("Juan", "Pérez", "Ingeniero");

        when(profesorDaoMemoryimpl.buscarProfesorporid(id)).thenReturn(profesor);

        Profesor resultado = profesorService.borrarProfesorporid(id);

        assertNotNull(resultado);
        assertEquals(profesor, resultado);
        verify(profesorDaoMemoryimpl, times(1)).borrarProfesorporid(id);
    }

    @Test
    public void testBuscarProfesores() {
        List<Profesor> listaProfesores = new ArrayList<>();
        listaProfesores.add(new Profesor("Juan", "Pérez", "Ingeniero"));
        listaProfesores.add(new Profesor("María", "García", "Licenciada"));

        when(profesorDaoMemoryimpl.buscarProfesores()).thenReturn(listaProfesores);

        List<Profesor> resultado = profesorService.buscarProfesores();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(profesorDaoMemoryimpl, times(1)).buscarProfesores();
    }

    @Test
    public void testBuscaProfesorporid() {
        long id = 1L;
        Profesor profesor = new Profesor("Juan", "Pérez", "Ingeniero");

        when(profesorDaoMemoryimpl.buscarProfesorporid(id)).thenReturn(profesor);

        Profesor resultado = profesorService.buscaProfesorporid(id);

        assertNotNull(resultado);
        assertEquals(profesor, resultado);
        verify(profesorDaoMemoryimpl, times(1)).buscarProfesorporid(id);
    }

    @Test
    public void testModificarProfesor() throws ProfesorNoEncontradoException {
        long id = 1L;
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Carlos");
        profesorDto.setApellido("Ramírez");
        profesorDto.setTitulo("Doctor");

        Profesor profesorExistente = new Profesor("Juan", "Perez", "Ingeniero");

        when(profesorDaoMemoryimpl.buscarProfesorporid(id)).thenReturn(profesorExistente);

        Profesor resultado = profesorService.modificarProfesor(id, profesorDto);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("Ramírez", resultado.getApellido());
        assertEquals("Doctor", resultado.getTitulo());
        verify(profesorDaoMemoryimpl, times(1)).modificarProfesor(profesorExistente);
    }

    @Test
    public void testModificarProfesorNoExistente() {
        long id = 1L;
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("Carlos");
        profesorDto.setApellido("Ramírez");
        profesorDto.setTitulo("Doctor");

        when(profesorDaoMemoryimpl.buscarProfesorporid(id)).thenReturn(null);

        ProfesorNoEncontradoException exception = assertThrows(ProfesorNoEncontradoException.class, () -> {
            profesorService.modificarProfesor(id, profesorDto);
        });

        assertEquals("No se encontró un profesor con el ID proporcionado: 1", exception.getMessage());
    }



    @Test
    public void testModificarProfesorConDatosIncompletos() {
        long id = 1L;
        ProfesorDto profesorDto = new ProfesorDto();
        profesorDto.setNombre("");

        ProfesorNoEncontradoException exception = assertThrows(ProfesorNoEncontradoException.class, () -> {
            profesorService.modificarProfesor(id, profesorDto);
        });

        assertEquals("No se encontró un profesor con el ID proporcionado: 1", exception.getMessage());
    }

    @Test
    public void testBuscarProfesoresConListaVacia() {
        when(profesorDaoMemoryimpl.buscarProfesores()).thenReturn(new ArrayList<>());

        List<Profesor> resultado = profesorService.buscarProfesores();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testModificarProfesorSinNombre() {
        long id = 1L;
        ProfesorDto profesorDto = new ProfesorDto();

        ProfesorNoEncontradoException exception = assertThrows(ProfesorNoEncontradoException.class, () -> {
            profesorService.modificarProfesor(id, profesorDto);
        });

        assertEquals("No se encontró un profesor con el ID proporcionado: 1", exception.getMessage());
    }
}
