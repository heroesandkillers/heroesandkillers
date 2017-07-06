package control.gets;

import model.DAO.BatallaDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionSupport;
import model.hibernate.*;
import java.util.List;
import org.hibernate.Session;

public class GetBatalla extends ActionSupport {

    public String mapaJSON = "error";
    private Integer id;

    @Override
    public String execute() throws Exception {
        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        BatallaDAO batallaDAO = new BatallaDAO(session);
        
        Batalla batalla;
        if(null != id){
            batalla = batallaDAO.getBatalla(id);
        }else{
            batalla = batallaDAO.getUltimaBatallaCalculada();        
        }
        
        session.flush();

        if (batalla.getTipo().equals("academia")) {

            List<Criatura> local = gson.fromJson(batalla.getAlinLoc(), new TypeToken<List<Criatura>>() {
            }.getType());

            if (null == local) {
                mapaJSON = "";
                return SUCCESS;
            }

            for (Criatura criatura : local) {
                criatura.setMedia(null);
                criatura.setFuerza(null);
                criatura.setAgilidad(null);
                criatura.setReflejos(null);
                criatura.setMagia(null);
                criatura.setConstitucion(null);
                criatura.setDefensa(null);
                criatura.setReaccion(null);
                criatura.setElemento("");
                criatura.setMutacion("");
            }
            batalla.setAlinLoc(gson.toJson(local));

            List<Criatura> visitante = gson.fromJson(batalla.getAlinVis(), new TypeToken<List<Criatura>>() {
            }.getType());

            if (null == visitante) {
                mapaJSON = "";
                return SUCCESS;
            }

            for (Criatura criatura : visitante) {
                criatura.setMedia(null);
                criatura.setFuerza(null);
                criatura.setAgilidad(null);
                criatura.setReflejos(null);
                criatura.setMagia(null);
                criatura.setConstitucion(null);
                criatura.setDefensa(null);
                criatura.setReaccion(null);
                criatura.setElemento("");
                criatura.setMutacion("");
            }
            batalla.setAlinVis(gson.toJson(visitante));
        }

        if (batalla.getEqLoc().getPhpbb_user() != null) {
            batalla.eqLocName = batalla.getEqLoc().getPhpbb_user().getUsername();
        } else {
            batalla.eqLocName = "bot_" + batalla.getEqLoc().getId();
        }
        if (batalla.getEqVis().getPhpbb_user() != null) {
            batalla.eqVisName = batalla.getEqVis().getPhpbb_user().getUsername();
        } else {
            batalla.eqVisName = "bot_" + batalla.getEqVis().getId();
        }

        batalla.setEqLoc(null);
        batalla.setEqVis(null);
        batalla.setCalculos(null);
        batalla.setRes2(null);
        batalla.setRes3(null);

        session.close();
        mapaJSON = gson.toJson(batalla);
        return SUCCESS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
