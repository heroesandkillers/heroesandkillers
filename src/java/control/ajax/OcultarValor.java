package control.ajax;

import model.DAO.CriaturaDAO;
import model.DAO.CriaturaDAO.Destapes;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import model.hibernate.*;
import org.hibernate.*;

public class OcultarValor extends ActionSupport {

    private String response = "error";
    private long id;
    private String atr;

    public String execute() throws Exception {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        CriaturaAcademia criatura = criaturaDAO.loadCriaturaAcademia(id);

        if (criatura.getUsuario().getId() == userId) {

            Gson gson = new Gson();
            Destapes destapes = gson.fromJson(criatura.getDestapes(), Destapes.class);

            if (atr.equals("fuerza")) {
                destapes.desFu++;
                destapes.fu = 0;
            } else if (atr.equals("magia")) {
                destapes.desMa++;
                destapes.ma = 0;
            } else if (atr.equals("agilidad")) {
                destapes.desAg++;
                destapes.ag = 0;
            } else if (atr.equals("reflejos")) {
                destapes.desRf++;
                destapes.rf = 0;
            } else if (atr.equals("constitucion")) {
                destapes.desCo++;
                destapes.co = 0;
            } else if (atr.equals("defensa")) {
                destapes.desDf++;
                destapes.df = 0;
            } else if (atr.equals("reaccion")) {
                destapes.desRc++;
                destapes.rc = 0;
            } else if (atr.equals("mutacion")) {
                if (destapes.mut.equals("none")) {
                    destapes.desMut++;
                    destapes.mut = "";
                }
            } else if (atr.equals("elemento")) {
                if (destapes.ele.equals("none")) {
                    destapes.desEle++;
                    destapes.ele = "";
                }
            }
            
            criatura.setDestapes(gson.toJson(destapes));
            criaturaDAO.update(criatura);

            session.close();
            response = "";
            return SUCCESS;

        } else {
            session.close();
            response = "La criatura recibida no es tuya";
            return SUCCESS;
        }

    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setAtr(String atr) {
        this.atr = atr;
    }

    public String getAtr() {
        return atr;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
