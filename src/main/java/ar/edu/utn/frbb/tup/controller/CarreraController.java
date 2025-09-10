package ar.edu.utn.frbb.tup.controller;
import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.controller.validator.carreraValidator;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraYaExisteEstaException;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ar.edu.utn.frbb.tup.business.ProfesorService;

@RestController
@RequestMapping("carrera")
public class CarreraController {

    @Autowired
    private ProfesorService profesorService;
    @Autowired
    private CarreraService carreraService;
    @Autowired
    private carreraValidator carreValidator;


    // Crear una nueva carrera
    @PostMapping
    public ResponseEntity<Carrera> crearCarrera(@RequestBody CarreraDto carreraDto) throws CarreraYaExisteEstaException {
        carreValidator.carreraValidation(carreraDto);
        Carrera nuevaCarrera = carreraService.crearCarrera(carreraDto);
        return new ResponseEntity<>(nuevaCarrera, HttpStatus.CREATED);
    }

    // Obtener una carrera por su ID
    @GetMapping("/{idCarrera}")
    public Carrera buscarCarreraId(@PathVariable("idCarrera") Integer idCarrera) {
        return carreraService.buscarCarreraId(idCarrera);
    }

    // Modificar una carrera existente
    @PutMapping("/{idCarrera}")
    public Carrera modificarCarrera(@PathVariable("idCarrera") Integer idCarrera, @RequestBody CarreraDto carreraDto) throws CarreraYaExisteEstaException {
        return carreraService.modificarCarrera(idCarrera, carreraDto);
    }

    // Eliminar una carrera
    /*@DeleteMapping("/{idCarrera}")
    public Carrera eliminarCarrera(@PathVariable("idCarrera") Integer idCarrera) {
        return carreraService.borrarCarreraporid(idCarrera);
    }*/

    @DeleteMapping("/{idProfesor}")
    public Profesor eliminarProfesor(@PathVariable("idProfesor") Integer idProfesor) throws ProfesorNoEncontradoException {
        return profesorService.borrarProfesorporid(idProfesor);
    }



    /*
    @DeleteMapping("/{idcarrera}")
    public ResponseEntity<?> eliminarCarrera(@PathVariable("id") long id) { // usa ("id") para mayor claridad
        Carrera carreraEliminada = carreraService.borrarCarreraporid(id);
        if (carreraEliminada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe carrera con el ID proporcionado: " + id);
        }
        return ResponseEntity.ok("Carrera eliminada exitosamente!");
    }*/
}

