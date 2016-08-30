package control.sets;

import com.opensymphony.xwork2.ActionSupport;
import java.io.UnsupportedEncodingException;
import model.DAO.DivisionDAO;
import model.hibernate.HibernateUtil;
import org.hibernate.*;

public class SetMapa extends ActionSupport {

    public int division;
    private String mapa;
    private byte[] back;

    public String execute() {        
        Session session = HibernateUtil.getSessionFactory().openSession();
        DivisionDAO divisionDAO = new DivisionDAO(session);
        divisionDAO.setMapa(division, mapa, back);
        session.close();
        
        return SUCCESS;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public int getDivision() {
        return division;
    }

    public void setMapa(String mapa) {
        this.mapa = mapa;
    }

    public String getMapa() {
        return mapa;
    }
    
    public void setBack(String back) {
        this.back =  back.getBytes();
    }

    public String getBack() throws UnsupportedEncodingException {
        return new String(back, "UTF-8");
    }
}