package model.hibernate;

import javax.persistence.*;
import org.hibernate.annotations.Index;

@MappedSuperclass
public abstract class Mensajes {

    private long id;
    public String tipo;
    private String mensaje;
    private Usuario emisor;
    public String emisorName;
    public String receptorName;
    private Integer fecha;    

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    @Index(name = "fecha")
    public Integer getFecha() {
        return fecha;
    }

    public void setFecha(Integer fecha) {
        this.fecha = fecha;
    }

}