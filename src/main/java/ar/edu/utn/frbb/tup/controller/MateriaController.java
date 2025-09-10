package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.controller.validator.materiaValidator;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.MateriaYaExisteException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("materia")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;
    @Autowired
    private materiaValidator mateValidator;


    // Crear una nueva materia
    @PostMapping
    public Materia crearMateria(@RequestBody MateriaDto materiaDto) throws ProfesorNoEncontradoException, MateriaYaExisteException {
        mateValidator.materiaValidation(materiaDto);
        return materiaService.crearMateria(materiaDto);
    }

    // Obtener una materia por su ID
    @GetMapping("/{idMateria}")
    public Materia buscarMateriaId(@PathVariable("idMateria") Integer idMateria) {
        return materiaService.buscarmateriaId(idMateria);
    }

    // Modificar una materia existente
    @PutMapping("/{idMateria}")
    public Materia modificarMateria(@PathVariable("idMateria") Integer idMateria, @RequestBody MateriaDto materiaDto) throws MateriaNoEncontradaException, ProfesorNoEncontradoException, MateriaYaExisteException {
        mateValidator.materiaValidation(materiaDto);
        return materiaService.modificarMateria(idMateria, materiaDto);
    }

    // Eliminar una materia
    @DeleteMapping("/{idMateria}")
    public Materia eliminarMateria(@PathVariable("idMateria") Integer idMateria) throws MateriaNoEncontradaException {
        return materiaService.borrarmateriaId(idMateria);
    }
}
