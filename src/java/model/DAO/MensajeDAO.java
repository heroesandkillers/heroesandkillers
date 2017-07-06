package model.DAO;

import java.util.Date;
import java.util.List;
import model.hibernate.Division;
import model.hibernate.MensajeJuego;
import model.hibernate.MensajeUsuario;
import model.hibernate.MensajePrensa;
import model.hibernate.Usuario;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MensajeDAO {

    private Session session;

    public MensajeDAO(Session session) {
        this.session = session;
    }

    public void update(Object object) {
        Transaction t = session.beginTransaction();
        session.update(object);
        t.commit();
    }

    public void save(Object object) {
        Transaction t = session.beginTransaction();
        session.save(object);
        t.commit();
    }

    public void setMensajeUsuario(String mensaje, String asunto, Usuario emisor, Usuario receptor) {

        MensajeUsuario nuevo = new MensajeUsuario();

        nuevo.setMensaje(mensaje);
        nuevo.setAsunto(asunto);
        nuevo.setEmisor(emisor);
        nuevo.setReceptor(receptor);

        Transaction t = session.beginTransaction();
        session.save(nuevo);
        t.commit();
    }

    public void setMensajePrensa(String mensaje, int emisorId, Division division) {
        int milis = (int) (new Date().getTime() / 1000);
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        MensajePrensa nuevo = new MensajePrensa();
        nuevo.setMensaje(mensaje);
        nuevo.setEmisor(usuarioDAO.loadUsuario(emisorId));
        nuevo.setDivision(division);
        nuevo.setFecha(milis);

        save(nuevo);
    }

    public MensajeUsuario loadMensajeUsuario(long id) {
        Transaction t = session.beginTransaction();
        MensajeUsuario mensaje = (MensajeUsuario) session.load(MensajeUsuario.class, id);
        t.commit();
        return mensaje;
    }

    public MensajeJuego loadMensajeJuego(long id) {
        Transaction t = session.beginTransaction();
        MensajeJuego mensaje = (MensajeJuego) session.load(MensajeJuego.class, id);
        t.commit();
        return mensaje;
    }

    public List<MensajePrensa> getMensajesPrensa(int division) {
        String peticion = "FROM MensajePrensa WHERE division = :division ";
        Query query = session.createQuery(peticion);
        query.setParameter("division", division);
        List<MensajePrensa> mensajes = query.list();
        return mensajes;
    }

    public List<MensajeUsuario> getMensajesUsuario(long id) {
        String peticion = "FROM MensajeUsuario WHERE emisor = :id OR receptor = :id";
        Query query = session.createQuery(peticion);
        query.setParameter("id", id);
        List<MensajeUsuario> mensajes = query.list();

        return mensajes;
    }

    public List<MensajeJuego> getMensajesJuego(long id) {
        String peticion = "FROM MensajeUsuario WHERE emisor = :id OR receptor = :id";
        Query query = session.createQuery(peticion);
        query.setParameter("id", id);
        List<MensajeJuego> mensajes = query.list();

        return mensajes;
    }

    public int getMensajesNuevos(long id) {
        String peticion = "SELECT count(*) FROM MensajeUsuario WHERE receptor = :id AND leido = false";
        Query query = session.createQuery(peticion);
        query.setParameter("id", id);
        return ((Number) query.uniqueResult()).intValue();
    }

    public void setPremioBatalla(Usuario ganador, String perdedor, int premio) {
        MensajeJuego mensaje = new MensajeJuego();
        mensaje.setReceptor(ganador);
        mensaje.setAsunto("Batalla contra " + perdedor);
        mensaje.setMensaje("Has saqueado un tesoro de " + premio + " monedas.");
        save(mensaje);
    }
}
