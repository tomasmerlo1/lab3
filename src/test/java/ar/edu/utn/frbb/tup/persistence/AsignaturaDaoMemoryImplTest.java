package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaNoEncontradaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AsignaturaDaoMemoryImplTest {

    @InjectMocks
    private AsignaturaDaoMemoryImpl asignaturaDaoMemoryImpl;

    @Mock
    private BufferedReader bufferedReader;

    @Mock
    private PrintWriter printWriter;

    @Mock
    private FileReader fileReader;

    @Mock
    private FileWriter fileWriter;

    private static final String CSV_FILE_PATH = "D:/lab3/src/main/java/ar/edu/utn/frbb/tup/persistence/dataCSV/asignaturaDATA.csv";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGuardarAsignatura() throws IOException {
        FileWriter writer = new FileWriter(CSV_FILE_PATH);
        writer.close(); // Cierra el archivo vacío

        Asignatura asignatura = new Asignatura(1L, EstadoAsignatura.CURSADA, 8, 123L, 456L);

        asignaturaDaoMemoryImpl.guardarAsignatura(asignatura);

        BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH));
        String line = reader.readLine();
        reader.close();

        assertEquals("1,123,456,8,CURSADA", line);
    }

    @Test
    public void testBuscarAsignaturaPorId() throws IOException {
        when(bufferedReader.readLine())
                .thenReturn("1,APROBADA,8,123,456")
                .thenReturn("1,123,456,8,CURSADA")
                .thenReturn(null);

        Asignatura asignatura = asignaturaDaoMemoryImpl.buscarAsignaturaporId(1L);

        assertNotNull(asignatura);
        assertEquals(1L, asignatura.getId());
        assertTrue(asignatura.getEstado() == EstadoAsignatura.APROBADA || asignatura.getEstado() == EstadoAsignatura.CURSADA);
        assertEquals(8, asignatura.getNota());
    }

    @Test
    public void testBorrarAsignaturaPorId() {
        Asignatura asignatura = new Asignatura(2L, EstadoAsignatura.APROBADA, 7, 124L, 457L);
        asignaturaDaoMemoryImpl.guardarAsignatura(asignatura);

        assertNotNull(asignaturaDaoMemoryImpl.buscarAsignaturaporId(2L));

        Asignatura deletedAsignatura = asignaturaDaoMemoryImpl.borrarAsignaturaporid(2L);

        assertNotNull(deletedAsignatura);
        assertEquals(7L, deletedAsignatura.getId());
        assertEquals(EstadoAsignatura.APROBADA, deletedAsignatura.getEstado());

        assertNull(asignaturaDaoMemoryImpl.buscarAsignaturaporId(2L));
    }

    @Test
    public void testBorrarAsignaturaPorIdNoExistente() {
        Asignatura deletedAsignatura = asignaturaDaoMemoryImpl.borrarAsignaturaporid(10L); // Asignatura no existe

        assertNull(deletedAsignatura); // Debe retornar null
    }

    @Test
    public void testModificarAsignatura() throws IOException {
        Asignatura asignaturaOriginal = new Asignatura(3L, EstadoAsignatura.APROBADA, 8, 123L, 456L);
        Asignatura asignaturaModificada = new Asignatura(3L, EstadoAsignatura.CURSADA, 9, 124L, 457L);

        asignaturaDaoMemoryImpl.guardarAsignatura(asignaturaOriginal);

        asignaturaDaoMemoryImpl.modificarAsignatura(asignaturaModificada);

        Asignatura asignaturaRecuperada = asignaturaDaoMemoryImpl.buscarAsignaturaporId(3L);

        assertNotNull(asignaturaRecuperada);
        assertEquals(EstadoAsignatura.CURSADA, asignaturaRecuperada.getEstado());
        assertEquals(9, asignaturaRecuperada.getNota());
    }

    @Test
    public void testActualizarEstadoAsignatura() throws IOException {
        Asignatura asignatura = new Asignatura(6L, EstadoAsignatura.APROBADA, 6, 125L, 458L);
        asignaturaDaoMemoryImpl.guardarAsignatura(asignatura);

        asignatura.setEstado(EstadoAsignatura.CURSADA);
        asignaturaDaoMemoryImpl.modificarAsignatura(asignatura);

        Asignatura asignaturaActualizada = asignaturaDaoMemoryImpl.buscarAsignaturaporId(6L);

        assertEquals(EstadoAsignatura.CURSADA, asignaturaActualizada.getEstado());
    }
}
