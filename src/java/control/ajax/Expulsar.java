package control.ajax;

import model.DAO.*;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.hibernate.*;
import java.util.Date;
import org.hibernate.*;

public class Expulsar extends ActionSupport {

    private String response = "error";
    private long id;

    public String execute() throws Exception {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario usuario = usuarioDAO.loadUsuario(userId);

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        CriaturaAcademia criatura = criaturaDAO.loadCriaturaAcademia(id);

        if (criatura.getUsuario() == usuario) {
            Date date = new Date();
            int dia = (int) (date.getTime() / 86400000);
            Gson gson = new Gson();

            Promesas promesas = gson.fromJson(usuario.getAcademia(), Promesas.class);

            try {
                if (promesas.diaExpulsado < dia) {
                    criaturaDAO.delete(criatura);
                    promesas.diaExpulsado = dia;
                    usuario.setAcademia(gson.toJson(promesas));
                    usuarioDAO.update(usuario);

                    session.close();
                    response = "";
                    return SUCCESS;
                }
            } catch (Exception e) {
                promesas = new Promesas();
                criaturaDAO.delete(criatura);
                promesas.diaExpulsado = dia;
                usuario.setAcademia(gson.toJson(promesas));
                usuarioDAO.update(usuario);

                session.close();
                response = "";
                return SUCCESS;
            }
            session.close();
            response = "Hoy ya has expulsado a una criatura";
            return SUCCESS;
        }
        session.close();
        response = "La criatura recibida no es tuya";
        return SUCCESS;
    }

    public class Promesas {

        int diaExpulsado = 0;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
