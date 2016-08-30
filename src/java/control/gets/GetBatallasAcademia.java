package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import model.DAO.BatallaDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.Batalla;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class GetBatallasAcademia extends ActionSupport {

    public String mapaJSON = "error";

    @Override
    public String execute() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        BatallaDAO batallaDAO = new BatallaDAO(session);
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        List<Batalla> batallas = batallaDAO.getBatallasAcademia(userId);
        session.flush();

        for(Batalla batalla : batallas){
            batalla.eqLocId = batalla.getEqLoc().getId();
            batalla.eqLocName = usuarioDAO.getName(batalla.getEqLoc());
            batalla.setEqLoc(null);
            batalla.eqVisId = batalla.getEqVis().getId();
            batalla.eqVisName = usuarioDAO.getName(batalla.getEqVis());
            batalla.setEqVis(null);
        }
        Gson gson = new Gson();
        mapaJSON = gson.toJson(batallas);

        session.close();

        return SUCCESS;

    }
}
