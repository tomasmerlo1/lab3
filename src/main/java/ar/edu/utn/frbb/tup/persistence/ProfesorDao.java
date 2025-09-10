package ar.edu.utn.frbb.tup.persistence;
import ar.edu.utn.frbb.tup.model.Profesor;
import java.util.List;

public interface ProfesorDao {
    public void guardarProfesor(Profesor profesor);

    public List<Profesor> buscarProfesores();

    public Profesor buscarProfesorporid(long id);


    public Profesor borrarProfesorporid(long id);


    public Profesor modificarProfesor(Profesor profesor);

    public int obtenerUltimoId();

}
