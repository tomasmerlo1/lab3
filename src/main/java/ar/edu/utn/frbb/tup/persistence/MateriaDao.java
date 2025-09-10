package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Materia;
import java.util.List;


public interface MateriaDao {
    public void guardarMateria(Materia materia);

    public List<Materia> buscarMaterias();

    public List<Materia> buscarMateriasPorProfesorId(long idProfesor);

    public Materia buscarMateriaId(long id);

    public Materia borrarmateriaporid(long id);

    public Materia modificarMateria(Materia materia);

    public int obtenerUltimoId();


}
