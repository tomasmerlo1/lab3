package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Asignatura;
import java.util.List;


public interface AsignaturaDao {

    public void guardarAsignatura(Asignatura asignatura);

    public List<Asignatura> buscarAsignaturas();

    public Asignatura buscarAsignaturaporId(long id);

    public Asignatura borrarAsignaturaporid(long id);

    public Asignatura modificarAsignatura(Asignatura asignatura);

    public int obtenerUltimoId();

    public Asignatura buscarAsignaturaporIdAsignaturaIdAlumno(long idAsignatura, long idAlumno);




}
