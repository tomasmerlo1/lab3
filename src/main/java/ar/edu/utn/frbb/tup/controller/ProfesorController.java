package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.controller.validator.profesorValidator;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorYaExisteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;
    @Autowired
    private profesorValidator profesorValidator;


    // Crear un nuevo profesor
    @PostMapping
    public ResponseEntity<Profesor> crearProfesor(@RequestBody ProfesorDto profesorDto) throws ProfesorYaExisteException {

        profesorValidator.profesorValidation(profesorDto);
        Profesor nuevoProfesor = profesorService.crearProfesor(profesorDto);
        return new ResponseEntity<>(nuevoProfesor, HttpStatus.CREATED);
    }


    @GetMapping("/{idProfesor}")
    public Profesor buscarProfesorPorId(@PathVariable("idProfesor") Integer idProfesor) {
        return profesorService.buscaProfesorporid(idProfesor);
    }

    @GetMapping("/materias/{idProfesor}")
    public List<Materia> buscarMateriasPorProefesorId(@PathVariable("idProfesor") Integer idProfesor) throws ProfesorNoEncontradoException, MateriaNoEncontradaException {
        return profesorService.buscarMateriasPorProfesorId(idProfesor);
    }

    // Modificar un profesor existente
    @PutMapping("/{idProfesor}")
    public Profesor modificarProfesor(@PathVariable("idProfesor") Integer idProfesor, @RequestBody ProfesorDto profesorDto) throws ProfesorNoEncontradoException {
       profesorValidator.profesorValidation(profesorDto);
        return  profesorService.modificarProfesor(idProfesor, profesorDto);
    }

    @DeleteMapping("/{idProfesor}")
    public ResponseEntity<Void> borrarProfesor(@PathVariable("idProfesor") Long idProfesor) throws ProfesorNoEncontradoException
    {
        profesorService.borrarProfesorporid(idProfesor);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

