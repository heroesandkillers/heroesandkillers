package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.DAO.BatallaDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.HibernateUtil;
import org.hibernate.Session;

public class GetBatallasDivision extends ActionSupport {

    public String mapaJSON = "error";

    @Override
    public String execute() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        int division = usuarioDAO.getUsuario(userId).getDivision().getDivision();
        
        BatallaDAO batallaDAO = new BatallaDAO(session);

        Gson gson = new Gson();
        mapaJSON = gson.toJson(batallaDAO.getBatallasDivision(division));

        session.close();
        return SUCCESS;

    }
}
