package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import model.DAO.TraspasoDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Traspaso;
import org.hibernate.Session;

public class GetTraspasos extends ActionSupport {

    public int id;
    public String mapaJSON = "";

    @Override
    public String execute() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        TraspasoDAO traspasoDAO = new TraspasoDAO(session);
        List<Traspaso> traspasos = traspasoDAO.getTraspasos(id);

        Gson gson = new Gson();
        mapaJSON = gson.toJson(traspasos);

        session.close();
        return SUCCESS;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}