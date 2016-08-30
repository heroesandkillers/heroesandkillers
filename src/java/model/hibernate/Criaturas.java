package model.hibernate;

import javax.persistence.*;

@MappedSuperclass
public abstract class Criaturas {

    private Long id;
    private Usuario usuario;
    private String nombre;
    private String apodo;
    private String aspecto;
    private Integer sexo;
    private String raza;
    private String elemento;
    private String mutacion;
    private String clases;
    private Double fuerza;
    private Double magia;
    private Double agilidad;// velocidad + velocidad de ataque
    private Double reflejos;// probabilidad de golpear / esquivar
    private Double constitucion;
    private Double defensa;
    private Double reaccion;
    private Integer media;
    private Double xp;
    private Integer frescura = 100;
    private Integer moral = 70;
    private Long edad;
    private String evolucion;
    private String historial;
    private Integer bajas = 0;
    private Integer muertes = 0;
    private Integer batallas = 0;
    private Long precio;
    public Integer user;
    public Integer lvl;
    public String actitud;
    public String clase;
    public String x;
    public String y;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getAspecto() {
        return aspecto;
    }

    public void setAspecto(String aspecto) {
        this.aspecto = aspecto;
    }

    public Integer getSexo() {
        return sexo;
    }

    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getMutacion() {
        return mutacion;
    }

    public void setMutacion(String mutacion) {
        this.mutacion = mutacion;
    }

    public String getClases() {
        return clases;
    }

    public void setClases(String clases) {
        this.clases = clases;
    }

    public Double getFuerza() {
        return fuerza;
    }

    public void setFuerza(Double fuerza) {
        this.fuerza = fuerza;
    }

    public Double getMagia() {
        return magia;
    }

    public void setMagia(Double magia) {
        this.magia = magia;
    }

    @Column(precision=10, scale=2)
    public Double getAgilidad() {
        return agilidad;
    }

    public void setAgilidad(Double agilidad) {
        this.agilidad = agilidad;
    }

    public Double getReflejos() {
        return reflejos;
    }

    public void setReflejos(Double reflejos) {
        this.reflejos = reflejos;
    }

    public Double getConstitucion() {
        return constitucion;
    }

    public void setConstitucion(Double constitucion) {
        this.constitucion = constitucion;
    }

    public Double getDefensa() {
        return defensa;
    }

    public void setDefensa(Double defensa) {
        this.defensa = defensa;
    }

    public Double getReaccion() {
        return reaccion;
    }

    public void setReaccion(Double reaccion) {
        this.reaccion = reaccion;
    }

    public Integer getMedia() {
        return media;
    }

    public void setMedia(Integer media) {
        this.media = media;
    }

    public Double getXp() {
        return xp;
    }

    public void setXp(Double xp) {
        this.xp = xp;
    }

    public Integer getFrescura() {
        return frescura;
    }

    public void setFrescura(Integer frescura) {
        this.frescura = frescura;
    }

    public Integer getMoral() {
        return moral;
    }

    public void setMoral(Integer moral) {
        this.moral = moral;
    }

    public Long getEdad() {
        return edad;
    }

    public void setEdad(Long edad) {
        this.edad = edad;
    }

    @Column(name = "evolucion", columnDefinition = "TEXT")
    public String getEvolucion() {
        return evolucion;
    }

    public void setEvolucion(String evolucion) {
        this.evolucion = evolucion;
    }

    @Column(name = "historial", columnDefinition = "TEXT")
    public String getHistorial() {
        return historial;
    }

    public void setHistorial(String historial) {
        this.historial = historial;
    }

    public Integer getBajas() {
        return bajas;
    }

    public void setBajas(Integer bajas) {
        this.bajas = bajas;
    }

    public Integer getMuertes() {
        return muertes;
    }

    public void setMuertes(Integer muertes) {
        this.muertes = muertes;
    }

    public Integer getBatallas() {
        return batallas;
    }

    public void setBatallas(Integer batallas) {
        this.batallas = batallas;
    }

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }
}
