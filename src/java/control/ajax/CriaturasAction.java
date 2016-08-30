package control.ajax;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import java.util.ArrayList;
import java.util.List;
import model.DAO.CriaturaDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.Criatura;
import model.hibernate.CriaturaAcademia;
import model.hibernate.CriaturaMazmorra;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class CriaturasAction {

    public String mapaJSON = "error";
    public String peticionJson;
    public int id;

    public void execute() {
    }

    public String GetEquipo() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        Gson gson = new Gson();
        mapaJSON = gson.toJson(getEquipo(userId));
        return SUCCESS;
    }

    public List<Criatura> getEquipo(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        criaturaDAO.comprobarEntreno(userId);
        List<Criatura> equipo = criaturaDAO.getCriaturasUsuario(userId);
        session.flush();
        session.close();

        for (Criatura criatura : equipo) {
            criatura.setUsuario(null);
            criatura.setFuerza(null);
            criatura.setMagia(null);
            criatura.setAgilidad(null);
            criatura.setReflejos(null);
            criatura.setConstitucion(null);
            criatura.setDefensa(null);
            criatura.setReaccion(null);
            criatura.setXp(null);
        }

        return equipo;
    }

    public String GetEquipoAcademia() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        List<CriaturaAcademia> equipo = getEquipoAcademia(userId);
        Gson gson = new Gson();
        mapaJSON = gson.toJson(equipo);
        return SUCCESS;
    }

    public List<CriaturaAcademia> getEquipoAcademia(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        List<CriaturaAcademia> equipoAcademia = criaturaDAO.getAcademiaUsuario(userId);
        session.close();

        for (CriaturaAcademia criatura : equipoAcademia) {
            criatura.setUsuario(null);
            criatura.setMedia(null);
            criatura.setFuerza(null);
            criatura.setMagia(null);
            criatura.setAgilidad(null);
            criatura.setReflejos(null);
            criatura.setConstitucion(null);
            criatura.setDefensa(null);
            criatura.setReaccion(null);
            criatura.setElemento(null);
            criatura.setMutacion(null);
            criatura.setEvolucion(null);
        }
        return equipoAcademia;
    }

    public String GetEquipoContacto() {
        List<Criatura> equipo = getEquipoContacto(id);
        Gson gson = new Gson();
        mapaJSON = gson.toJson(equipo);

        return SUCCESS;
    }

    public List<Criatura> getEquipoContacto(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        List<Criatura> equipo = criaturaDAO.getCriaturasUsuario(id);
        session.flush();
        session.close();

        for (Criatura criatura : equipo) {
            criatura.setUsuario(null);
            criatura.setFuerza(null);
            criatura.setMagia(null);
            criatura.setAgilidad(null);
            criatura.setReflejos(null);
            criatura.setConstitucion(null);
            criatura.setDefensa(null);
            criatura.setReaccion(null);
            criatura.setXp(null);
            criatura.setEvolucion(null);
        }
        return equipo;
    }

    public String getMisPujas() {
        if (null == ActionContext.getContext().getSession()) {
            mapaJSON = "la sesión ha expirado";
            return SUCCESS;
        }

        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");

        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        CriaturaDAO criaturaDAO = new CriaturaDAO(session);

        Usuario usuario = usuarioDAO.loadUsuario(userId);
        List<CriaturaMazmorra> mazmorras;
        List<Long> lista;

//        try {
        String pujas = usuario.getPujas();
        if(null == pujas || pujas.isEmpty()){
            mapaJSON = "";
            return SUCCESS;
        }
        
        lista = gson.fromJson(pujas, new TypeToken<List<Long>>() {
        }.getType());

        mazmorras = criaturaDAO.getMisPujas(userId, lista);
        session.evict(mazmorras);
        session.close();

        for (CriaturaMazmorra criatura : mazmorras) {
            criatura.setPujaMax(0L);

            criatura.setFuerza((double) Math.round(criatura.getFuerza()));
            criatura.setMagia((double) Math.round(criatura.getMagia()));
            criatura.setAgilidad((double) Math.round(criatura.getAgilidad()));
            criatura.setReflejos((double) Math.round(criatura.getReflejos()));
            criatura.setConstitucion((double) Math.round(criatura.getConstitucion()));
            criatura.setDefensa((double) Math.round(criatura.getDefensa()));
            criatura.setReaccion((double) Math.round(criatura.getReaccion()));
            criatura.setXp((double) Math.round(criatura.getXp()));

            criatura.setEvolucion(null);
        }
        mapaJSON = gson.toJson(mazmorras);

//        } catch (Exception e) {
//            System.out.println("catch empty on CriaturasAction.getMisPujas()");
//            lista = new ArrayList();
//            mapaJSON = "";
//        }
//        usuario.setPujas(gson.toJson(lista));
//        usuarioDAO.update(usuario);    
//        session.close();

        return SUCCESS;
    }

    public String getMazmorrasRango() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        List<CriaturaMazmorra> mazmorras = criaturaDAO.getMazmorrasRango(peticionJson);
        session.close();

        for (CriaturaMazmorra criatura : mazmorras) {
            criatura.setFuerza((double) Math.round(criatura.getFuerza()));
            criatura.setMagia((double) Math.round(criatura.getMagia()));
            criatura.setAgilidad((double) Math.round(criatura.getAgilidad()));
            criatura.setReflejos((double) Math.round(criatura.getReflejos()));
            criatura.setConstitucion((double) Math.round(criatura.getConstitucion()));
            criatura.setDefensa((double) Math.round(criatura.getDefensa()));
            criatura.setReaccion((double) Math.round(criatura.getReaccion()));
            criatura.setXp((double) Math.round(criatura.getXp()));

            criatura.setEvolucion(null);
            if (criatura.getUsuario() != null) {
                criatura.user = criatura.getUsuario().getId();
                criatura.setUsuario(null);
            }
            criatura.lvl = criaturaDAO.getNivel(criatura.getXp());
            criatura.setXp(null);
        }

        Gson gson = new Gson();
        mapaJSON = gson.toJson(mazmorras);
        return SUCCESS;
    }

    public String getPeticionJson() {
        return peticionJson;
    }

    public void setPeticionJson(String peticionJson) {
        this.peticionJson = peticionJson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
