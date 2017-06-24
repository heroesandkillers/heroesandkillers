package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;
//import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "hak_batallas")
public class Batalla implements Serializable {

    private Long id;
    private Integer calculos = 0;
    private Division division;
    private Usuario eqLoc;
    public Integer eqLocId;
    public String eqLocName;
    private Usuario eqVis;
    public Integer eqVisId;
    public String eqVisName;
    private String alinLoc;
    private String alinVis;
    private Integer fecha;
    private String tipo;
    private String resultado;
    private String res;
    private String res2;
    private String res3;
    private Integer den;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
//    @NotNull
    @Column(nullable = false)
    public Integer getCalculos() {
        return calculos;
    }

    public void setCalculos(Integer calculos) {
        this.calculos = calculos;
    }

    @Index(name = "division")
    @ManyToOne(cascade = CascadeType.ALL)
    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

//    @Index(name = "eqLoc")
    @ManyToOne(cascade = CascadeType.ALL)
    public Usuario getEqLoc() {
        return eqLoc;
    }

    public void setEqLoc(Usuario eqLoc) {
        this.eqLoc = eqLoc;
    }

//    @Index(name = "eqVis")
    @ManyToOne(cascade = CascadeType.ALL)
    public Usuario getEqVis() {
        return eqVis;
    }

    public void setEqVis(Usuario eqVis) {
        this.eqVis = eqVis;
    }

    @Column(name = "alinLoc", columnDefinition = "TEXT")
    public String getAlinLoc() {
        return alinLoc;
    }

    public void setAlinLoc(String alinLoc) {
        this.alinLoc = alinLoc;
    }

    @Column(name = "alinVis", columnDefinition = "TEXT")
    public String getAlinVis() {
        return alinVis;
    }

    public void setAlinVis(String alinVis) {
        this.alinVis = alinVis;
    }

    @Index(name = "fecha")
    public Integer getFecha() {
        return fecha;
    }

    public void setFecha(Integer fecha) {
        this.fecha = fecha;
    }

    @Index(name = "tipo")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    @Column(name = "res", columnDefinition = "LONGTEXT")
    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    @Column(name = "res2", columnDefinition = "LONGTEXT")
    public String getRes2() {
        return res2;
    }

    public void setRes2(String res2) {
        this.res2 = res2;
    }

    @Column(name = "res3", columnDefinition = "LONGTEXT")
    public String getRes3() {
        return res3;
    }

    public void setRes3(String res3) {
        this.res3 = res3;
    }

    public Integer getDen() {
        return den;
    }

    public void setDen(Integer den) {
        this.den = den;
    }
}