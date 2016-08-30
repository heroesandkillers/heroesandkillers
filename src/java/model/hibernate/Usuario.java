package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "hak_usuarios")
public class Usuario implements Serializable {

    private Integer id;
    private Phpbb_user phpbb_user;
    private Long oro;
    private String entreno;
    private String alin;
    private String alinAcad;
    private String fichajes;
    private String economia;
    private String estad;
    private String puntos;
    private Integer posicion;
    private Division division;    
    private Integer activo;
    private String cons;
    private String graficos;
    private String pujas;
    private String academia;
    //
    public String username;
    public Integer div;
    public Integer mensajes;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Phpbb_user getPhpbb_user() {
        return phpbb_user;
    }

    public void setPhpbb_user(Phpbb_user phpbb_user) {
        this.phpbb_user = phpbb_user;
    }

    public Long getOro() {
        return oro;
    }

    public void setOro(Long oro) {
        this.oro = oro;
    }

    public String getEntreno() {
        return entreno;
    }

    public void setEntreno(String entreno) {
        this.entreno = entreno;
    }

    @Column(name = "alin", columnDefinition = "TEXT")
    public String getAlin() {
        return alin;
    }

    public void setAlin(String alin) {
        this.alin = alin;
    }

    @Column(name = "alinAcad", columnDefinition = "TEXT")
    public String getAlinAcad() {
        return alinAcad;
    }

    public void setAlinAcad(String alinAcad) {
        this.alinAcad = alinAcad;
    }

    @Column(name = "fichajes", columnDefinition = "TEXT")
    public String getFichajes() {
        return fichajes;
    }

    public void setFichajes(String fichajes) {
        this.fichajes = fichajes;
    }

    @Column(name = "economia", columnDefinition = "TEXT")
    public String getEconomia() {
        return economia;
    }

    public void setEconomia(String economia) {
        this.economia = economia;
    }

    @Column(name = "estadisticas", columnDefinition = "TEXT")
    public String getEstad() {
        return estad;
    }

    public void setEstad(String estad) {
        this.estad = estad;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    @Index(name = "posicion")
    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    @Index(name = "activo")
    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public String getCons() {
        return cons;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public String getGraficos() {
        return graficos;
    }

    public void setGraficos(String graficos) {
        this.graficos = graficos;
    }

    public String getPujas() {
        return pujas;
    }

    public void setPujas(String pujas) {
        this.pujas = pujas;
    }

    public String getAcademia() {
        return academia;
    }

    public void setAcademia(String academia) {
        this.academia = academia;
    }

}
