package ar.edu.utn.frbb.tup.model.dto;

public class CarreraDto {
    private long id;
    private String nombre;


    public CarreraDto(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public CarreraDto() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
