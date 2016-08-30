package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.mapping.UniqueKey;

@Entity
@Table(name = "hak_tiempos",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nombreId"})})
public class Tiempo implements Serializable {

    private int id;
    private String nombreId;
    private int dia;
    private int editorId;
    private String estado = "inactivo";

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreId() {
        return nombreId;
    }

    public void setNombreId(String nombreId) {
        this.nombreId = nombreId;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getEditorId() {
        return editorId;
    }

    public void setEditorId(int editorId) {
        this.editorId = editorId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
