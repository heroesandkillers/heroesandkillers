package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import model.DAO.TrofeoDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Trofeo;
import org.hibernate.Session;

public class GetTrofeos extends ActionSupport {

    public String mapaJSON = "";
    private int id;

    @Override
    public String execute() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        TrofeoDAO trofeoDAO = new TrofeoDAO(session);
        List<Trofeo> trofeos = trofeoDAO.getTrofeosUsuario(id);

        if (!trofeos.isEmpty()) {
            Gson gson = new Gson();
            mapaJSON = gson.toJson(trofeos);
        }

        session.close();
        return SUCCESS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}