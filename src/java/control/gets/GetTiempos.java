package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import model.DAO.TiempoDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Tiempo;
import org.hibernate.Session;

public class GetTiempos extends ActionSupport {

    public String mapaJSON = "";

    @Override
    public String execute() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        TiempoDAO tiempoDAO = new TiempoDAO(session);
        List<Tiempo> tiempos = tiempoDAO.getTiempos();

        Gson gson = new Gson();
        mapaJSON = gson.toJson(tiempos);

        session.close();
        return SUCCESS;

    }
}