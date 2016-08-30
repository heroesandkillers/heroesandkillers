package control.ajax;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.hibernate.*;
import model.DAO.*;
import model.DAO.ConstruccionesDAO.Cons;
import org.hibernate.Session;


public class Ampliar extends ActionSupport {

    private String edificio;
    private String response = "error";

    public String execute() throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);

        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");

        Usuario usuario = usuarioDAO.loadUsuario(userId);
        long oro = usuario.getOro();

        Gson gson = new Gson();
        ConstruccionesDAO consDAO = new ConstruccionesDAO(session);
        Cons cons = gson.fromJson(usuario.getCons(), Cons.class);

        long precio = 0;

        if (edificio.equals("fort")) {
            precio = cons.fortPrecio;
            if (oro >= precio) {
                cons.fort++;
                cons.fortPrecio = consDAO.getPrecioTorre(cons.fort);
            }

        } else if (edificio.equals("acad")) {
            precio = cons.acadPrecio;
            if (oro >= precio) {
                cons.acad++;
                cons.acadPrecio = consDAO.getPrecioTorre(cons.acad);
            }

        } else if (edificio.equals("tabe")) {
            precio = cons.tabePrecio;
            if (oro >= precio) {
                cons.tabe++;
                cons.tabePrecio = consDAO.getPrecioTorre(cons.tabe);
            }
            
        } else if (edificio.equals("torr")) {
            precio = cons.torrPrecio;
            if (oro >= precio) {
                cons.torr++;
                cons.torrPrecio = consDAO.getPrecioTorre(cons.torr);
            }
            
        } else if (edificio.equals("mura")) {
            precio = cons.muraPrecio;
            if (oro >= precio) {
                cons.mura++;
                cons.muraPrecio = consDAO.getPrecioTorre(cons.mura);
            }
            
        } else if (edificio.equals("ento")) {
            precio = cons.entoPrecio;
            if (oro >= precio) {
                cons.ento++;
                cons.entoPrecio = consDAO.getPrecioTorre(cons.ento);
            }
            
        } else {
            session.close();
            return SUCCESS;
        }

        usuario.setOro(oro - precio);
        String parse = gson.toJson(cons);
        usuario.setCons(parse);

        usuarioDAO.update(usuario);
        session.close();

        response = parse;
        return SUCCESS;

    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}