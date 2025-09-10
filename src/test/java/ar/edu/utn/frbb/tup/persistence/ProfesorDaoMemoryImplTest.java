package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfesorDaoMemoryImplTest {

    @Mock
    private ProfesorDaoMemoryImpl profesorDao;

    private Profesor profesor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Configuración básica del profesor para pruebas
        profesor = new Profesor(1L, "Juan", "Perez", "Licenciado en Física");
    }

    @Test
    public void testGuardarProfesor() {
        doNothing().when(profesorDao).guardarProfesor(any(Profesor.class));
        profesorDao.guardarProfesor(profesor);

        verify(profesorDao, times(1)).guardarProfesor(any(Profesor.class));
    }

    @Test
    public void testBuscarProfesorporId() throws IOException {
        when(profesorDao.buscarProfesorporid(1L)).thenReturn(profesor);

        Profesor encontrado = profesorDao.buscarProfesorporid(1L);

        assertNotNull(encontrado);
        assertEquals("Juan", encontrado.getNombre());
        assertEquals("Perez", encontrado.getApellido());
        assertEquals("Licenciado en Física", encontrado.getTitulo());
    }

    @Test
    public void testModificarProfesor() throws IOException {
        profesor.setNombre("Carlos");
        profesor.setApellido("Gonzalez");

        when(profesorDao.modificarProfesor(any(Profesor.class))).thenReturn(profesor);

        Profesor modificado = profesorDao.modificarProfesor(profesor);

        assertNotNull(modificado);
        assertEquals("Carlos", modificado.getNombre());
        assertEquals("Gonzalez", modificado.getApellido());

        verify(profesorDao, times(1)).modificarProfesor(any(Profesor.class));
    }

    @Test
    public void testObtenerUltimoId() throws IOException {
        when(profesorDao.obtenerUltimoId()).thenReturn(5);

        int ultimoId = profesorDao.obtenerUltimoId();

        assertEquals(5, ultimoId);
    }
}
