package control.sets;

import com.opensymphony.xwork2.ActionSupport;
import model.DAO.BatallaDAO;
import model.hibernate.HibernateUtil;
import org.hibernate.*;

public class SetDenuncia extends ActionSupport {

    public long id;

    public String execute() {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        BatallaDAO batallaDAO = new BatallaDAO(session);
        batallaDAO.setDenuncia(id);
        session.close();
        
        return SUCCESS;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}