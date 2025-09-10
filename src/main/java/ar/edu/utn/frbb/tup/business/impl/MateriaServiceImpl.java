package ar.edu.utn.frbb.tup.business.impl;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;
import ar.edu.utn.frbb.tup.persistence.ProfesorDaoMemoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class MateriaServiceImpl implements MateriaService {
    @Autowired
    private MateriaDaoMemoryImpl materiaDaoMemoryimp;
    @Autowired
    private ProfesorDaoMemoryImpl profesorDaoMem;

    @Override
    public Materia crearMateria(MateriaDto materiadto) throws ProfesorNoEncontradoException, MateriaYaExisteException {
        // Validar que el nombre no esté vacío
        if (materiadto.getNombre() == null || materiadto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío.");
        }

        // Validar que el nombre solo contenga letras y espacios
        if (!materiadto.getNombre().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            throw new IllegalArgumentException("El nombre de la materia solo puede contener letras y espacios.");
        }

        // Validar que el año sea positivo
        if (materiadto.getAnio() <= 0) {
            throw new IllegalArgumentException("El año debe ser un número positivo.");
        }
        if (materiadto.getAnio() > 5) {
            throw new IllegalArgumentException("El año de la materia no puede ser mayor a 5.");
        }
        // Validar que el cuatrimestre esté entre 1 y 4
        if (materiadto.getCuatrimestre() < 1 || materiadto.getCuatrimestre() > 2) {
            throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 4.");
        }

        // Validar que la lista de correlatividades no sea nula ni vacía
        if (materiadto.getCorrelatividades() == null || materiadto.getCorrelatividades().isEmpty()) {
            throw new IllegalArgumentException("La materia debe tener al menos una correlatividad.");
        }

        // Verificar que el profesor exista
        ProfesorDaoMemoryImpl profesorDaoMem = new ProfesorDaoMemoryImpl();
        Profesor profesor = profesorDaoMem.buscarProfesorporid(materiadto.getProfesorId());
        if (profesor == null) {
            throw new ProfesorNoEncontradoException("El id del Profesor no se encuentra en la BASE DE DATOS");
        }

        // Verificar si ya existe una materia con el mismo nombre y cuatrimestre
        List<Materia> todasLasMaterias = materiaDaoMemoryimp.buscarMaterias();
        for (Materia materia : todasLasMaterias) {
            if (materia.getNombre().equalsIgnoreCase(materiadto.getNombre()) &&
                    materia.getCuatrimestre() == materiadto.getCuatrimestre()) {
                throw new MateriaYaExisteException("Ya existe una materia con el mismo nombre y cuatrimestre.");
            }
        }

        // Crear la nueva materia
        Materia materia = new Materia(materiadto.getNombre(), materiadto.getAnio(), materiadto.getCuatrimestre(), materiadto.getProfesorId(), materiadto.getCorrelatividades());

        // Guardar la materia en el DAO
        materiaDaoMemoryimp.guardarMateria(materia);

        // Retornar la materia creada
        return materia;
    }

    @Override
    public List<Materia> buscarMateria() {
        List<Materia> lista_de_materias = materiaDaoMemoryimp.buscarMaterias();
        return lista_de_materias;
    }



    @Override
    public Materia buscarmateriaId(long id) {
        Materia materiaId=materiaDaoMemoryimp.buscarMateriaId(id);
        if(materiaId==null){
            throw new IllegalArgumentException("No se encontró la materia con el ID: " + id);
        }
        return materiaId;
    }

    @Override
    public Materia modificarMateria(long id, MateriaDto materiaModificada) throws MateriaNoEncontradaException, ProfesorNoEncontradoException, MateriaYaExisteException {

        // Buscar si existe la materia a través del id
        Materia materiaExistente = materiaDaoMemoryimp.buscarMateriaId(id);

        if (materiaExistente == null) {
            // Lanzar excepción personalizada si no se encuentra la materia
            throw new MateriaNoEncontradaException("No se encontró una materia con el ID proporcionado: " + id);
        }

        // Validar que el nombre no sea nulo ni vacío
        if (materiaModificada.getNombre() == null || materiaModificada.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío.");
        }

        // Validar que el nombre solo contenga letras y espacios
        if (!materiaModificada.getNombre().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            throw new IllegalArgumentException("El nombre de la materia solo puede contener letras y espacios.");
        }

        // Validar que el año sea positivo
        if (materiaModificada.getAnio() <= 0) {
            throw new IllegalArgumentException("El año debe ser un número positivo.");
        }
        if (materiaModificada.getAnio() > 5) {
            throw new IllegalArgumentException("El año de la materia no puede ser mayor a 5.");
        }

        // Validar que el cuatrimestre esté entre 1 y 2
        if (materiaModificada.getCuatrimestre() < 1 || materiaModificada.getCuatrimestre() > 2) {
            throw new IllegalArgumentException("El cuatrimestre debe estar entre 1 y 2.");
        }

        // Validar que la lista de correlatividades no sea nula ni vacía
        if (materiaModificada.getCorrelatividades() == null || materiaModificada.getCorrelatividades().isEmpty()) {
            throw new IllegalArgumentException("La materia debe tener al menos una correlatividad.");
        }

        // Verificar que el profesor exista
        ProfesorDaoMemoryImpl profesorDaoMem = new ProfesorDaoMemoryImpl();
        Profesor profesor = profesorDaoMem.buscarProfesorporid(materiaModificada.getProfesorId());
        if (profesor == null) {
            throw new ProfesorNoEncontradoException("El id del Profesor no se encuentra en la BASE DE DATOS");
        }

        // Verificar si ya existe otra materia con el mismo nombre y cuatrimestre
        List<Materia> todasLasMaterias = materiaDaoMemoryimp.buscarMaterias();
        for (Materia materia : todasLasMaterias) {
            if (materia.getId() != id && materia.getNombre().equalsIgnoreCase(materiaModificada.getNombre()) &&
                    materia.getCuatrimestre() == materiaModificada.getCuatrimestre()) {
                throw new MateriaYaExisteException("Ya existe otra materia con el mismo nombre y cuatrimestre.");
            }
        }

        // Actualizar los datos de la materia existente con los nuevos datos del DTO
        materiaExistente.setNombre(materiaModificada.getNombre());
        materiaExistente.setAnio(materiaModificada.getAnio());
        materiaExistente.setCuatrimestre(materiaModificada.getCuatrimestre());
        materiaExistente.setIdprofesor(materiaModificada.getProfesorId());
        materiaExistente.setCorrelatividades(materiaModificada.getCorrelatividades());

        // Guardar los cambios
        materiaDaoMemoryimp.modificarMateria(materiaExistente);

        // Retornar la materia modificada
        return materiaExistente;
    }

    @Override
    public Materia borrarmateriaId(long id) throws MateriaNoEncontradaException {
        Materia materiaExistente = materiaDaoMemoryimp.buscarMateriaId(id);

        // Validar si la materia existe
        if (materiaExistente == null) {
            throw new MateriaNoEncontradaException("La materia con ID " + id + " no existe en la base de datos.");
        }

        // Eliminar la materia
        materiaDaoMemoryimp.borrarmateriaporid(id);

        return materiaExistente;
    }


}
