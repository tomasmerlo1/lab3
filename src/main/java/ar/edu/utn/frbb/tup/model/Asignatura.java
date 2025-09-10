package ar.edu.utn.frbb.tup.model;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDaoMemoryImpl;


public class Asignatura {


    private long id;
    private EstadoAsignatura estado;
    private Integer nota;
    private long idmateria;
    private long idalumno;

    public Asignatura() {
    }
    public Asignatura(EstadoAsignatura estado,Integer nota,long idalumno,long idmateria) {
        this.id = incrementarId();
        this.estado = estado;
        this.nota=nota;
        this.idalumno=idalumno;
        this.idmateria=idmateria;
    }

    public Asignatura(long id, EstadoAsignatura estado, Integer nota, long idalumno, long idmateria) {
        this.id = id;
        this.estado = estado;
        this.nota = nota;
        this.idmateria = idmateria;
        this.idalumno = idalumno;
    }

    public long getIdalumno() {
        return idalumno;
    }

    public void setIdalumno(long idalumno) {
        this.idalumno = idalumno;
    }

    public long getIdmateria() {
        return idmateria;
    }

    public void setIdmateria(long idmateria) {
        this.idmateria = idmateria;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNota() {
        return nota;
    }

    @Override
    public String toString() {
        return "Asignatura{" +
                "id=" + id +
                ", estado=" + estado +
                ", nota=" + nota +
                ", idmateria=" + idmateria +
                ", idalumno=" + idalumno +
                '}';
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public EstadoAsignatura getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoAsignatura estado) {
        this.estado = estado;
    }



    public void cursarAsignatura(){
        this.estado = EstadoAsignatura.CURSADA;
    }

    public void aprobarAsignatura(int nota) throws EstadoIncorrectoException {
        if (!this.estado.equals(EstadoAsignatura.CURSADA)) {
            throw new EstadoIncorrectoException("La materia debe estar cursada");
        }
        if (nota>=4) {
            this.estado = EstadoAsignatura.APROBADA;
            this.nota = nota;
        }
    }

    private int incrementarId()
    {
        AsignaturaDaoMemoryImpl admi = new AsignaturaDaoMemoryImpl();
        int ultimoId = admi.obtenerUltimoId();
        ultimoId++;
        return ultimoId;

    }

}
