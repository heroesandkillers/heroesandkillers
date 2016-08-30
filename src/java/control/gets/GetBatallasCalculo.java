package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import model.DAO.BatallaDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.Batalla;
import model.hibernate.HibernateUtil;
import org.hibernate.Session;

public class GetBatallasCalculo extends ActionSupport {

    public String mapaJSON = "error";

    public String execute() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Gson gson = new Gson();
        
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        int division = usuarioDAO.getUsuario(userId).getDivision().getDivision();
        
        BatallaDAO batallaDAO = new BatallaDAO(session);        
        List<Batalla> batallas;

        batallas = batallaDAO.getBatallasCalculo(division);
        mapaJSON = gson.toJson(batallas);

        session.close();
        return SUCCESS;
    }
}
