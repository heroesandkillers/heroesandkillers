package model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "hak_traspasos")
public class Traspaso implements Serializable {

    private long id;
    private Date fecha;
    private Criatura criatura;
    private Usuario vendedor;
    private Usuario comprador;    
    private long valor;
    private long precio;
    private int edad;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Index(name = "fecha")
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Index(name = "criatura")
    @ManyToOne(cascade = CascadeType.ALL)
    public Criatura getCriatura() {
        return criatura;
    }

    public void setCriatura(Criatura criatura) {
        this.criatura = criatura;
    }

    @Index(name = "comprador")
    @ManyToOne(cascade = CascadeType.ALL)
    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    @Index(name = "vendedorId")
    @ManyToOne(cascade = CascadeType.ALL)
    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}