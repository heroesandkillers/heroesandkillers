package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import model.DAO.DivisionDAO;
import model.hibernate.Division;
import model.hibernate.HibernateUtil;
import org.hibernate.Session;

public class GetDivision extends ActionSupport {

    public String mapaJSON = "error";
    public int division;

    public String execute() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        DivisionDAO divisionDAO = new DivisionDAO(session);
        Division div = divisionDAO.getDivision(division);
        session.close();

        Gson gson = new Gson();
        mapaJSON = gson.toJson(div);
        return SUCCESS;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public long getDivision() {
        return division;
    }
}