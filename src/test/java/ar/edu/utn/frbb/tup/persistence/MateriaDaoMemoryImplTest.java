package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MateriaDaoMemoryImplTest {

    @Mock
    private Materia materiaMock;

    @InjectMocks
    private MateriaDaoMemoryImpl materiaDao;

    private final String testFilePath = "C:/tup/Academic-Management-System/src/main/java/ar/edu/utn/frbb/tup/persistence/dataCSV/materiaDATA.csv";

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        File testFile = new File(testFilePath);
        if (testFile.exists()) {
            assertTrue(testFile.delete());
        }
        assertTrue(testFile.createNewFile(), "Error al crear el archivo de prueba");
    }

    @Test
    public void testGuardarMateria() throws IOException {
        // Crear un objeto Materia
        Materia materia = new Materia(1L, "Matemática", 2024, 1, 100L, new ArrayList<>());

        // Llamar al método guardarMateria
        materiaDao.guardarMateria(materia);

        // Leer el archivo para verificar que se haya guardado la información
        try (BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {
            String line = reader.readLine();
            assertNotNull(line, "El archivo no tiene contenido");
            assertTrue(line.contains("Matemática"), "El nombre de la materia no está en el archivo");
        }
    }

    @Test
    public void testBuscarMaterias() throws IOException {
        // Preparar archivo con datos
        try (FileWriter writer = new FileWriter(testFilePath)) {
            writer.write("1,Matemática,1,1,100,\n");
            writer.write("2,Física,2,1,101,\n");
        }


        List<Materia> materias = materiaDao.buscarMaterias();

        // Verifica la cantidad de materias y sus nombres
        assertEquals(2, materias.size(), "No se ha leído correctamente el número de materias");
        assertEquals("Matemática", materias.get(0).getNombre(), "El nombre de la primera materia es incorrecto");
        assertEquals("Física", materias.get(1).getNombre(), "El nombre de la segunda materia es incorrecto");
    }

    @Test
    public void testBuscarMateriaId() throws IOException {
        // Preparar archivo con datos
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilePath))) {
            writer.println("1,Matemática,1,1,100,");
        }

        Materia materia = materiaDao.buscarMateriaId(1L);
        assertNotNull(materia, "La materia no debería ser nula");
        assertEquals("Matemática", materia.getNombre(), "El nombre de la materia es incorrecto");
    }

    @Test
    public void testBorrarMateriaPorId() throws IOException {
        // Preparar archivo con datos
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilePath))) {
            writer.println("1,Matemática,1,1,100,");
        }

        Materia materia = materiaDao.borrarmateriaporid(1L);
        assertNotNull(materia, "La materia no debería ser nula");
        assertEquals("Matemática", materia.getNombre(), "El nombre de la materia es incorrecto");

        // Comprobar que el archivo se haya vaciado después de borrar
        try (BufferedReader reader = new BufferedReader(new FileReader(testFilePath))) {
            assertNull(reader.readLine(), "El archivo no debería tener contenido después de borrar la materia");
        }
    }
}
