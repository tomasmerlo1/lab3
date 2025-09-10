package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Carrera;
import java.util.List;

public interface CarreraDao {

    public void guardarCarrera(Carrera carrera);

    public List<Carrera> buscarCarrera();

    public Carrera buscarCarreraporId(long id);

    public Carrera borrarCarreraporid(long id);

    public Carrera modificarCarrera(Carrera carrera);

    public int obtenerUltimoId();

    public Carrera buscarCarrerasPorNombre(String nombre);

}
