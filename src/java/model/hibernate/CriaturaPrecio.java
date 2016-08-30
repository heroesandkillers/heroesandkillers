package model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "hak_criaturas_precio")
public class CriaturaPrecio implements Serializable {

    private long id;
    private Date tiempoVenta;
    private int sexo;
    private String raza;
    private String elemento;
    private String mutacion;
    private int fuerza;
    private int magia;
    private int agilidad;// velocidad + velocidad de ataque
    private int reflejos;// probabilidad de golpear / esquivar
    private int constitucion;
    private int defensa;
    private int reaccion;
    private double xp;
    private int edad;
    private long precio;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Index(name = "tiempoVenta")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTiempoVenta() {
        return tiempoVenta;
    }

    public void setTiempoVenta(Date tiempoVenta) {
        this.tiempoVenta = tiempoVenta;
    }

    @Index(name = "sexo")
    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    @Index(name = "raza")
    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    @Index(name = "elemento")
    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    @Index(name = "mutacion")
    public String getMutacion() {
        return mutacion;
    }

    public void setMutacion(String mutacion) {
        this.mutacion = mutacion;
    }

    @Index(name = "fuerza")
    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    @Index(name = "magia")
    public int getMagia() {
        return magia;
    }

    public void setMagia(int magia) {
        this.magia = magia;
    }

    @Index(name = "agilidad")
    public int getAgilidad() {
        return agilidad;
    }

    public void setAgilidad(int agilidad) {
        this.agilidad = agilidad;
    }

    @Index(name = "reflejos")
    public int getReflejos() {
        return reflejos;
    }

    public void setReflejos(int reflejos) {
        this.reflejos = reflejos;
    }

    @Index(name = "constitucion")
    public int getConstitucion() {
        return constitucion;
    }

    public void setConstitucion(int constitucion) {
        this.constitucion = constitucion;
    }

    @Index(name = "defensa")
    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    @Index(name = "reaccion")
    public int getReaccion() {
        return reaccion;
    }

    public void setReaccion(int reaccion) {
        this.reaccion = reaccion;
    }

    @Index(name = "xp")
    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    @Index(name = "edad")
    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Index(name = "precio")
    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }
}
