package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.DAO.UsuarioDAO;
import model.hibernate.HibernateUtil;
import org.hibernate.Session;

public class GetEntreno extends ActionSupport {

    public String mapaJSON = "error";

    @Override
    public String execute() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
            
            UsuarioDAO usuarioDAO = new UsuarioDAO(session);
            String entreno = usuarioDAO.getEntreno(userId);
            
            Gson gson = new Gson();
            mapaJSON = gson.toJson(entreno);

            session.close();            
            return SUCCESS;

        } catch (Exception e) {
            return ERROR;
        }
    }
}