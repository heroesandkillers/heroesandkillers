package control.ajax;

import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.util.ArrayList;
import java.util.List;
import model.DAO.MensajeDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.Division;
import model.hibernate.HibernateUtil;
import model.hibernate.MensajeJuego;
import model.hibernate.MensajePrensa;
import model.hibernate.MensajeUsuario;
import model.hibernate.Mensajes;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class MensajeAction {

    public String mapaJSON = "";
    private long id;
    private String mensaje;
    private String asunto;
    private int receptorId;
    private int division;

    public void execute() throws Exception {
    }

    public void marcarMensajeLeidoUsuario() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        MensajeDAO mensajeDAO = new MensajeDAO(session);
        MensajeUsuario mens = mensajeDAO.loadMensajeUsuario(id);

        mens.setLeido(true);
        mensajeDAO.update(mens);

        session.close();
    }

    public void marcarMensajeLeidoJuego() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        MensajeDAO mensajeDAO = new MensajeDAO(session);
        MensajeJuego mens = mensajeDAO.loadMensajeJuego(id);

        mens.setLeido(true);
        mensajeDAO.update(mens);

        session.close();
    }

    public void enviarMensajePrensa() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Division div = usuarioDAO.getUsuario(userId).getDivision();

        MensajeDAO mensajeDAO = new MensajeDAO(session);
        mensajeDAO.setMensajePrensa(mensaje, userId, div);

        session.close();
    }

    public void enviarMensajeUsuario() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();

        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario emisor = usuarioDAO.getUsuario(userId);
        Usuario receptor = usuarioDAO.getUsuario(receptorId);

        MensajeDAO mensajeDAO = new MensajeDAO(session);
        mensajeDAO.setMensajeUsuario(mensaje, asunto, emisor, receptor);

        session.close();
    }

    public String getMensajesPrensa() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        MensajeDAO mensajeDAO = new MensajeDAO(session);
        List<MensajePrensa> mensajes = mensajeDAO.getMensajesPrensa(division);
        session.flush();

        for (MensajePrensa mens : mensajes) {
            try{
                mens.emisorName = mens.getEmisor().getPhpbb_user().getUsername();
            }catch(Exception e){
                mens.emisorName = "'deleted user'";
            }
            mens.setEmisor(null);
//            mens.setDivision(null);
        }

        if (!mensajes.isEmpty()) {
            Gson gson = new Gson();
            mapaJSON = gson.toJson(mensajes);
        }

        session.close();
        return SUCCESS;
    }

    public String getMensajesPrivados() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        MensajeDAO mensajeDAO = new MensajeDAO(session);
        List<MensajeUsuario> mensajesUsuario = mensajeDAO.getMensajesUsuario(id);
        List<MensajeJuego> mensajesJuego = mensajeDAO.getMensajesJuego(id);
        session.flush();

        for (MensajeUsuario mens : mensajesUsuario) {
            mens.emisorName = mens.getEmisor().getPhpbb_user().getUsername();
            mens.setEmisor(null);
            mens.receptorName = mens.getReceptor().getPhpbb_user().getUsername();
            mens.setReceptor(null);
        }
        for (MensajeJuego mens : mensajesJuego) {
            mens.setEmisor(null);
        }

        List<Mensajes> mensajes = new ArrayList<Mensajes>();
        mensajes.addAll(mensajesUsuario);
        mensajes.addAll(mensajesJuego);

        if (!mensajes.isEmpty()) {
            Gson gson = new Gson();
            mapaJSON = gson.toJson(mensajesUsuario);
        }

        session.close();
        return SUCCESS;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    
    public void setDivision(int division) {
        this.division = division;
    }

    public int getDivision() {
        return division;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setReceptorId(int receptorId) {
        this.receptorId = receptorId;
    }

    public int getReceptorId() {
        return receptorId;
    }
}
