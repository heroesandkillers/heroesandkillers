package control.ajax;

import model.DAO.CriaturaDAO.Entreno;
import model.DAO.UsuarioDAO;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import model.hibernate.*;
import org.hibernate.*;

public class AsignarEntreno extends ActionSupport {

    public String mapaJSON = "error";

    private String fuerza = null;
    private String magia = null;
    private String agilidad = null;
    private String reflejos = null;
    private String constitucion = null;
    private String defensa = null;
    private String reaccion = null;

    public String execute() throws Exception {

        //throw error on empty
        if (null == fuerza && null == magia && null == agilidad && null == reflejos && null == constitucion && null == defensa && null == reaccion) {
            mapaJSON = "entrenamiento vacío";
            return SUCCESS;
        }

        Entreno entreno = new Entreno();

        entreno.fuerza = Integer.parseInt(fuerza);
        entreno.magia = Integer.parseInt(magia);
        entreno.agilidad = Integer.parseInt(agilidad);
        entreno.reflejos = Integer.parseInt(reflejos);
        entreno.constitucion = Integer.parseInt(constitucion);
        entreno.defensa = Integer.parseInt(defensa);
        entreno.reaccion = Integer.parseInt(reaccion);

        if (entreno.fuerza + entreno.magia + entreno.agilidad + entreno.reflejos + entreno.constitucion + entreno.defensa + entreno.reaccion > 25) {
            mapaJSON = "la cantidad de entreno no es válida";
            return SUCCESS;
        }

        Gson gson = new Gson();
        String entr = gson.toJson(entreno);
        ActionContext ctx = ActionContext.getContext();
        Map<String, Object> s = ctx.getSession();
        Object usuario = s.get("usuario");
        int userId = (Integer) usuario;

        Session session = HibernateUtil.getSessionFactory().openSession();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        usuarioDAO.setEntreno(userId, entr);

        mapaJSON = "";
        session.close();
        return SUCCESS;
    }

    public void setFuerza(String fuerza) {
        this.fuerza = fuerza;
    }

    public String getFuerza() {
        return fuerza;
    }

    public void setMagia(String magia) {
        this.magia = magia;
    }

    public String getMagia() {
        return magia;
    }

    public void setAgilidad(String agilidad) {
        this.agilidad = agilidad;
    }

    public String getAgilidad() {
        return agilidad;
    }

    public void setReflejos(String reflejos) {
        this.reflejos = reflejos;
    }

    public String getReflejos() {
        return reflejos;
    }

    public void setConstitucion(String constitucion) {
        this.constitucion = constitucion;
    }

    public String getConstitucion() {
        return constitucion;
    }

    public void setDefensa(String defensa) {
        this.defensa = defensa;
    }

    public String getDefensa() {
        return defensa;
    }

    public void setReaccion(String reaccion) {
        this.reaccion = reaccion;
    }

    public String getReaccion() {
        return reaccion;
    }
}
