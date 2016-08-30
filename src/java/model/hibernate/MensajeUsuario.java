package model.hibernate;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "hak_mensajes_usuario")
public class MensajeUsuario extends Mensajes implements Serializable {

    private Usuario receptor;
    private String asunto;
    private boolean leido = false;

    @ManyToOne(cascade = CascadeType.ALL)
    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public boolean getLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}