package ar.edu.utn.frbb.tup.model;
import ar.edu.utn.frbb.tup.persistence.ProfesorDaoMemoryImpl;

public class Profesor {



    private long id;
    private String nombre;
    private String apellido;
    private String titulo;


    public Profesor(String nombre, String apellido, String titulo) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.titulo = titulo;
        this.id=incrementarId();
    }

    public Profesor(long id, String nombre, String apellido, String titulo) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.titulo = titulo;
        this.id=id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getTitulo() {
        return titulo;
    }
    public long getId(){
        return id;
    }

    private int incrementarId()
    {
        ProfesorDaoMemoryImpl admi = new ProfesorDaoMemoryImpl();
        int ultimoId = admi.obtenerUltimoId();
        ultimoId++;
        return ultimoId;

    }

    @Override
    public String toString() {
        return "Profesor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}
