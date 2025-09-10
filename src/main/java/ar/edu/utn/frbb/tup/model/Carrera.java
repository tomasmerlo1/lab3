package ar.edu.utn.frbb.tup.model;
import ar.edu.utn.frbb.tup.persistence.CarreraDaoMemoryImpl;


public class Carrera {
    private long id;
    private String nombre;


    public Carrera(String nombre) {
        this.id = incrementarId();
        this.nombre = nombre;
    }

    public Carrera() {
    }

    public Carrera(long id,String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Carrera{" +
                ", id=" + id +
                "nombre='" + nombre +
                '}';
    }

    private int incrementarId()
    {
        CarreraDaoMemoryImpl admi = new CarreraDaoMemoryImpl();
        int ultimoId = admi.obtenerUltimoId();
        ultimoId++;
        return ultimoId;

    }


}
