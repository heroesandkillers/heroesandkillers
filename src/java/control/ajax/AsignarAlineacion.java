package control.ajax;

import model.jsonClass.Alineacion;
import model.DAO.UsuarioDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.hibernate.*;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;


public class AsignarAlineacion extends ActionSupport {

    private String alineacion;
    private String response = "error";

    public String execute() throws Exception {

        try {

            Session session = HibernateUtil.getSessionFactory().openSession();
            int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
            UsuarioDAO usuarioDAO = new UsuarioDAO(session);

            Gson gson = new Gson();
            System.out.println("alineacion = " + alineacion);
            List<Alineacion> alin = gson.fromJson(alineacion, new TypeToken<List<Alineacion>>() {
            }.getType());

            long[] ids = new long[5];
            int[] posiciones = new int[5];

            long id;
            int posicion;
            for (int i = 0; i < alin.size(); i++) {
                id = alin.get(i).getId();
                ids[i] = id;
                posicion = alin.get(i).getPosicion();
                posiciones[i] = posicion;
            }

            Arrays.sort(ids);
            Arrays.sort(posiciones);
            //captar ERRORES causado por trampas
            for (int i = 0; i < ids.length; i++) {
                for (int j = 0; j < ids.length; j++) {
                    if (i != j && ids[i] > 0 && posiciones[i] > 0 && posiciones[i] < 16 && (ids[i] == ids[j] || posiciones[i] == posiciones[j])) {
                        response = "Lo sentimos, se ha detectado un error en esa alineación. (1)";
                        return SUCCESS;
                    }
                }
            }

            usuarioDAO.setAlineacionUsuario(userId, alineacion);
            session.close();
            response = "";
            return SUCCESS;

        } catch (Exception e) {
            response = "Lo sentimos, se ha producido un error con esa alineación. (2)";
            return SUCCESS;
        }
    }

    public void setAlineacion(String alineacion) {
        this.alineacion = alineacion;
    }

    public String getAlineacion() {
        return alineacion;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}