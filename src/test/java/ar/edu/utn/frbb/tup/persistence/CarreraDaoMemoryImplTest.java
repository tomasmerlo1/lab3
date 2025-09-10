package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarreraDaoMemoryImplTest {

    @Autowired
    private CarreraDaoMemoryImpl carreraDao;

    private static final String TEST_CSV_FILE_PATH = "C:/tup/Academic-Management-System/src/main/java/ar/edu/utn/frbb/tup/persistence/dataCSV/carreraDATA.csv";

    @BeforeEach
    public void setUp() {
        carreraDao = new CarreraDaoMemoryImpl();
    }

    @Test
    public void testGuardarCarrera() {
        Carrera carrera = new Carrera(1, "Ingeniería en Sistemas");
        carreraDao.guardarCarrera(carrera);

        List<Carrera> carreras = carreraDao.buscarCarrera();
        assertNotNull(carreras);
        assertTrue(carreras.stream().anyMatch(c -> c.getId() == carrera.getId() && c.getNombre().equals(carrera.getNombre())));
    }





    @Test
    public void testBuscarCarreraPorIdNotFound() {
        Carrera foundCarrera = carreraDao.buscarCarreraporId(99);
        assertNull(foundCarrera);
    }

    @Test
    public void testBorrarCarreraPorId() {
        Carrera carrera = new Carrera(4, "Ingeniería Mecánica");
        carreraDao.guardarCarrera(carrera);

        Carrera deletedCarrera = carreraDao.borrarCarreraporid(4);

        assertNotNull(deletedCarrera);
        assertEquals("Ingeniería Mecánica", deletedCarrera.getNombre());

        assertNull(carreraDao.buscarCarreraporId(4));
    }

    @Test
    public void testModificarCarrera() {
        Carrera carrera = new Carrera(5, "Ingeniería Eléctrica");
        carreraDao.guardarCarrera(carrera);

        Carrera carreraModificada = new Carrera(5, "Ingeniería Electrónica");
        Carrera updatedCarrera = carreraDao.modificarCarrera(carreraModificada);

        assertNotNull(updatedCarrera);
        assertEquals("Ingeniería Electrónica", updatedCarrera.getNombre());
    }

    @Test
    public void testModificarCarreraNotFound() {
        Carrera carreraInexistente = new Carrera(999, "Carrera Inexistente");

        // Se espera que modifique una carrera que no existe, debe lanzar una excepción
        assertThrows(ResponseStatusException.class, () -> carreraDao.modificarCarrera(carreraInexistente));
    }

    @Test
    public void testObtenerUltimoId() {
        Carrera carrera1 = new Carrera(6, "Arquitectura");
        Carrera carrera2 = new Carrera(7, "Diseño Gráfico");

        carreraDao.guardarCarrera(carrera1);
        carreraDao.guardarCarrera(carrera2);

        int ultimoId = carreraDao.obtenerUltimoId();

        assertEquals(7, ultimoId);
    }
}
