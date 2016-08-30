package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "hak_criaturas_mazmorra")
@org.hibernate.annotations.Table(appliesTo = "hak_criaturas_mazmorra",
        indexes = {
    @Index(name = "fuerza"),
    @Index(name = "magia"),
    @Index(name = "agilidad"),
    @Index(name = "reflejos"),
    @Index(name = "constitucion"),
    @Index(name = "defensa"),
    @Index(name = "reaccion"),
    @Index(name = "exp")
})
public class CriaturaMazmorra extends Criaturas implements Serializable {

    @Index(name = "tiempoVenta")
    private int tiempoVenta;
    @Column(name = "pujas", columnDefinition = "TEXT")
    private String pujas;
    private Long pujaMax;    

    public Integer getTiempoVenta() {
        return tiempoVenta;
    }

    public void setTiempoVenta(Integer tiempoVenta) {
        this.tiempoVenta = tiempoVenta;
    }

    public String getPujas() {
        return pujas;
    }

    public void setPujas(String pujas) {
        this.pujas = pujas;
    }

    public Long getPujaMax() {
        return pujaMax;
    }

    public void setPujaMax(Long pujaMax) {
        this.pujaMax = pujaMax;
    }
}