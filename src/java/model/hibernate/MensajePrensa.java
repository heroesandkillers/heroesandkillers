package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "hak_mensajes_prensa")
public class MensajePrensa extends Mensajes implements Serializable {

    private Division division;

    @ManyToOne(cascade = CascadeType.ALL)
    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

}