package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.controller.validator.alumnoValidator;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;

import ar.edu.utn.frbb.tup.model.exception.AlumnoNoEncontradoException;
import ar.edu.utn.frbb.tup.model.exception.AlumnoYaExisteException;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;
    @Autowired
    private AsignaturaService asignaturaService;
    @Autowired
    private  alumnoValidator alumValidator;


    @PostMapping
    public ResponseEntity<Alumno> crearAlumno(@RequestBody AlumnoDto alumnoDto) throws AlumnoNoEncontradoException, AlumnoYaExisteException {
        alumValidator.validarAlumno(alumnoDto);
        Alumno nuevoAlumno;
        try {
            nuevoAlumno = alumnoService.crearAlumno(alumnoDto);
        } catch (ar.edu.utn.frbb.tup.model.exception.AlumnoYaExisteException e) {
            throw new AlumnoYaExisteException("El alumno con el DNI " + alumnoDto.getDni() + " ya existe.");
        }

        return new ResponseEntity<>(nuevoAlumno, HttpStatus.CREATED);
    }

    @GetMapping("/{idAlumno}")
    public Alumno buscarAlumnoId(@PathVariable("idAlumno") Integer idAlumno) throws AlumnoNoEncontradoException {
        return alumnoService.buscarAlumnoId(idAlumno);
    }
    // Modificar un alumno existente
    @PutMapping("/{idAlumno}")
    public Alumno modificarAlumno(@PathVariable("idAlumno") Integer idAlumno, @RequestBody AlumnoDto alumnoDto) throws AlumnoYaExisteException {
        return alumnoService.modificarAlumno(idAlumno, alumnoDto);
    }

    @PutMapping("/{idAlumno}/asignatura/{idAsignatura}")
    public Asignatura modificarEstadoAsignatura(@PathVariable("idAlumno") Integer idAlumno,@PathVariable("idAsignatura") Integer idAsignatura) throws AsignaturaNoEncontradaException, EstadoIncorrectoException {
        return asignaturaService.modificarEstadoAsignatura(idAlumno, idAsignatura);
    }

    // Eliminar un alumno
    @DeleteMapping("/{idAlumno}")
    public Alumno eliminarAlumno(@PathVariable("idAlumno") Integer idAlumno) throws AlumnoNoEncontradoException {
        return alumnoService.borraralumnoId(idAlumno);
    }


}
