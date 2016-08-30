package control.ajax;

import static com.opensymphony.xwork2.Action.SUCCESS;
import model.DAO.DivisionDAO;
import model.hibernate.HibernateUtil;
import org.hibernate.Session;

public class DivisionesAction {

    public String mapaJSON = "error";

    public void execute() throws Exception {
    }

    public String GetDivisiones() {
        mapaJSON = getDivisiones() + "";
        return SUCCESS;
    }

    public int getDivisiones() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        DivisionDAO divisionDAO = new DivisionDAO(session);
        int divisiones = divisionDAO.numSubDivisiones();
        session.close();
        return divisiones;
    }
}