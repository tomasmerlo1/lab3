package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.controller.validator.materiaValidator;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.MateriaYaExisteException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MateriaControllerTest {

    @InjectMocks
    private MateriaController materiaController;

    @Mock
    private MateriaService materiaService;

    @Mock
    private materiaValidator materiaValidator;

    private MateriaDto materiaDto;
    private Materia materia;
    private List<Materia> materias;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear la lista de correlatividades con identificadores de asignaturas
        List<Long> c = new ArrayList<>();
        c.add(1L);  // ID de la asignatura 1
        c.add(2L);  // ID de la asignatura 2

        // Crear y configurar MateriaDto
        materiaDto = new MateriaDto();
        materiaDto.setProfesorId(1);
        materiaDto.setCuatrimestre(2);
        materiaDto.setAnio(2);
        materiaDto.setCorrelatividades(c);  // Asignar la lista de correlatividades (IDs de asignaturas)

        // Crear y configurar el objeto Materia
        materia = new Materia();
        materia.setId(1);  // Se asigna un ID a la materia
        materia.setNombre("Matemáticas");  // Asignar el nombre de la materia
        materia.setAnio(2);
        materia.setIdprofesor(1);
        materia.setCuatrimestre(2);
        materia.setCorrelatividades(c);

        // Crear una lista de Materias
        materias = Arrays.asList(new Materia(), new Materia());
    }

    @Test
    void testCrearMateria() throws ProfesorNoEncontradoException, MateriaYaExisteException {
        // Configurar el comportamiento del validador
        doNothing().when(materiaValidator).materiaValidation(materiaDto);

        // Configurar el comportamiento del servicio
        when(materiaService.crearMateria(materiaDto)).thenReturn(materia);

        // Ejecutar el método del controlador
        Materia response = materiaController.crearMateria(materiaDto);

        // Verificaciones
        assertNotNull(response); // Verificar que la respuesta no sea nula
        assertEquals("Matemáticas", response.getNombre()); // Verificar que el nombre de la materia sea correcto

        // Verificar interacciones con el validador y el servicio
        verify(materiaValidator, times(1)).materiaValidation(materiaDto); // Verificar que el validador fue invocado una vez
        verify(materiaService, times(1)).crearMateria(materiaDto); // Verificar que el servicio fue invocado una vez
    }

    @Test
    void testBuscarMateriaId() {
        when(materiaService.buscarmateriaId(1)).thenReturn(materia);

        Materia response = materiaController.buscarMateriaId(1);

        assertNotNull(response);
        assertEquals("Matemáticas", response.getNombre());
    }

    @Test
    void testModificarMateria() throws MateriaNoEncontradaException, ProfesorNoEncontradoException, MateriaYaExisteException {
        when(materiaService.modificarMateria(1, materiaDto)).thenReturn(materia);

        Materia response = materiaController.modificarMateria(1, materiaDto);

        assertNotNull(response);
        assertEquals("Matemáticas", response.getNombre());
    }

    @Test
    void testEliminarMateria() throws MateriaNoEncontradaException {
        when(materiaService.borrarmateriaId(1)).thenReturn(materia);

        Materia response = materiaController.eliminarMateria(1);

        assertNotNull(response);
        assertEquals("Matemáticas", response.getNombre());
    }
}
