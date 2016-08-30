package control.sets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.lang.reflect.Type;
import java.util.List;
import model.DAO.BatallaDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.HibernateUtil;
import model.jsonClass.EstadisticasBatalla;
import org.hibernate.*;

public class SetResultadosBatallas extends ActionSupport {

    private String resultados;

    public String execute() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();

        BatallaDAO batallaDAO = new BatallaDAO(session);
        Gson gson = new Gson();
        Type fooType = new TypeToken<List<EstadisticasBatalla>>() {
        }.getType();

        List<EstadisticasBatalla> estadisticas = gson.fromJson(resultados, fooType);
        
        int div = 0;
        for (EstadisticasBatalla estadisticasBatalla : estadisticas) {
            long batallaId = estadisticasBatalla.getId();
            div = batallaDAO.setResultadoBatalla(userId, batallaId, estadisticasBatalla);
        }
        
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        usuarioDAO.ordenarPosiciones(div);
        
        session.close();
        return SUCCESS;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    public String getResultados() {
        return resultados;
    }

}