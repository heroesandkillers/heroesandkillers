package control.ajax;

import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import java.util.List;
import model.DAO.BatallaDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.Batalla;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class CalendarioAction {

    public int id;
    public int division;
    public String mapaJSON = "error";
    boolean pendientes = false;
    boolean batallas = false;
    boolean repesca = false;

    public void execute() throws Exception {
    }

    public String GetCalendario() {
        Gson gson = new Gson();
        mapaJSON = gson.toJson(getCalendario(id));
        return SUCCESS;
    }

    public List<Batalla> getCalendario(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        BatallaDAO batallaDAO = new BatallaDAO(session);
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        Usuario yo = usuarioDAO.getUsuario(userId);

        List<Batalla> calendario = batallaDAO.getBatallasUsuario(
                yo.getDivision().getDivision(),
                yo.getId()
        );
        session.flush();
        session.close();

        for (Batalla batalla : calendario) {
            batalla.eqLocId = batalla.getEqLoc().getId();
            batalla.eqLocName = usuarioDAO.getName(batalla.getEqLoc());
            batalla.setEqLoc(null);
            batalla.eqVisId = batalla.getEqVis().getId();
            batalla.eqVisName = usuarioDAO.getName(batalla.getEqVis());
            batalla.setEqVis(null);
            batalla.setDivision(null);
//            batalla.setAlinLoc(null);
//            batalla.setAlinVis(null);
            batalla.setRes2(null);
            batalla.setRes3(null);

            batalla.setRes(null);
        }

        return calendario;
    }

    public String GetCalendarioDivision() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Batalla> lista = getCalendarioDivision(division);

        if (null != lista) {
            Gson gson = new Gson();
            mapaJSON = gson.toJson(lista);
        }else{
            mapaJSON = "";
        }

        session.close();
        return SUCCESS;
    }

    public List<Batalla> getCalendarioDivision(int div) { //ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        System.out.println("getCalendarioDivision");
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Batalla> calendario = cargarCalendario(div);

        if (false == pendientes) {
            System.out.println("no hay batallas pendientes por calcular");
            BatallaDAO batallaDAO = new BatallaDAO(session);

            //if need repesca
            if (repesca == false && batallas == true) {
                System.out.println("batallaDAO.crearBatallasRepesca in CalendarioAction");
                boolean resolved = batallaDAO.crearBatallasRepesca(div);

                if (!resolved) {
                    batallaDAO.crearBatallasCalendario(div);
                    pendientes = true;
                }

                //if no battles
            } else {
                System.out.println("batallaDAO.crearBatallasCalendario in CalendarioAction");
                boolean playersDivision = batallaDAO.crearBatallasCalendario(div);
                if (!playersDivision) {
                    return null;
                }
                pendientes = true;
            }
        }

        session.flush();
        session.clear();
        for (int i = 0; i < calendario.size(); i++) {
            calendario.get(i).setDivision(null);
        }

        session.close();
        return calendario;
    }

    private List<Batalla> cargarCalendario(int div) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        BatallaDAO batallaDAO = new BatallaDAO(session);
        List<Batalla> calendario = batallaDAO.getCalendarioDivision(div);
//        session.flush();        
//        session.close();
        session.clear();

        System.out.println("calendario.size() = " + calendario.size());
        for (Batalla batalla : calendario) {
            try {
                batalla.eqLocId = batalla.getEqLoc().getId();
                batalla.eqVisId = batalla.getEqVis().getId();
            } catch (Exception e) {
                calendario.remove(batalla);
                session.delete(batalla);
                System.out.println("batalla sin usuarios asignados ha sido eliminada.");
                break;
            }

            batalla.eqLocName = usuarioDAO.getName(batalla.getEqLoc());
            batalla.setEqLoc(null);
            batalla.eqVisName = usuarioDAO.getName(batalla.getEqVis());
            batalla.setEqVis(null);
//            batalla.setDivision(null);
            batalla.setAlinLoc(null);
            batalla.setAlinVis(null);
            batalla.setRes2(null);
            batalla.setRes3(null);

            batalla.setRes(null); //prueba

            if (!pendientes && batalla.getCalculos() == 0) {
                System.out.println("pendientes = true");
                pendientes = true;
            }
            if (!repesca && batalla.getTipo().equals("repesca")) {
                System.out.println("repesca = true");
                repesca = true;
            }
            batallas = true;
        }
        session.close();

        return calendario;
    }
    
    public void clearOldBatallas(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        BatallaDAO batallaDAO = new BatallaDAO(session);
        batallaDAO.eliminarBatallaCalendarioAntiguas();
        session.close();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public int getDivision() {
        return division;
    }
}
