package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "hak_trofeos")
public class Trofeo implements Serializable {

    private Long id;
    private Usuario usuario;
    private String tipo;
    private Integer rango;
    private Integer fecha;
    private String descripcion;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Index(name = "tipo")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Index(name = "rango")
    public Integer getRango() {
        return rango;
    }

    public void setRango(Integer rango) {
        this.rango = rango;
    }

    @Index(name = "fecha")
    public Integer getFecha() {
        return fecha;
    }

    public void setFecha(Integer fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}