package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "hak_criaturas_academia")
public class CriaturaAcademia extends Criaturas implements Serializable {

    private String destapes;

    public String getDestapes() {
        return destapes;
    }

    public void setDestapes(String destapes) {
        this.destapes = destapes;
    }
}