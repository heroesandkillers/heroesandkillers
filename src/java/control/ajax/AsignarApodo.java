package control.ajax;

import model.DAO.CriaturaDAO;
import com.opensymphony.xwork2.ActionContext;
import model.hibernate.*;
import org.hibernate.*;

public class AsignarApodo {

    private long id;
    private String apodo;

    public void execute() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();        
        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        Criatura criatura = criaturaDAO.loadCriatura(id);

        if (criatura.getUsuario().getId() == userId) {
            criatura.setApodo(apodo);
            criaturaDAO.update(criatura);
        }
        session.close();
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getApodo() {
        return apodo;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
