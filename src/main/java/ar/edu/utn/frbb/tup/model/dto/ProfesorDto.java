package ar.edu.utn.frbb.tup.model.dto;

public class ProfesorDto {
    private String nombre;
    private String apellido;
    private String titulo;
    private long id;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getId(){
        return  id;
    }
    public void setId(long id){
        this.id=id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }




}
