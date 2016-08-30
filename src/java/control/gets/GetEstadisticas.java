package control.gets;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import model.DAO.CriaturaDAO;
import model.DAO.UsuarioDAO;
import model.hibernate.Criatura;
import model.hibernate.HibernateUtil;
import model.hibernate.Usuario;
import org.hibernate.Session;

public class GetEstadisticas extends ActionSupport {

    private Session session;
    private int rangoMin;
    private int rangoMax;
    private String response;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public String posicionAbsoluta() {
        session = HibernateUtil.getSessionFactory().openSession();

        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        List<Usuario> usuarios = usuarioDAO.getRango(rangoMin, rangoMax);

        for (Usuario usuario : usuarios) {
            usuario.setOro(null);
            usuario.setEntreno(null);
            usuario.setAlin(null);
            usuario.setAlinAcad(null);
            usuario.setFichajes(null);
            usuario.setEconomia(null);
            usuario.setEstad(null);
            usuario.setActivo(null);
            usuario.setCons(null);
            usuario.setGraficos(null);
            usuario.setPujas(null);
            usuario.div = usuario.getDivision().getDivision();
            usuario.setDivision(null);            
        }

        Gson gson = new Gson();
        response = gson.toJson(usuarios);

        session.close();
        return SUCCESS;
    }

    public String mediaCriaturas() {
        int userId = (Integer) ActionContext.getContext().getSession().get("usuario");
        session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        List<Criatura> criaturas = criaturaDAO.getRango(rangoMin, rangoMax);

        for (Criatura criatura : criaturas) {

            if (criatura.getUsuario().getId() == userId) {
                criatura.setFuerza((double)Math.round(criatura.getFuerza()));
                criatura.setMagia((double)Math.round(criatura.getMagia()));
                criatura.setAgilidad((double)Math.round(criatura.getAgilidad()));
                criatura.setReflejos((double)Math.round(criatura.getReflejos()));
                criatura.setConstitucion((double)Math.round(criatura.getConstitucion()));
                criatura.setDefensa((double)Math.round(criatura.getDefensa()));
                criatura.setReaccion((double)Math.round(criatura.getReaccion()));
            }else{
                criatura.setFuerza(null);
                criatura.setMagia(null);
                criatura.setAgilidad(null);
                criatura.setReflejos(null);
                criatura.setConstitucion(null);
                criatura.setDefensa(null);
                criatura.setReaccion(null);
                
                criatura.setPrecio(null);
            }
            criatura.setXp((double)Math.round(criatura.getXp()));
        }

        Gson gson = new Gson();
        response = gson.toJson(criaturas);

        session.close();
        return SUCCESS;
    }

//    public String campeones() {
//        session = HibernateUtil.getSessionFactory().openSession();
//
//        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
//        List<Usuario> usuarios = usuarioDAO.getCampeones(rangoMin, rangoMax);
//
//        List<UsuarioExterno> rankPosicion = new ArrayList<UsuarioExterno>();
//
//        for (Usuario usuario : usuarios) {
//
//            UsuarioExterno usuarioE = new UsuarioExterno();
//
//            usuarioE.setId(usuario.getId());
//            usuarioE.setNombreId(usuario.getNombreId());
//            usuarioE.setPosicion(usuario.getPosicion());
//            usuarioE.setDivision(usuario.getDivision());
//            usuarioE.setPuntos(usuario.getPuntos());
//
//            rankPosicion.add(usuarioE);
//        }
//
//        Gson gson = new Gson();
//        response = gson.toJson(rankPosicion);
//
//        session.close();
//        return SUCCESS;
//    }
    public String numeroUsuarios() {
        session = HibernateUtil.getSessionFactory().openSession();

        UsuarioDAO usuarioDAO = new UsuarioDAO(session);
        int num = usuarioDAO.numUsuarios();

        response = num + "";

        session.close();
        return SUCCESS;
    }

    public String numeroCriaturas() {
        session = HibernateUtil.getSessionFactory().openSession();

        CriaturaDAO criaturaDAO = new CriaturaDAO(session);
        int num = criaturaDAO.numCriaturas();

        response = num + "";

        session.close();
        return SUCCESS;
    }

    public void setRangoMin(int rangoMin) {
        this.rangoMin = rangoMin;
    }

    public long getRangoMin() {
        return rangoMin;
    }

    public void setRangoMax(int rangoMax) {
        this.rangoMax = rangoMax;
    }

    public long getRangoMax() {
        return rangoMax;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
