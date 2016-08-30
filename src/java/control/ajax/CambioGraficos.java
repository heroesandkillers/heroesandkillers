package control.ajax;

import model.DAO.UsuarioDAO;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;
import model.hibernate.Graficos;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class CambioGraficos extends ActionSupport {

    private boolean menu;

    public String execute() throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);

        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Usuario usuario = usuarioDAO.loadUsuario(userId);

        Gson gson = new Gson();
        Graficos graficos = gson.fromJson(usuario.getGraficos(), Graficos.class);

        if (graficos == null) {
            graficos = new Graficos();
        }

        graficos.setMenu(menu);
        usuario.setGraficos(gson.toJson(graficos));

        usuarioDAO.update(usuario);

        session.close();
        return SUCCESS;
    }

    public boolean getMenu() {
        return menu;
    }

    public void setMenu(boolean menu) {
        this.menu = menu;
    }
}