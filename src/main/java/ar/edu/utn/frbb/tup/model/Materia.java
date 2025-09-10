package ar.edu.utn.frbb.tup.model;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Materia implements Comparable<Materia> {


    private long id;
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private long idprofesor;
    private List<Long> correlatividades;


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    public long getId() {
        return id;
    }

    public long getIdprofesor() {
        return idprofesor;
    }

    public List<Long> getCorrelatividades() {
        return correlatividades;
    }

    public Materia(){

    }


    public Materia(Long id, String nombre, int anio, int cuatrimestre, long idprofesor,  List<Long> correlatividades) {
        this.id=id;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.nombre = nombre;
        this.idprofesor = idprofesor;
        this.correlatividades = correlatividades;
    }

    public Materia(String nombre, int anio, int cuatrimestre, long idprofesor,  List<Long> correlatividades) {


        this.id=incrementarId();
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.nombre = nombre;
        this.idprofesor = idprofesor;
        this.correlatividades = correlatividades;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCorrelatividades(List<Long> correlatividades) {
        this.correlatividades = correlatividades;
    }

    public Materia(String nombre, int anio, int cuatrimestre, long idprofesor) {


        this.id=incrementarId();
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.nombre = nombre;
        this.idprofesor = idprofesor;
        correlatividades = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Materia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", anio=" + anio +
                ", cuatrimestre=" + cuatrimestre +
                ", idprofesor=" + idprofesor +
                ", correlatividades=" + correlatividades +
                '}';
    }

    public String getNombre() {
        return nombre;
    }
    public void setMateriaId(int materiaId) {
        this.id = materiaId;
    }

    public void setIdprofesor(long idprofesor) {
        this.idprofesor = idprofesor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, anio, cuatrimestre, idprofesor, correlatividades);

    }

    private int incrementarId()
    {
        MateriaDaoMemoryImpl admi = new MateriaDaoMemoryImpl();
        int ultimoId = admi.obtenerUltimoId();
        ultimoId++;
        return ultimoId;

    }

    @Override
    public int compareTo(Materia otraMateria) {
        return this.getNombre().compareTo(otraMateria.getNombre());
    }
}
