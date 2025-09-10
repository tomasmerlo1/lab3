package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.business.impl.AsignaturaServiceImpl;
import ar.edu.utn.frbb.tup.business.impl.MateriaServiceImpl;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaYaExisteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.model.exception.NoseEncontroAsignatura;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AsignaturaServiceImplTest {

    @InjectMocks
    private AsignaturaServiceImpl asignaturaService;

    @Mock
    private AsignaturaDaoMemoryImpl asignaturaDaoMemoryImpl;
    @Mock
    private MateriaServiceImpl materiaServiceimpl;
    @Mock
    private AlumnoDaoMemoryImpl alumnoDaoMemory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Alumno alumno = new Alumno(1, "Diego", "Maradona", 23232424);
        when(alumnoDaoMemory.buscarAlumnoporid(1)).thenReturn(alumno);
        Materia materia =new Materia("Historia", 1,2,1);
        when(materiaServiceimpl.buscarmateriaId(1)).thenReturn(materia);
    }

    @Test
    public void testCrearAsignaturaConDatosValidos() throws AsignaturaYaExisteException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setNota(8);
        asignaturaDto.setIdalumno(1);
        asignaturaDto.setIdmateria(1);
        asignaturaDto.setEstado(EstadoAsignatura.NO_CURSADA);

        Asignatura asignatura = new Asignatura(
                asignaturaDto.getEstado(),
                asignaturaDto.getNota(),
                asignaturaDto.getIdalumno(),
                asignaturaDto.getIdmateria()
        );

        Asignatura resultado = asignaturaService.crearAsignatura(asignaturaDto);

        assertNotNull(resultado);
        assertEquals(asignaturaDto.getNota(), Optional.of(resultado.getNota()).orElse(null));
        assertEquals(asignaturaDto.getIdalumno(), resultado.getIdalumno());
        assertEquals(asignaturaDto.getIdmateria(), resultado.getIdmateria());
        verify(asignaturaDaoMemoryImpl, times(1)).guardarAsignatura(any(Asignatura.class));
    }

    @Test
    public void testCrearAsignaturaConNotaNula() {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setIdalumno(1);
        asignaturaDto.setIdmateria(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                asignaturaService.crearAsignatura(asignaturaDto)
        );
        assertEquals("La nota no puede ser nula", exception.getMessage());
    }

    @Test
    public void testCrearAsignaturaNotaNula() throws AsignaturaYaExisteException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setEstado(EstadoAsignatura.CURSADA);
        asignaturaDto.setNota(null); // Nota nula
        asignaturaDto.setIdalumno(1);
        asignaturaDto.setIdmateria(1);

        assertThrows(IllegalArgumentException.class, () -> asignaturaService.crearAsignatura(asignaturaDto));
    }

    @Test
    public void testBuscarAsignaturaId() {
        long id = 2;
        Asignatura asignatura = new Asignatura();
        asignatura.setNota(8);

        when(asignaturaDaoMemoryImpl.buscarAsignaturaporId(id)).thenReturn(asignatura);

        Asignatura resultado = asignaturaService.buscarAsignaturaId(id);

        assertNotNull(resultado);
        assertEquals(asignatura.getNota(), resultado.getNota());
        verify(asignaturaDaoMemoryImpl, times(2)).buscarAsignaturaporId(id);
    }

    @Test
    public void testBorrarAsignaturaporId() {
        long id = 1;
        Asignatura asignatura = new Asignatura(EstadoAsignatura.APROBADA, 7, 1, 1);

        when(asignaturaDaoMemoryImpl.borrarAsignaturaporid(id)).thenReturn(asignatura);

        Asignatura resultado = asignaturaService.borrarAsignaturaporid(id);

        assertNotNull(resultado);
        assertEquals(asignatura, resultado);
        verify(asignaturaDaoMemoryImpl, times(2)).borrarAsignaturaporid(id);
    }

    @Test
    public void testCrearAsignaturaIdMateriaInvalido() throws AsignaturaYaExisteException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setEstado(EstadoAsignatura.CURSADA);
        asignaturaDto.setNota(8);
        asignaturaDto.setIdalumno(1);
        asignaturaDto.setIdmateria(-1); // ID de la materia inválido

        assertThrows(IllegalArgumentException.class, () -> asignaturaService.crearAsignatura(asignaturaDto));
    }

    @Test
    public void testCrearAsignaturaIdAlumnoInvalido() throws AsignaturaYaExisteException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setEstado(EstadoAsignatura.CURSADA);
        asignaturaDto.setNota(8);
        asignaturaDto.setIdalumno(-1); // ID del alumno inválido
        asignaturaDto.setIdmateria(1);

        assertThrows(IllegalArgumentException.class, () -> asignaturaService.crearAsignatura(asignaturaDto));
    }

    @Test
    public void testModificarEstadoAsignatura() throws AsignaturaNoEncontradaException, EstadoIncorrectoException {
        long idAlumno = 1;
        long idAsignatura = 1;
        Asignatura asignatura = new Asignatura(EstadoAsignatura.CURSADA, 7, idAlumno, idAsignatura);

        when(asignaturaDaoMemoryImpl.buscarAsignaturaporIdAsignaturaIdAlumno(idAsignatura, idAlumno)).thenReturn(asignatura);

        Asignatura resultado = asignaturaService.modificarEstadoAsignatura(idAlumno, idAsignatura);

        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.APROBADA, resultado.getEstado());
        verify(asignaturaDaoMemoryImpl, times(1)).modificarAsignatura(asignatura);
    }

    @Test
    public void testBuscarAsignaturaIdNoExistente() {
        long id = 999; // ID de asignatura no existente

        assertThrows(IllegalArgumentException.class, () -> asignaturaService.buscarAsignaturaId(id));
    }

    @Test
    public void testModificarAsignaturaNoExistente() {
        long id = 999; // ID de asignatura no existente
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setEstado(EstadoAsignatura.APROBADA);
        asignaturaDto.setNota(9);
        asignaturaDto.setIdalumno(1);
        asignaturaDto.setIdmateria(1);

        assertThrows(NoseEncontroAsignatura.class, () -> asignaturaService.modificarAsignatura(id, asignaturaDto));
    }

    @Test
    public void testBorrarAsignaturaNoExistente() {
        long id = 999; // ID de asignatura no existente

        assertThrows(NoseEncontroAsignatura.class, () -> asignaturaService.borrarAsignaturaporid(id));
    }

    @Test
    public void testModificarEstadoAsignaturaExistente() throws AsignaturaNoEncontradaException, EstadoIncorrectoException {
        long idAlumno = 1;
        long idAsignatura = 1;
        Asignatura asignatura = new Asignatura(EstadoAsignatura.CURSADA, 7, idAlumno, idAsignatura);

        when(asignaturaDaoMemoryImpl.buscarAsignaturaporIdAsignaturaIdAlumno(idAsignatura, idAlumno)).thenReturn(asignatura);

        Asignatura resultado = asignaturaService.modificarEstadoAsignatura(idAlumno, idAsignatura);

        assertNotNull(resultado);
        assertEquals(EstadoAsignatura.APROBADA, resultado.getEstado());
        verify(asignaturaDaoMemoryImpl, times(1)).modificarAsignatura(asignatura);
    }

    @Test
    public void testCrearAsignaturaRelacionInvalida() throws IllegalArgumentException, AsignaturaYaExisteException {
        AsignaturaDto asignaturaDto = new AsignaturaDto();
        asignaturaDto.setEstado(EstadoAsignatura.CURSADA);
        asignaturaDto.setNota(8);
        asignaturaDto.setIdalumno(999); // Alumno no existente
        asignaturaDto.setIdmateria(1);

        assertThrows(IllegalStateException.class, () -> asignaturaService.crearAsignatura(asignaturaDto));
    }
}
