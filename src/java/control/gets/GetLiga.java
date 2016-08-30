package control.gets;

import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import model.DAO.UsuarioDAO;
import model.DAO.DivisionDAO;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class GetLiga extends ActionSupport {

    public int division;
    public String mapaJSON = "error";

    public String execute() {
        List<Usuario> mapa = getLiga(division);
        Gson gson = new Gson();
        mapaJSON = gson.toJson(mapa);
        return SUCCESS;
    }

    public List<Usuario> getLiga(int div) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);

        List<Usuario> mapa = usuarioDAO.getUsuariosDivision(div);
        if (mapa.size() != DivisionDAO.leagueUsers) {
            DivisionDAO divisionDAO = new DivisionDAO(session);
            divisionDAO.checkDivision(div, mapa.size());
        }
        session.flush();
        session.clear();

        for (Usuario usuario : mapa) {
            usuario.username = usuarioDAO.getName(usuario);
            usuario.setPhpbb_user(null);
//            usuario.setDivision(null);
            usuario.setAlin(null);
            usuario.setAlinAcad(null);
            usuario.setEntreno(null);
            usuario.setPujas(null);
            usuario.setGraficos(null);
            usuario.setAcademia(null);
            usuario.setOro(null);

//            String json = usuario.getCons();
//            if (json != null) {
//                Gson gson = new Gson();
//                Cons cons = gson.fromJson(json, Cons.class);
//                usuario.setCons(gson.toJson(cons));
//                usuarioDAO.update(usuario);
//            }
        }
        session.close();
        return mapa;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    class Cons {

        String fort;
        String ento;
    }
}
